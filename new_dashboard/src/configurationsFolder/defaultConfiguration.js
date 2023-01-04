const benchGameAllConfig = {
    benchs: ["mandelbrot", "fannkuch", "knucleotide", "nbody", "pidigits", "regex", "revcomp", "spectral", "fasta", "binarytree"],
    parameters: {
        "mandelbrot": [16000],
        "binarytree": [21, 22, 23],
        "fannkuch": [11, 12],
        "fasta": [17500000, 35000000, 52500000, 78750000, 120000000, 180000000, 270000000],
        "knucleotide": [2500000, 5000000, 25000000, 40000000],
        "nbody": [22500000, 45000000, 80000000, 120000000, 180000000, 270000000, 405000000],
        "pidigits": [6500, 13000, 19500, 29250],
        "regex": [2500000, 5000000, 10000000, 25000000, 40000000],
        "revcomp": [5000000, 10000000, 25000000, 40000000],
        "spectral": [6500, 13000, 19500, 29250, 45000]
    }
}

const imagesRotationConfig = {
    benchs: ["rotation"],
    parameters: {
        "rotation": ["28-4", "112-8", "252-12", "448-16"]
    }
}

const espressoConfig = {
    benchs: ["espresso"],
    parameters: {
        "espresso": ["test1", "test2", "test3", "test4"]
    }
}

module.exports = {
    "key": "default",
    "benchmarks": {
        "benchmarkGame": benchGameAllConfig,
        "espressoTest": espressoConfig,
        "imageRotation": imagesRotationConfig
    },
    "start_execution": 1,
    "end_execution": 45
}
