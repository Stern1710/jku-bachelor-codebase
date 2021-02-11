from matplotlib.colors import TABLEAU_COLORS
import matplotlib.pyplot as plt
import ai_assignments
import numpy as np
import argparse
import textwrap
import os

__quivers = dict()
fig = plt.figure()


def main():
    parser = argparse.ArgumentParser(
        description=textwrap.dedent('''
        this script lets you view a problem instance in JSON format,
        and will optionally display solution paths superimposed on
        the problem instance. the simple 2D environment for most of
        these assignments consists of two boards that encode

            SPACES in white color
            WALLS in black color

        in the upper display, and the costs for 'transitioning to a new state',
        which means stepping onto a new position on the board, in the lower
        display.
        '''),
        epilog=textwrap.dedent('''
        example usage:

        $ python view.py test/board.json test/bfs.path
        this will view the problem instance 'test/board.json', and superimpose
        the path 'test/bfs.path' on top of both views.

        $ python view.py test/board.json test/*.path
        this will view the problem instance 'test/board.json', and superimpose
        ALL the files in the directory 'test', that end in '.path'
        '''),
        formatter_class=argparse.RawDescriptionHelpFormatter
    )
    parser.add_argument('problem_instance_name', type=str)
    parser.add_argument('paths', nargs='*')
    parser.add_argument('--coords', default=False, action='store_true')
    parser.add_argument('--grid', default=False, action='store_true')
    args = parser.parse_args()

    problem = ai_assignments.load_problem_instance(args.problem_instance_name)

    ax = ax1 = plt.subplot(211)
    ax.set_title('The field')
    im = ax.imshow(problem.board, cmap='gray_r')
    fig.colorbar(im)

    print('start_state', problem.start_state)
    print('end_state', problem.end_state)

    ax.annotate('s', (problem.start_state[1], problem.start_state[0]))
    ax.scatter(problem.start_state[1], problem.start_state[0])

    ax.annotate('e', (problem.end_state[1], problem.end_state[0]))
    ax.scatter(problem.end_state[1], problem.end_state[0])

    ax = ax2 = plt.subplot(212, sharex=ax, sharey=ax)
    ax.set_title('The costs (for stepping on a tile)')
    im = ax.imshow(problem.costs, cmap='viridis')
    fig.colorbar(im)

    for ax in [ax1, ax2]:
        for path_fn, color in zip(args.paths, TABLEAU_COLORS):
            name = os.path.splitext(os.path.split(path_fn)[-1])[0]
            with open(path_fn, 'r') as fh:
                sequence_string = fh.read()
                if sequence_string == '':
                    print('path file {} is empty'.format(path_fn))
                else:
                    sequence = sequence_string.split(',')
                    draw_path(ax, problem, name, sequence, color)

    if len(args.paths) > 0:
        ax1.legend(
            bbox_to_anchor=(-1, 1),
            loc='upper left',
        )

    for ax in [ax1, ax2]:
        ax.tick_params(
            top=False,
            bottom=args.coords,
            left=args.coords,
            right=False,
            labelleft=args.coords,
            labelbottom=args.coords
        )

        # Major ticks
        s = problem.board.shape[0]
        ax.set_xticks(np.arange(0, s, 1))
        ax.set_yticks(np.arange(0, s, 1))

        # Minor ticks
        ax.set_xticks(np.arange(-.5, s, 1), minor=True)
        ax.set_yticks(np.arange(-.5, s, 1), minor=True)

    if args.grid:
        for color, ax in zip(['m', 'w'], [ax1, ax2]):
            # Gridlines based on minor ticks
            ax.grid(which='minor', color=color, linestyle='-', linewidth=1)

    cid = fig.canvas.mpl_connect('pick_event', on_pick)
    plt.tight_layout()
    plt.show()


def draw_path(ax, problem, name, sequence, color):
    current = problem.get_start_node()
    xs = [current.state[1]]
    ys = [current.state[0]]
    us = [0]
    vs = [0]

    length = len(sequence)
    cost = 0
    costs = [0] * length
    for i, action in enumerate(sequence):
        costs[i] = current.cost
        xs.append(current.state[1])
        ys.append(current.state[0])
        current = problem.successor(current, action)
        dy, dx = problem.ACTIONS_DELTA[action]
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
    __quivers[quiv] = (name, len(__quivers), sequence, costs)


def on_pick(event):
    quiv = event.artist
    indices = event.ind

    # print('event.ind', event.ind)
    # print('__quivers[quiv]', __quivers[quiv])

    name, quiver_id, sequence, costs = __quivers[quiv]
    sequence = np.array(sequence)
    costs = np.array(costs)

    if (indices < len(sequence)).all():
        print('#' * 30)
        print('quiver_id', quiver_id)
        print('name', name)
        print('sequence', sequence[indices])
        print('costs', costs[indices])
        linewidths = np.ones_like(costs)
        linewidths[indices] = 5
        quiv.set_linewidths(linewidths)
        event.canvas.draw()


if __name__ == '__main__':
    main()
