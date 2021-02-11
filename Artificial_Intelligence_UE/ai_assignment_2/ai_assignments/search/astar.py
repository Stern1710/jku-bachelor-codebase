import math
from ..problem import Problem
from ..datastructures.priority_queue import PriorityQueue


# please ignore this
def get_solver_mapping():
    return dict(
        astar_ec=ASTAR_Euclidean,
        astar_mh=ASTAR_Manhattan
    )


class ASTAR(object):
    def solve(self, problem: Problem):
        fringe = PriorityQueue()  # Setup fringe and visited set
        visited = set()

        goal = problem.get_end_node()  # Get the end node / goal node

        first_node = problem.get_start_node()  # Get the first node
        fringe.put(self.heuristic(first_node, goal), first_node)  # Add it to the fringe will the according heuristics
        while fringe.has_elements():  # Iterate over all elements in the fringe
            cur_elem = fringe.get()  # Get current element

            if problem.is_end(cur_elem):  # If the current element is our searched element, return that element
                return cur_elem

            if cur_elem not in visited:  # Only if we haven't visited that element before, go on
                visited.add(cur_elem)  # Mark as visited now (bc we haven't been there before)

                successors = problem.successors(cur_elem)  # Get all successors
                for node in successors:  # Iteratate over all successors and add them to the fringe
                    fringe.put(node.cost + self.heuristic(node, goal), node)

        return None  # Return None if no path to the goal was found


# this is the ASTAR variant with the euclidean distance as a heuristic
# it is registered as a solver with the name 'gbfs_ec'

# please note that in an ideal world, this heuristic should actually be part
# of the problem definition, as it assumes domain knowledge about the structure
# of the problem, and defines a distance to the goal state
class ASTAR_Euclidean(ASTAR):
    def heuristic(self, current, goal):
        cy, cx = current.state
        gy, gx = goal.state
        return math.sqrt((cy - gy) ** 2 + (cx - gx) ** 2)


# this is the ASTAR variant with the manhattan distance as a heuristic
# it is registered as a solver with the name 'gbfs_mh'

# please note that in an ideal world, this heuristic should actually be part
# of the problem definition, as it assumes domain knowledge about the structure
# of the problem, and defines a distance to the goal state
class ASTAR_Manhattan(ASTAR):
    def heuristic(self, current, goal):
        cy, cx = current.state
        gy, gx = goal.state
        return math.fabs((cy - gy)) + math.fabs(cx - gx)
