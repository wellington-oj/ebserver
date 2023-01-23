const express = require("express")
const cors = require('cors');
const session = require("./session")
const adb = require("./adbCommands")

const app = express()
app.use(cors())


app.get("/", (req, res) => {
    res.send("index")
})

app.get("/what_now", async (req, res) => {
    const ip = req.ip.substring(req.ip.lastIndexOf(":") + 1)
    await adb.cleanBatteryStatus(ip)
    const [execution, execution_number] = session.getCurrentExecution(
        req.headers.device,
        req.headers.test_type
    )
    console.log("---------------------------------------------------");
    console.log(`what_now ${req.headers.device} ${req.headers.test_type}, ${execution}, ${execution_number}`);
    console.log("---------------------------------------------------");
    res.send(execution)
})

app.get("/logdata", async (req, res) => {
    const ip = req.ip.substring(req.ip.lastIndexOf(":") + 1)
    const [execution, execution_number] = session.getCurrentExecution(
        req.headers.device,
        req.headers.test_type
    )
    console.log(`logdata ${req.headers.device} ${req.headers.test_type}, ${execution}, ${execution_number}`)
    await adb.outputBatteryStatsTo(
        ip,
        req.headers.framework,
        req.headers.device,
        execution,
        execution_number,
        req.headers.application_id
    )
    res.send("done")
})

app.get("/done", async (req, res) => {
    const ip = req.ip.substring(req.ip.lastIndexOf(":") + 1)
    const [execution, execution_number] = session.getCurrentExecution(
        req.headers.device,
        req.headers.test_type
    )
    console.log(`done ${req.headers.device} ${req.headers.test_type}, ${execution}, ${execution_number}`)
    session.updateExecution(
        req.headers.device, 
        req.headers.test_type
    )
    await adb.done(
        ip,
        req.headers.application_id,
        req.headers.activity
    )
    res.send("")
})


app.listen(3000, () => {
    console.log("Dashboard app listening on port " + 3000 + "!");
});


