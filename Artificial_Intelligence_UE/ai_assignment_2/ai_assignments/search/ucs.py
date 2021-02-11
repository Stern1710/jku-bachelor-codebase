from ..problem import Problem
from ..datastructures.priority_queue import PriorityQueue


def get_solver_mapping():
    return dict(ucs=UCS)


class UCS(object):
    def solve(self, problem: Problem):
        fringe = PriorityQueue()  # Setup fringe and visited set
        visited = set()

        first_node = problem.get_start_node()  # Get the first node
        fringe.put(0, first_node)  # Add with cost 0
        visited.add(first_node)

        while fringe.has_elements():  # Iterate over all expanded nodes
            curElem = fringe.get()  # Get first element

            if problem.is_end(curElem):  # Return current element if it is the goal
                return curElem

            successors = problem.successors(curElem)  # Get all successors
            for node in successors:
                if node not in visited:  # Only add to fringe if not already visited
                    fringe.put(curElem.cost + node.cost, node)  # Add the calculated cost as the priority
                    visited.add(node)

        return None  # Return nothing as no way was found
