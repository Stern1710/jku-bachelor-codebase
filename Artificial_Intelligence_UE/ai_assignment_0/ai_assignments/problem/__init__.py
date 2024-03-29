class Problem(object):
    def get_number_of_expanded_nodes():
        raise NotImplementedError()

    def get_start_node(self):
        raise NotImplementedError()

    def get_end_node(self):
        raise NotImplementedError()

    def is_end(self, node):
        raise NotImplementedError()

    def action_cost(self, node, action):
        raise NotImplementedError()

    def successors(self, node):
        raise NotImplementedError()

    def to_json(self):
        raise NotImplementedError()

    @staticmethod
    def from_json(self, jsonstring):
        raise NotImplementedError()


class Node(object):
    def __init__(self, parent, state, action, cost, depth):
        self.parent = parent
        self.state = state
        self.action = action
        self.cost = cost
        self.depth = depth

    def key(self):
        # if state is composed of other stuff (dict, set, ...)
        # make it a tuple containing hashable datatypes
        # (this is supposed to be overridden by subclasses)
        return self.state

    def __hash__(self):
        return hash(self.key())

    def __eq__(self, other):
        if type(self) == type(other):
            return self.key() == self.key()
        raise ValueError('cannot simply compare two different node types')


def get_action_sequence(end: Node):
    if end is None:
        return list()

    current = end
    reverse_sequence = []
    while current.parent is not None:
        reverse_sequence.append(current.action)
        current = current.parent
    return list(reversed(reverse_sequence))
