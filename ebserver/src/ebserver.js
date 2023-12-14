const express = require("express")
const cors = require('cors');
const session = require("./session")
const adb = require("./adbCommands")
const ui = require("./UIModule")

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
    console.log(` + fetch ${device} ${test_type}, ${execution}, ${execution_number}`);
    clearTimerObj(
        device
    )
    timerObj[device] = setTimeout(() => {
        console.log(`timeout ${device}-${execution}`);
        done(
            ip, 
            device, 
            test_type, 
            application_id, 
            activity
        )
    }, timeout_seconds * 1000);
    return execution
}

async function logdata(ip, device, test_type, framework, application_id) {
    clearTimerObj(device)
    const [execution, execution_number] = session.getCurrentExecution(
        device,
        test_type
    )
    console.log(` | logdata ${device} ${test_type}, ${execution}, ${execution_number}`)
    await adb.outputBatteryStatsTo(
        ip,
        framework,
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
    console.log(` - done ${device} ${test_type}, ${execution}, ${execution_number}`)
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

app.get("/fetch", async (req, res) => {
    const ip = req.ip.substring(req.ip.lastIndexOf(":") + 1)
    const execution = await what_now(
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
    if(process.argv.length <= 2) console.log("Ebserver listening on port " + 3000 + "!");
});

readInput();

async function readInput() {
    ip = await adb.connectWifi();
    if(ip == "error"){
        return;
    }
    if(process.argv[2] == "-ui"){
        console.log(">> Running UI Mode");
        ui.run(process.argv);
    }
    else{
        console.log(">> Running Individual Executions Mode");
    }
    
}

