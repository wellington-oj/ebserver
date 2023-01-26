const fs = require("fs");
const util = require('util');
const exec = util.promisify(require("child_process").exec);

async function runCommand(command, targetDevice, dir, fileName){
    try {
       await exec(`adb ${getTarget(targetDevice)} shell dumpsys ${command.name} ${command.params}  >  ${dir}/${command.name}-${fileName}`)
    } catch (error) {
        console.log(`ERROR EXECUTING ` +command.name + ` ON ${targetDevice}`)
    }	
}

function getTarget(targetDevice = ""){
    if(targetDevice  === ""){
        return `-s ${targetDevice}`
    }
    return " "
}

async function cleanBatteryStatus(targetDevice) {
    try {
        await exec("adb" + getTarget(targetDevice) + "shell dumpsys procstats --clear")
        await exec("adb" + getTarget(targetDevice) + "shell dumpsys batterystats --reset")
    } catch (error) {
        console.log(`ERROR CLEANING STATUS ${targetDevice}`)
    }
}

async function outputBatteryStatsTo(targetDevice, framework, currentTest, counter, packageName) {
    const fileName = `${counter}.txt`
    const device = await exec("adb" + getTarget(targetDevice) + "shell getprop ro.product.model")
    createDirIfNotExists(fs, 'experiment-results')
    createDirIfNotExists(fs, 'experiment-results/' + framework)
    createDirIfNotExists(fs, 'experiment-results/' + framework + '/' + device.stdout.trim())
    const dir = 'experiment-results/' + framework + '/' + '/' + device.stdout.trim() + '/' + currentTest
    createDirIfNotExists(fs, dir)
    
    const meminfo = {name: "meminfo", params: ` ${packageName}.test -d`}
    const batterystats = {name: "batterystats", params: ""}
    const procstats = {name: "procstats", params: ` --hours 1`}
    
    await runCommand(meminfo, targetDevice, dir, fileName)
    await runCommand(batterystats, targetDevice, dir, fileName)
    await runCommand(procstats, targetDevice, dir, fileName)

}

async function outputBatteryStatsTest(framework, currentTest, counter, packageName) {
    outputBatteryStatsTo("",framework,currentTest,counter,packageName);
}

async function startApp(targetDevice, applicationId, mainActivity) {
    try {
        await exec("adb" + getTarget(targetDevice) + "shell am start -n " + applicationId + "/" + mainActivity)
    } catch (error) {
        console.log(`ERROR STARTING APP ${targetDevice}`)
    }
}

async function startUITest(className, methodName, packageName) {
    try {
       await exec(`adb shell am instrument -w -e debug false -e class ${packageName}.${className}#${methodName} ${packageName}.test/androidx.test.runner.AndroidJUnitRunner`)
    } catch (error) {
        console.log(`ERROR STARTING APP`)
    }
}

async function killApp(targetDevice, applicationId) {
    try {
        await exec("adb" + getTarget(targetDevice) + "shell pm clear " + applicationId)
    } catch (error) {
        console.log(`ERROR KILLING APP ${targetDevice} ${applicationId}`)
    }
}

async function done(targetDevice, applicationId, mainActivity) {
    await killApp(targetDevice, applicationId)
    await startApp(targetDevice, applicationId, mainActivity)
}


function createDirIfNotExists(fs, dir) {
    if (!fs.existsSync(dir)) {
        fs.mkdirSync(dir)   
    }
}

module.exports = {
    cleanBatteryStatus, outputBatteryStatsTo, outputBatteryStatsTest, done, startUITest
}
