const express = require("express")
const cors = require('cors');
const session = require("./session")
const adb = require("./adbCommands")
const test = require("./testModule")
//const fs = require('fs')
//const https = require('https')

//var options = {
//    key: fs.readFileSync('../key.pem'),
//    cert: fs.readFileSync('../cert.pem')
//};


const app = express()

app.use(cors())

//https.createServer(app).listen(3001);
//https.createServer(options, app).listen(3002);


//const server = https.createServer({key: key, cert: cert }, app)


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
/*process.argv.forEach(function (val, index, array) {
  if (index === 2 && val === '-t'){
    myPort = val;
    console.log("ae");
  }
  if (index === 3) {
    targetDeviceIP = val;
    console.log("Target device ip set to: " + targetDeviceIP);
  }
  if (index === 4) {
    myIpAddress = val;
    console.log("As informed by the user, my ip address is: " + myIpAddress);
  }
});    
}*/




