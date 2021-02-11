from .. problem import Problem
from .. datastructures.stack import Stack


# please ignore this
def get_solver_mapping():
    return dict(ids=IDS)


class DLDFS(object):
    def __init__(self, max_depth):
        self.max_depth = max_depth

    # TODO, Exercise 1: implement Depth-Limited Depth First Search (DLDFS)
    # - you can:
    #     - either use the 'Stack()' datastructure for a frontier
    #     - or implement DFS recursively
    #     - if you implement DFS recursively, you use the call stack
    #       as a datastructure for an implicit frontier
    # - DO NOT USE A 'VISITED' SET!
    def solve(self, problem: Problem):
        return None


class IDS(object):
    # TODO, Exercise 1: implement Iterative Deepening Search (IDS)
    # - use the DLDFS implementation from above
    # - start the iterated search at depth == 0
    # - DO NOT USE A 'VISITED' SET
    def solve(self, problem: Problem):
        return None
