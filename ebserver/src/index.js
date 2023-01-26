const express = require("express")
const cors = require('cors');
const session = require("./session")
const adb = require("./adbCommands")
const test = require("./testModule")

const app = express()
app.use(cors())

const timeout_seconds = 120
let timerObj = {}
function clearTimerObj(device){
    if(timerObj[device]){
        clearTimeout(timerObj[device])
        delete timerObj[device]
    }
}

async function what_now(ip, device, test_type, application_id, activity, timerObj) {
    await adb.cleanBatteryStatus(ip)
    const [execution, execution_number] = session.getCurrentExecution(
        device,
        test_type
    )
    console.log(` --- what_now ${device} ${test_type}, ${execution}, ${execution_number}`);
    res.send(execution)
    clearTimerObj(
        device
    )
    timerObj[device] = setTimeout(() => {
        done(
            ip, 
            device, 
            test_type, 
            application_id, 
            activity
        )
    }, timeout_seconds * 1000);
}

async function logdata(ip, device, test_type, framework, application_id) {
    clearTimerObj(device)
    const [execution, execution_number] = session.getCurrentExecution(
        device,
        test_type
    )
    console.log(`logdata ${req.headers.device} ${req.headers.test_type}, ${execution}, ${execution_number}`)
    await adb.outputBatteryStatsTo(
        ip,
        framework,
        device,
        execution,
        execution_number,
        application_id
    )
}

async function done(ip, device, test_type, application_id, activity) {
    const [execution, execution_number] = session.getCurrentExecution(
        device,
        test_type
    )
    console.log(`done ${device} ${test_type}, ${execution}, ${execution_number}`)
    session.updateExecution(
        device, 
        test_type
    )
    await adb.done(
        ip,
        application_id,
        activity
    )
}

app.get("/", (req, res) => {
    res.send("index")
})

app.get("/what_now", async (req, res) => {
    const ip = req.ip.substring(req.ip.lastIndexOf(":") + 1)
    await what_now(
        ip, 
        req.headers.device, 
        req.headers.test_type, 
        req.headers.application_id, 
        req.headers.activity,
        timerObj
    )
    res.send(execution)
})

app.get("/logdata", async (req, res) => {
    const ip = req.ip.substring(req.ip.lastIndexOf(":") + 1)
    await logdata(
        ip,
        req.headers.device,
        req.headers.test_type,
        req.headers.framework,
        req.headers.application_id
    )    
    res.send("done")
})

app.get("/done", async (req, res) => {
    const ip = req.ip.substring(req.ip.lastIndexOf(":") + 1)
    done(
        ip, 
        req.headers.device, 
        req.headers.test_type, 
        req.headers.application_id, 
        req.headers.activity
    )
    res.send("")
})

app.listen(3000, () => {
    if(process.argv.length <= 2) console.log("Dashboard app listening on port " + 3000 + "!");
});

readInput();

async function readInput() {
    if(process.argv.length > 2){
        test.runTests(process.argv);
    }else{
        console.log(">> Running Individual Executions Mode")
    }
}

