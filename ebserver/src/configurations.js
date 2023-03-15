const fs = require('fs');

const configurations = {}

function plainConfigs(config) {
    const plainConfig = {}
    for (key of config["program-types"]) {
        let value = config[key]
        let plainList = []
        for (let index = 0; index < value.experiments.length; index++) {
            const bench = value.experiments[index];
            const bench_parameters = value.parameters[bench]
            for (let index = 0; index < bench_parameters.length; index++) {
                const parameter = bench_parameters[index];
                plainList.push(bench + "-" + parameter)
            }
        }
        plainConfig[key] = plainList
    }
    return plainConfig
}

function loadConfigs(){
    const configurations = fs.readdirSync("src/configurationsFolder")
    configurations.forEach((configuration) => {
        const raw = fs.readFileSync("src/configurationsFolder/" + configuration)
        const config = JSON.parse(raw)
        addConfig(config, configuration.split(".json")[0])
    })
}

function addConfig(config, fileName){
    configurations[fileName] = {
        "experiments": plainConfigs(config),
        "start_execution": config.start_execution,
        "end_execution": config.end_execution
    }
}

function getConfiguration(device) {
    if (device in configurations) {
        return configurations[device]
    } else {
        return configurations["default"]
    }
}

loadConfigs()

module.exports = { getConfiguration }
