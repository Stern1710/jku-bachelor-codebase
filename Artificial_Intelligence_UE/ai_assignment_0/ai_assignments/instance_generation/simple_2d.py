from .. problem import Problem, Node
from . import enc
import json
import numpy as np
from collections import OrderedDict


class Simple2DProblem(Problem):
    """
    the states are the positions on the board that the agent can walk on
    """

    ACTIONS_DELTA = OrderedDict([
        ('R', (0, +1)),
        ('U', (-1, 0)),
        ('L', (0, -1)),
        ('D', (+1, 0)),
    ])

    def __init__(self, board, costs, start, end):
        self.board = board
        self.costs = costs
        self.start_state = start
        self.end_state = end
        self.n_expands = 0

    def get_start_node(self):
        return Node(None, self.start_state, None, 0, 0)

    def get_end_node(self):
        return Node(None, self.end_state, None, 0, 0)

    def is_end(self, node):
        return node.state == self.end_state

    def action_cost(self, state, action):
        # for the MazeProblem, the cost of any action
        # is stored at the coordinates of the successor state,
        # and represents the cost of 'stepping onto' this
        # position on the board
        sy, sx = self.__delta_state(state, action)
        return self.costs[sy][sx]

    def successor(self, node, action):
        # determine the next state
        successor_state = self.__delta_state(node.state, action)
        if successor_state is None:
            return None

        # determine what it would cost to take this action in this state
        cost = self.action_cost(node.state, action)

        # add the next state to the list of successor nodes
        return Node(
            node,
            successor_state,
            action,
            node.cost + cost,
            node.depth + 1
        )

    def get_number_of_expanded_nodes(self):
        return self.n_expands

    def successors(self, node):
        self.n_expands += 1
        successor_nodes = []
        for action in self.ACTIONS_DELTA.keys():
            succ = self.successor(node, action)
            if succ is not None:
                successor_nodes.append(succ)
        return successor_nodes

    def to_json(self):
        return json.dumps(dict(
            type=self.__class__.__name__,
            board=self.board.tolist(),
            costs=self.costs.tolist(),
            start_state=self.start_state,
            end_state=self.end_state
        ))

    @staticmethod
    def from_json(jsonstring):
        data = json.loads(jsonstring)
        return Simple2DProblem(
            np.array(data['board']),
            np.array(data['costs']),
            tuple(data['start_state']),
            tuple(data['end_state'])
        )

    def __delta_state(self, state, action):
        # the old state's coordinates
        y, x = state

        # the deltas for each coordinates
        dy, dx = self.ACTIONS_DELTA[action]

        # compute the coordinates of the next state
        sy = y + dy
        sx = x + dx

        if self.__on_board(sy, sx) and self.__walkable(sy, sx):
            # (sy, sx) is a *valid* state if it is on the board
            # and there is no wall where we want to go
            return sy, sx
        else:
            return None

    def __on_board(self, y, x):
        size = len(self.board)  # all boards are quadratic
        return y >= 0 and y < size and x >= 0 and x < size

    def __walkable(self, y, x):
        return self.board[y][x] != enc.WALL
