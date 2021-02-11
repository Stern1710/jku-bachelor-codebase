from . instance_generation import maze_generator
from . instance_generation import terrain_generator
from . instance_generation import room_generator
from . instance_generation import instance_loader
import importlib
import os


__GENERATOR_MAPPING = dict(
    maze=maze_generator,
    terrain=terrain_generator,
    rooms=room_generator
)

__SOLVER_MAPPING = dict()


def try_and_get_solver(module_path):
    solver = None
    try:
        # if the reference_implementations do not exist, try to import
        # the implementations that need to be done during an assignment
        solver = importlib.import_module('ai_assignments.' + module_path)
    except Exception as se:
        print('#' * 40)
        print('Could not load problem solver module ({})!'.format(module_path))
        import traceback
        traceback.print_exc()
        print('#' * 40)

    return solver


def get_solvers(path):
    module_names = []
    module_base = os.path.split(path)[-1]

    if os.path.exists(path):
        for filename in os.listdir(path):
            if filename.endswith('.py'):
                if not filename.startswith('__'):
                    basename = os.path.splitext(filename)[0]
                    module_names.append('.'.join([module_base, basename]))

    mapping = dict()
    for module_name in module_names:
        solver = try_and_get_solver(module_name)

        if solver is not None:
            # try to get the solver mapping
            try:
                mapping.update(solver.get_solver_mapping())
            except Exception as me:
                print('#' * 40)
                print('Unable to import solver for ({})'.format(module_name))
                import traceback
                traceback.print_exc()
                print('#' * 40)
    return mapping


def register_solver_modules():
    path = os.path.split(__file__)[0]

    reference_solver_path = os.path.join(path, 'reference_implementations')
    default_solver_path = os.path.join(path, 'search')

    for path in [reference_solver_path, default_solver_path]:
        solvers = get_solvers(path)
        __SOLVER_MAPPING.update(solvers)


register_solver_modules()


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
