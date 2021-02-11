from . instance_generation import maze_generator
from . instance_generation import terrain_generator
from . instance_generation import room_generator
from . instance_generation import instance_loader
import importlib


__GENERATOR_MAPPING = dict(
    maze=maze_generator,
    terrain=terrain_generator,
    rooms=room_generator
)

__SOLVER_MAPPING = dict()

potential_solver_modules = ['rs', 'bfs', 'ucs', 'gbfs', 'dfs', 'ids', 'astar']

for module_name in potential_solver_modules:
    solver = None

    try:
        # try to import reference_implementations first
        solver = importlib.import_module('ai_assignments.reference_implementations.' + module_name)
        print('USING REFERENCE IMPLEMENTATION OF ({})'.format(module_name))
    except Exception as re:
        try:
            # if the reference_implementations do not exist, try to import
            # the implementations that need to be done during an assignment
            solver = importlib.import_module('ai_assignments.search.' + module_name)
        except Exception as se:
            pass

    if solver is not None:
        # try to get the solver mapping
        try:
            mapping = solver.get_solver_mapping()
            __SOLVER_MAPPING.update(mapping)
        except Exception as me:
            print('Unable to import solver for ({})'.format(module_name))


def get_problem_generators():
    return list(__GENERATOR_MAPPING.keys())


def get_solution_methods():
    return list(__SOLVER_MAPPING.keys())


def get_solution_method(name):
    return __SOLVER_MAPPING[name]


def get_problem_generator(name):
    return __GENERATOR_MAPPING[name]


def load_problem_instance(path):
    with open(path, 'r') as fh:
        return instance_loader.from_json(fh.read())
