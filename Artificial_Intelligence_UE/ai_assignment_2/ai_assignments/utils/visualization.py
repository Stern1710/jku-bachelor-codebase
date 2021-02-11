from matplotlib.colors import TABLEAU_COLORS
import matplotlib.pyplot as plt
import numpy as np
from .. problem import Problem
from typing import Iterable
from mpl_toolkits.axes_grid1 import make_axes_locatable


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
                         show_grid=False,
                         plot_filename=None):
    fig = plt.figure()
    field_ax, costs_ax = plot_field_and_costs_aux(fig, problem, show_coordinates, show_grid)
    if sequences is not None and len(sequences) > 0:
        plot_sequences(fig, field_ax, problem, sequences)
        plot_sequences(fig, costs_ax, problem, sequences)

    if nodes is not None and len(nodes) > 0:
        plot_nodes(fig, field_ax, nodes)

    plt.tight_layout()
    if plot_filename is not None:
        plt.savefig(plot_filename)
        plt.close(fig)
    else:
        plt.show()


def plot_sequences(fig, ax, problem, sequences):
    for (name, start_node, action_sequence), color in zip(sequences, TABLEAU_COLORS):
        draw_path(fig, ax, problem, name, start_node, action_sequence, color)

    ax.legend(
        bbox_to_anchor=(-1, 0),
        loc='upper left',
    ).set_draggable(True)


def plot_nodes(fig, ax, nodes):
    if len(nodes) > 0:
        if len(nodes[0]) == 3:
            for (name, marker, node_collection), color in zip(nodes, TABLEAU_COLORS):
                if len(node_collection) > 0:
                    draw_nodes(fig, ax, name, node_collection, color, marker)
        else:
            for name, marker, node_collection, color in nodes:
                if len(node_collection) > 0:
                    draw_nodes(fig, ax, name, node_collection, color, marker)

        ax.legend(
            bbox_to_anchor=(-1, 0),
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

    divider = make_axes_locatable(ax)
    cax = divider.append_axes('right', size='5%', pad=0)
    cbar = fig.colorbar(im, cax=cax, orientation='vertical')
    cbar.set_ticks([0, 1])
    cbar.set_ticklabels([0, 1])

    if costs_ax is None:
        ax = costs_ax = plt.subplot(212, sharex=ax, sharey=ax)
    else:
        ax = costs_ax

    ax.set_title('The costs (for stepping on a tile)')
    im = ax.imshow(problem.costs.T, cmap='viridis')
    divider = make_axes_locatable(ax)
    cax = divider.append_axes('right', size='5%', pad=0)
    cbar = fig.colorbar(im, cax=cax, orientation='vertical')
    ticks = np.arange(problem.costs.min(), problem.costs.max() + 1)
    cbar.set_ticks(ticks)
    cbar.set_ticklabels(ticks)

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

    return g.get_graph()


def plot_search_tree(problem: Problem, fringe, closed, current, successors, tree_ax, fringe_ax):
    import networkx as nx
    from networkx.drawing.nx_pydot import graphviz_layout

    G = nx.DiGraph(ordering='out')
    node_labels = dict()
    edge_labels = dict()

    def sort_key(node):
        for order, key in enumerate(problem.ACTIONS_DELTA.keys()):
            if key == node.action:
                return order

        return -1

    node_lists_ordered = [fringe, closed, current, successors]

    all_nodes = []
    for node_list in node_lists_ordered:
        all_nodes.extend(node_list)

    all_nodes = sorted(all_nodes, key=sort_key)

    for node in all_nodes:
        G.add_node(id(node), search_node=node)
        node_labels[id(node)] = 's:{},{}\nd:{}\nc:{}'.format(
            *node.state,
            node.depth,
            node.cost
        )

    # display fringe in a different ax
    fG = nx.Graph(ordering='out')
    fG_node_labels = dict()
    fringe_color = 'tab:blue'
    if len(fringe) > 0:
        for node in fringe:
            fG.add_node(id(node))
            fG_node_labels[id(node)] = 's:{},{}\nd:{}\nc:{}'.format(
                *node.state,
                node.depth,
                node.cost
            )
    else:
        fG.add_node('empty')
        fG_node_labels['empty'] = 'the fringe is empty'
        fringe_color = 'white'

    for node in all_nodes:
        if node.parent is not None:
            edge = id(node.parent), id(node)
            G.add_edge(*edge, parent_node=node.parent)
            action_cost = problem.action_cost(node.parent.state, node.action)
            edge_labels[edge] = '{}\nc:{}'.format(node.action, action_cost)

    pos = graphviz_layout(G, prog='dot')
    fG_pos = graphviz_layout(fG, prog='dot')

    node_size = 1000
    for color, node_list in zip(TABLEAU_COLORS, node_lists_ordered):
        nx.draw_networkx_nodes(
            G, pos,
            nodelist=[id(node) for node in node_list],
            node_size=node_size,
            ax=tree_ax,
            node_color=color
        )
    nx.draw_networkx_labels(G, pos, node_labels, font_size=8, ax=tree_ax)

    nx.draw_networkx_nodes(
        fG, fG_pos,
        node_size=node_size,
        ax=fringe_ax,
        node_color=fringe_color
    )
    nx.draw_networkx_labels(fG, fG_pos, fG_node_labels, font_size=8, ax=fringe_ax)

    nx.draw_networkx_edges(G, pos, arrowstyle='-|>', arrowsize=20, node_size=node_size, ax=tree_ax)
    nx.draw_networkx_edge_labels(G, pos, edge_labels, ax=tree_ax)


def main():
    import networkx as nx
    from networkx.drawing.nx_pydot import graphviz_layout

    G = nx.DiGraph(ordering='out')

    G.add_node(4)
    G.add_node(0)
    G.add_node(1)
    G.add_node(2)
    G.add_node(3)

    G.add_edge(0, 2)
    G.add_edge(0, 1)
    G.add_edge(1, 3)
    G.add_edge(1, 4)

    pos = graphviz_layout(G, prog='dot')
    nx.draw_networkx(G, pos)
    plt.show()


if __name__ == '__main__':
    main()
