from .. problem import Problem
from .. datastructures.queue import Queue

# please ignore this
def get_solver_mapping():
    return dict(bfs=BFS)

class BFS(object):
    def solve(self, problem: Problem):
        fringe = Queue() # init my queue forr nodes to visit
        visited = set() # init my set for already visited nodes
        
        first_node = problem.get_start_node() # get first node
        fringe.put(first_node) #starting element into the queue
        visited.add(first_node) #put first node in the visited set

        while Queue.has_elements(fringe): #iterate over elements in queue
            cur = fringe.get() # get current element
            
            if problem.is_end(cur): #if we found the end, return the current node
                return cur
            
            #get all successor nodes and add them to get queue
            successors = problem.successors(cur)
            for node in successors:
                if node not in visited: #only add if not already visited
                    fringe.put(node)
                    visited.add(node) #add to visited to prevent re-adding at a later stage in the queue

        return None # Return nothing as nothing was found :(
