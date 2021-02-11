import math
from ..problem import Problem
from ..datastructures.priority_queue import PriorityQueue


# please ignore this
def get_solver_mapping():
    return dict(
        gbfs_ec=GBFS_Euclidean,
        gbfs_mh=GBFS_Manhattan
    )


class GBFS(object):
    def solve(self, problem: Problem):
        fringe = PriorityQueue()  # Setup fringe and visited set
        visited = set()

        goal = problem.get_end_node()  # Get my goal node

        first_node = problem.get_start_node()  # Get the first node to start with
        fringe.put(self.heuristic(first_node, goal), first_node)  # Add first with heuristics into queue
        visited.add(first_node)

        while fringe.has_elements():  # Iterate over all elements in the queue at any given time
            curElem = fringe.get()

            if problem.is_end(curElem):  # If we found the solution, return the correct node
                return curElem

            successors = problem.successors(curElem)  # Get all succesors
            for node in successors:
                if node not in visited:  # If node was not already visited, add to the fringe
                    fringe.put(self.heuristic(node, goal), node)
                    visited.add(node)

        return None  # Return nothing as no way was found


# this is the GBFS variant with the euclidean distance as a heuristic
# it is registered as a solver with the name 'gbfs_ec'

# please note that in an ideal world, this heuristic should actually be part
# of the problem definition, as it assumes domain knowledge about the structure
# of the problem, and defines a distance to the goal state
class GBFS_Euclidean(GBFS):
    def heuristic(self, current, goal):
        cy, cx = current.state
        gy, gx = goal.state
        return math.sqrt((cy - gy) ** 2 + (cx - gx) ** 2)


# this is the GBFS variant with the manhattan distance as a heuristic
# it is registered as a solver with the name 'gbfs_mh'

# please note that in an ideal world, this heuristic should actually be part
# of the problem definition, as it assumes domain knowledge about the structure
# of the problem, and defines a distance to the goal state
class GBFS_Manhattan(GBFS):
    def heuristic(self, current, goal):
        cy, cx = current.state
        gy, gx = goal.state
        return math.fabs((cy - gy)) + math.fabs(cx - gx)
