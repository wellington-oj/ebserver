const fs = require("fs");
const util = require('util');
const exec = util.promisify(require("child_process").exec);

async function cleanBatteryStatus(targetDevice) {
    try {
        if(targetDevice === ""){
            await exec(`adb shell dumpsys procstats --clear`)
            await exec(`adb shell dumpsys batterystats --reset`)
        }
        else{
            await exec(`adb -s ${targetDevice} shell dumpsys procstats --clear`)
            await exec(`adb -s ${targetDevice} shell dumpsys batterystats --reset`)
        }
    } catch (error) {
        console.log(`ERROR CLEANING STATUS ${targetDevice}`)
    }
}

async function outputBatteryStatsTo(targetDevice, framework, currentTest, counter, packageName) {
    const fileName = `${counter}.txt`
    const device = await exec('adb shell getprop ro.product.model');
    createDirIfNotExists(fs, 'experiment-results')
    createDirIfNotExists(fs, 'experiment-results/' + framework)
    createDirIfNotExists(fs, 'experiment-results/' + framework + '/' + device.stdout.trim())
    const dir = 'experiment-results/' + framework + '/' + '/' + device.stdout.trim() + '/' + currentTest
    createDirIfNotExists(fs, dir)
    await outputData(targetDevice, dir, fileName)
    await outputEnergy(targetDevice, dir, fileName)
    await outputMemInfo(targetDevice, packageName, dir, fileName)
}

async function outputBatteryStatsTest(framework, currentTest, counter, packageName) {
    outputBatteryStatsTo("",framework,currentTest,counter,packageName);
}

async function outputMemInfo(targetDevice="", packageName="", dir, fileName) {
    try {
        if(targetDevice === ""){
            await exec(`adb shell dumpsys meminfo ${packageName}.test -d > "${dir + "/memory-" + fileName}"`)
        }else{
            const pid = await exec(`adb -s ${targetDevice} shell pidof ${packageName}`)
            await exec(`adb -s ${targetDevice} shell dumpsys meminfo ${pid.stdout.trim()} -d > "${dir + "/memory-" + fileName}"`)
        }
        
    } catch (error) {
        console.log(`ERROR OUTPUTTING MEMORY DATA ${targetDevice}`)
    }
}

async function outputEnergy(targetDevice="", dir, fileName) {
    try {
        if(targetDevice === ""){
            await exec(`adb shell dumpsys batterystats > "${dir + "/energy-" + fileName}"`)
        }else{
            await exec(`adb -s ${targetDevice} shell dumpsys batterystats > "${dir + "/energy-" + fileName}"`)
        }
    } catch (error) {
        console.log(`ERROR OUTPUTTING ENERGY DATA ${targetDevice}`)
    }
}

async function outputData(targetDevice="", dir, fileName){
    try { 
        if(targetDevice === ""){
            await exec(`adb shell dumpsys procstats --hours 1 > "${dir + "/data-" + fileName}"`)
        }
        else{
            await exec(`adb -s ${targetDevice} shell dumpsys procstats --hours 1 > "${dir + "/data-" + fileName}"`)
        }
    } catch (error) {
        console.log(`ERROR OUTPUTTING DATA ${targetDevice}`)
    }
}

async function startApp(targetDevice, applicationId, mainActivity) {
    try {
        await exec(`adb -s ${targetDevice} shell am start -n ${applicationId}/${mainActivity}`)
    } catch (error) {
        console.log(`ERROR STARTING APP ${targetDevice}`)
    }
}

async function startTest(className, methodName, packageName) {
    try {
       await exec(`adb shell am instrument -w -e debug false -e class ${packageName}.${className}#${methodName} ${packageName}.test/androidx.test.runner.AndroidJUnitRunner`)
    } catch (error) {
        console.log(`ERROR STARTING APP here?`)
    }
}

async function killApp(targetDevice, applicationId) {
    try {
        if(targetDevice === ""){
            await exec(`adb shell pm clear ${applicationId}`)
        }
        else{
            await exec(`adb -s ${targetDevice} shell pm clear ${applicationId}`)
        }
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
    cleanBatteryStatus, outputBatteryStatsTo, outputBatteryStatsTest, done, startTest
}
