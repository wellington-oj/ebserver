const configurations = require("./configurations")

let sessions = {}

function addSession(device, currentTest, testNumber, config, test_type) {
    sessions[device] = {
        "current_algoritm": currentTest,
        "test_type": test_type,
        "current_execution": testNumber,
        "configuration": config
    }
}

function getCurrentSession(device, test_type) {
    try {
        if (sessions[device]["test_type"] !== test_type) {
            throw "needs to create another session"
        }
    } catch (error) {
        const config = configurations.getConfiguration(device)
        addSession(
            device,
            config.experiments[test_type][0],
            config.start_execution,
            config,
            test_type
        )
    }
    return sessions[device]
}

function getCurrentExecution(device, test_type) {
    const session = getCurrentSession(device, test_type)
    return [session.current_algoritm, session.current_execution]
}

function updateExecution(device, test_type, skip_experiment) {
    const session = sessions[device]
    const execution_list = session.configuration.experiments[test_type]
    const algoritm_index = execution_list.indexOf(session.current_algoritm)
    if (skip_experiment) {
        session.current_execution = 1
        return updateAlgorithm(session, execution_list, algoritm_index)
    }
    if(session.current_execution < session.configuration.end_execution) {
        session.current_execution += 1
    } else {
        session.current_execution = 1
        if(algoritm_index < (execution_list.length - 1)) {
            session.current_algoritm = execution_list[algoritm_index + 1]
        } else {
            return false
        }
    }
    return true
}

function updateAlgorithm(session, execution_list, algoritm_index){
    const algorithm = session.current_algoritm.split("-")[0]
    for(let index = algoritm_index; index < execution_list.length; index++) {
        const element = execution_list[index].split("-")[0]
        if (element !== algorithm){
            session.current_algoritm = execution_list[index]
            return true
        }
    }
    return false
}

module.exports = {
    getCurrentExecution, updateExecution
}
