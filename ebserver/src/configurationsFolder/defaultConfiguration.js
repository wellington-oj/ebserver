const exampleConfig = {
    benchs: ["foo","boo"],
    parameters: {
        "foo": [10,15],
        "boo": [10]
    }
}

module.exports = {
    "key": "default",
    "experiments": {
        "example": exampleConfig
    },
    "start_execution": 1,
    "end_execution": 45
}
