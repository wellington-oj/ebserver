const fs = require("fs");
const util = require('util');
const exec = util.promisify(require("child_process").exec);

async function cleanBatteryStatus(targetDevice) {
    try {
        await exec(`adb -s ${targetDevice} shell dumpsys procstats --clear`)
        await exec(`adb -s ${targetDevice} shell dumpsys batterystats --reset`)
    } catch (error) {
        console.log(`ERROR CLEANING STATUS ${targetDevice}`)
    }
}

async function outputBatteryStatsTo(targetDevice, framework, device, currentTest, counter, device_id) {
    const fileName = `${counter}.txt`
    createDirIfNotExists(fs, 'experiment-results')
    createDirIfNotExists(fs, 'experiment-results/' + framework)
    createDirIfNotExists(fs, 'experiment-results/' + framework + '/' + device)
    const dir = 'experiment-results/' + framework + '/' + '/' + device + '/' + currentTest
    createDirIfNotExists(fs, dir)
    await outputData(targetDevice, dir, fileName)
    await outputEnergy(targetDevice, dir, fileName)
    await outputMemInfo(targetDevice, device_id, dir, fileName)
}

async function outputMemInfo(targetDevice, device_id, dir, fileName) {
    try {
        const pid = await exec(`adb -s ${targetDevice} shell pidof ${device_id}`)
        await exec(`adb -s ${targetDevice} shell dumpsys meminfo ${pid.stdout.trim()} -d > "${dir + "/memory-" + fileName}"`)
    } catch (error) {
        console.log(`ERROR OUTPUTTING MEMORY DATA ${targetDevice}`)
    }
}

async function outputEnergy(targetDevice, dir, fileName) {
    try {
        await exec(`adb -s ${targetDevice} shell dumpsys batterystats > "${dir + "/energy-" + fileName}"`)
    } catch (error) {
        console.log(`ERROR OUTPUTTING ENERGY DATA ${targetDevice}`)
    }
}

async function outputData(targetDevice, dir, fileName){
    try {
        await exec(`adb -s ${targetDevice} shell dumpsys procstats --hours 1 > "${dir + "/data-" + fileName}"`)
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

async function killApp(targetDevice, applicationId) {
    try {
        await exec(`adb -s ${targetDevice} shell pm clear ${applicationId}`)
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
    cleanBatteryStatus, outputBatteryStatsTo, done
}
