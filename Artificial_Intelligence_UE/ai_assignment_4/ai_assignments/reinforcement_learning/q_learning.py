from ..environment import Environment, Outcome
import numpy as np


def eps_greedy(rng, qs, epsilon):
    # this function makes an epsilon greedy decision
    if rng.uniform(0, 1) < epsilon:
        return rng.choice(list(qs))
    else:
        return max(qs, key=qs.get)


class QLearning():
    def train(self, env: Environment):
        ########################################
        # please leave untouched
        rng = np.random.RandomState(1234)
        alpha = 0.2
        epsilon = 0.3
        gamma = env.get_gamma()
        n_episodes = 10000
        ########################################

        ########################################
        # initialize the 'table'
        Q = dict()
        for s in range(env.get_n_states()):
            Q[s] = dict()
            for a in range(env.get_n_actions()):
                Q[s][a] = 0.
        ########################################

        for episode in range(1, n_episodes + 1):

            cur_state = env.reset()  # Reset the field
            done = False

            while not done:
                action = eps_greedy(rng, Q[cur_state], epsilon)  # Get decision
                next_state, reward, done = env.step(action)  # Perform a step

                max_val = max(Q[next_state].values())  # get maximum value of next actions
                Q[cur_state][action] += alpha * (reward + gamma * max_val - Q[cur_state][action])  # Update action value

                cur_state = next_state  # Update used state for greedy_eps

        ########################################

        ########################################
        # this computes a deterministic policy
        # from the Q value function
        # along the way, we compute V, the
        # state value function as well
        policy = dict()
        V = dict()
        for s, qs in Q.items():
            policy[s] = dict()
            V[s] = 0.
            best_a = None
            best_q = float('-Inf')
            for a, q in qs.items():
                if q > best_q:
                    best_q = q
                    best_a = a

            # how good is it to be in state 's'?
            # if we take the best action, we can expect to get 'best_q'
            # future reward. hence, being in state V[s] we can expect
            # the same amount of reward ...
            V[s] = best_q
            for a in qs.keys():
                if a == best_a:
                    policy[s][a] = 1.
                else:
                    policy[s][a] = 0.
        ########################################

        return Outcome(n_episodes, policy, V=V, Q=Q)
