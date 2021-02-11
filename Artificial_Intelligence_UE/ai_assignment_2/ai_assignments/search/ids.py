from ..problem import Problem
from ..datastructures.stack import Stack


# please ignore this
def get_solver_mapping():
    return dict(ids=IDS)


class DLDFS(object):
    def __init__(self, max_depth):
        self.max_depth = max_depth

    def solve(self, problem: Problem):
        fringe = Stack()  # Init the fringe as a simple stack

        first_node = problem.get_start_node()  # Get the first node and put it in the fringe
        fringe.put(first_node)

        while fringe.has_elements():  # Iterate over all elements in the fringe
            cur_elem = fringe.get()  # Get next element on top of the stack

            if problem.is_end(cur_elem):  # Return cur element if it is the gaol
                return cur_elem

            if cur_elem.depth < self.max_depth:  # Only expand child nodes if the current depth is small enough
                successors = problem.successors(cur_elem)  # Get all succesors
                for node in successors:  # Put all successors in the fringe
                    fringe.put(node)

        return None  # Return nothing as no way was found


class IDS(object):
    def solve(self, problem: Problem):
        depth = 0  # Start with depth zero (basically just traverse the first node)

        while 1:  # Work loop that is exiting via returnin a result
            dldfs = DLDFS(depth)  # Init the DLSDS with the current depth
            result = dldfs.solve(problem)  # Get the result

            if result is not None:  # If the result is not empty (None), we have a solution, return that
                return result

            depth += 1 # Add 1 to depth as not result was found and repeat
