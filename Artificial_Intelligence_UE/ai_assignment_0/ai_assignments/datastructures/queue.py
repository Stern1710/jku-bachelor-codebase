from collections import deque


class Queue(object):
    def __init__(self):
        self.d = deque()

    def put(self, v):
        self.d.append(v)

    def get(self):
        return self.d.popleft()

    def has_elements(self):
        return len(self.d) > 0
