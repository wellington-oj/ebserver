const benchGameAllConfig = {
    benchs: ["fasta", "binarytree"],
    parameters: {
        "binarytree": [21],
        "fannkuch": [11],
        "fasta": [35000000],
        "knucleotide": [2500000],
        "nbody": [22500000],
        "pidigits": [13000],
        "regex": [2500000],
        "revcomp": [5000000, 10000000, 25000000, 40000000],
        "spectral": [19500]
    }
}

const imagesRotationConfig = {
    benchs: ["rotation"],
    parameters: {
        "rotation": ["28-4", "112-8", "252-12", "448-16"]
    }
}

module.exports = {
    "key": "Redmi Note 7 Pro",
    "benchmarks": {
        "benchmarkGame": benchGameAllConfig,
        "imageRotation": imagesRotationConfig
    },
    "start_execution": 1,
    "end_execution": 45
}