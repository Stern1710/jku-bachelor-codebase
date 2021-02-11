from matplotlib.colors import TABLEAU_COLORS
import matplotlib.pyplot as plt
import numpy as np
from .. problem import Problem
from typing import Iterable


# this is a convenience function that displays a simple 2D problem
# optionally it can take a list of names with start nodes and action sequences
#
# sequences = [
#     (name_a, start_node_a, [action_x, action_y, ...]),
#     (name_b, start_node_b, [action_u, action_v, ...]),
#     .
#     .
#     .
# ]
#
# optionally it can take a list of names with lists containing nodes to highlight
# nodes = [
#     (name_a, 'x', [node_x, node_y, ...]),
#     (name_b, 'o', [node_u, node_v, ...]),
#     .
#     .
#     .
# ]
def plot_field_and_costs(problem: Problem,
                         sequences: Iterable=None,
                         nodes: Iterable=None,
                         show_coordinates=False,
                         show_grid=False):
    fig = plt.figure()
    field_ax, costs_ax = plot_field_and_costs_aux(fig, problem, show_coordinates, show_grid)
    if sequences is not None and len(sequences) > 0:
        plot_sequences(fig, field_ax, problem, sequences)
        plot_sequences(fig, costs_ax, problem, sequences)

    if nodes is not None and len(nodes) > 0:
        plot_nodes(fig, field_ax, nodes)

    plt.tight_layout()
    plt.show()


def plot_sequences(fig, ax, problem, sequences):
    for (name, start_node, action_sequence), color in zip(sequences, TABLEAU_COLORS):
        draw_path(fig, ax, problem, name, start_node, action_sequence, color)

    ax.legend(
        bbox_to_anchor=(-1, 1),
        loc='upper left',
    ).set_draggable(True)


def plot_nodes(fig, ax, nodes):
    for (name, marker, node_collection), color in zip(nodes, TABLEAU_COLORS):
        draw_nodes(fig, ax, name, node_collection, color, marker)

    ax.legend(
        bbox_to_anchor=(-1, 1),
        loc='lower left',
    ).set_draggable(True)


def draw_nodes(fig, ax, name, node_collection, color, marker):
    states = np.array([node.state for node in node_collection])
    if len(states) > 0:
        ax.scatter(states[:, 0], states[:, 1], color=color, label=name, marker=marker)


def plot_field_and_costs_aux(fig, problem, show_coordinates, show_grid,
                             field_ax=None, costs_ax=None):

    if field_ax is None:
        ax = field_ax = plt.subplot(211)
    else:
        ax = field_ax

    ax.set_title('The field')
    im = ax.imshow(problem.board.T, cmap='gray_r')
    fig.colorbar(im)

    if costs_ax is None:
        ax = costs_ax = plt.subplot(212, sharex=ax, sharey=ax)
    else:
        ax = costs_ax

    ax.set_title('The costs (for stepping on a tile)')
    im = ax.imshow(problem.costs.T, cmap='viridis')
    fig.colorbar(im)

    for ax in [field_ax, costs_ax]:
        ax.tick_params(
            top=show_coordinates,
            left=show_coordinates,
            labelleft=show_coordinates,
            labeltop=show_coordinates,
            right=False,
            bottom=False,
            labelbottom=False
        )

        # Major ticks
        s = problem.board.shape[0]
        ax.set_xticks(np.arange(0, s, 1))
        ax.set_yticks(np.arange(0, s, 1))

        # Minor ticks
        ax.set_xticks(np.arange(-.5, s, 1), minor=True)
        ax.set_yticks(np.arange(-.5, s, 1), minor=True)

    if show_grid:
        for color, ax in zip(['m', 'w'], [field_ax, costs_ax]):
            # Gridlines based on minor ticks
            ax.grid(which='minor', color=color, linestyle='-', linewidth=1)

    return field_ax, costs_ax


def draw_path(fig, ax, problem: Problem, name, start_node, action_sequence, color):
    current = start_node
    xs = [current.state[0]]
    ys = [current.state[1]]
    us = [0]
    vs = [0]

    length = len(action_sequence)
    cost = 0
    costs = [0] * length
    for i, action in enumerate(action_sequence):
        costs[i] = current.cost
        xs.append(current.state[0])
        ys.append(current.state[1])
        current = problem.successor(current, action)
        dx, dy = problem.ACTIONS_DELTA[action]
        us.append(dx)
        vs.append(-dy)
        cost = current.cost

    quiv = ax.quiver(
        xs, ys, us, vs,
        color=color,
        label='{} l:{} c:{}'.format(name, length, cost),
        scale_units='xy',
        units='xy',
        scale=1,
        headwidth=1,
        headlength=1,
        linewidth=1,
        picker=5
    )
    return quiv


def export_equivalent_graph_to_yed_graphml(problem: Problem):
    import pyyed
    from .. instance_generation import enc
    from .. problem import Node

    g = pyyed.Graph()

    board = problem.board
    size = board.shape[0]

    scale = 160
    for x in range(size):
        for y in range(size):
            if board[x, y] == enc.SPACE:
                g.add_node(
                    '{},{}'.format(x, y),
                    x=str(x * scale),
                    y=str(y * scale),
                    width='50',
                    height='50',
                    shape='ellipse',
                    shape_fill='#FFFFFF'
                )

    all_actions = set(problem.ACTIONS_DELTA.keys())
    for x in range(size):
        for y in range(size):
            if board[x, y] == enc.SPACE:
                state = (x, y)
                actions = set()
                # if cost = 0, we automatically get the
                # cost for the step!
                current = Node(None, state, None, 0, 0)
                for succ in problem.successors(current):
                    actions.add(succ.action)
                    sx, sy = succ.state

                    g.add_edge(
                        '{},{}'.format(x, y),
                        '{},{}'.format(sx, sy),
                        label='{} (c:{})'.format(succ.action, succ.cost)
                    )

                actions_str = ','.join(map(str, sorted(list(all_actions - actions))))
                # add self edge with actions that lead to staying in the state
                g.add_edge(
                    '{},{}'.format(x, y),
                    '{},{}'.format(x, y),
                    label='{} (c:{})'.format(actions_str, problem.costs[x, y])
                )

    # g.add_edge('foo', 'bar', label='yolo')
    # g.add_node('foo2', shape="roundrectangle", font_style="bolditalic",
    #            underlined_text="true")

    # g.add_edge('foo1', 'foo2')
    # g.add_node('abc', font_size="72", height="100")

    # g.add_node('bar', label="Multi\nline\ntext")
    # g.add_node('foobar', label="""Multi
    # Line
    # Text!""")

    # g.add_edge('foo', 'foo1', label="EDGE!", width="3.0", color="#0000FF",
    #            arrowhead="white_diamond", arrowfoot="standard", line_type="dotted")

    return g.get_graph()
