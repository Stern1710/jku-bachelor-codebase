import numpy as np
from . import enc


# def conv2d(a, f):
#     s = f.shape + tuple(np.subtract(a.shape, f.shape) + 1)
#     strd = np.lib.stride_tricks.as_strided
#     subM = strd(a, shape=s, strides=a.strides * 2)
#     return np.einsum('ij,ijkl->kl', f, subM)


# def gkern(k=5, sig=1.):
#     ax = np.linspace(-(k - 1) / 2., (k - 1) / 2., k)
#     xx, yy = np.meshgrid(ax, ax)
#     kernel = np.exp(-0.5 * (np.square(xx) + np.square(yy)) / np.square(sig))
#     return kernel / np.sum(kernel)


# this is code taken from
# https://github.com/dandrino/terrain-erosion-3-ways/blob/master/util.py
# Copyright (c) 2018 Daniel Andrino
# (project is MIT licensed)
def fbm(shape, p, lower=-np.inf, upper=np.inf):
    freqs = tuple(np.fft.fftfreq(n, d=1.0 / n) for n in shape)
    freq_radial = np.hypot(*np.meshgrid(*freqs))
    envelope = (np.power(freq_radial, p, where=freq_radial != 0) *
                (freq_radial > lower) * (freq_radial < upper))
    envelope[0][0] = 0.0
    phase_noise = np.exp(2j * np.pi * np.random.rand(*shape))
    return np.real(np.fft.ifft2(np.fft.fft2(phase_noise) * envelope))


class Level(object):
    def __init__(self, rng, size):
        self.rng = rng
        self.size = size
        self.field = np.full((self.size, self.size), enc.SPACE, dtype=np.int8)

        # k = 31
        # cs = self.size + 2 * (k // 2)
        # random_costs = self.rng.uniform(1e-5, 1, (cs, cs))
        # kernel = gkern(k, 5.)
        # self.costs = conv2d(random_costs, kernel)

        self.costs = fbm(self.field.shape, -2)
        self.costs -= self.costs.min()
        self.costs /= self.costs.max()
        self.costs *= 9
        self.costs += 1
        self.costs = self.costs.astype(np.int)

        self.start = (0, 0)
        self.end = (size - 1, size - 1)

        x = 0
        y = size - 1
        for i in range(0, size):
            self.field[x, y] = enc.WALL
            x += 1
            y -= 1

        self.replace_one_or_more_walls()

    def replace_one_or_more_walls(self):
        # select only coordinates of walls
        walls = np.where(self.field == enc.WALL)
        n_walls = len(walls[0])
        n_replace = self.rng.randint(1, max(2, n_walls // 5))
        to_replace = self.rng.randint(0, n_walls, n_replace)

        # ... with space, but very *costly* space (it's trap!)
        for ri in to_replace:
            x, y = walls[0][ri], walls[1][ri]
            self.field[x, y] = enc.SPACE
