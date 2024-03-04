const fs = require("fs");
const util = require("util");
const exec = util.promisify(require("child_process").exec);

async function runCommand(command, targetDevice, dir, fileName) {
  try {
    await exec(
      `adb ${getTarget(targetDevice)} shell dumpsys ${command.name} ${
        command.params
      }  >  ${dir}/${command.name}-${fileName}`
    );
  } catch (error) {
    console.log(`ERROR EXECUTING ` + command.name + ` ON ${targetDevice}`);
  }
}

async function connectWifi() {
  try {
    // Check the number of connected devices
    const deviceCmd = await exec("adb devices");
    const strDev = deviceCmd.stdout.toString();
    const numDevices = strDev.split("\n").length - 3;

    console.log(numDevices);

    if (numDevices > 1) {
      console.error(
        "More than one device connected. Make sure that there is only one device."
      );
      return "error";
    }

    const deviceIpRegex = /(\d+\.\d+\.\d+\.\d+)/;
    const deviceMatch = strDev.match(deviceIpRegex);

    // Get WiFi IP address on the connected device
    const ipCmd = await exec("adb shell ip addr show wlan0");
    const ipRegex = /inet (\d+\.\d+\.\d+\.\d+)/;
    const ipMatch = ipCmd.stdout.toString().match(ipRegex);

    if (!ipMatch) {
      console.error(
        "Unable to extract WiFi IP address from the adb shell command output"
      );
      return "error";
    }

    const ip = ipMatch[1];

    // If not already connected, attempt to connect
    if (!(deviceMatch && ipMatch)) {
      console.log(
        `Device not connected via Wi-fi. Connecting to ${ip}. Remove the USB cable and run ebserver again.`
      );
      await exec(`adb connect ${ip}`);
      return "error";
    } else {
      console.log(`Connected to ${ip}`);
    }

    return ip;
  } catch (error) {
    console.error("Error running adb shell command:", error);
    return "error";
  }
}

function getTarget(targetDevice) {
  return targetDevice === "" ? " " : ` -s ${targetDevice} `;
}

async function cleanBatteryStatus(targetDevice) {
  try {
    await exec(
      `adb ${getTarget(targetDevice)} shell dumpsys procstats --clear`
    );
    await exec(
      `adb ${getTarget(targetDevice)} shell dumpsys batterystats --reset`
    );
  } catch (error) {
    console.log(`ERROR CLEANING STATUS ${targetDevice}`);
  }
}

async function outputBatteryStatsTo(
  targetDevice = "",
  framework,
  currentTest,
  counter,
  packageName
) {
  const fileName = `${counter}.txt`;
  const device = await exec(
    "adb" + getTarget(targetDevice) + "shell getprop ro.product.model"
  );
  createDirIfNotExists(fs, "../experiment-results");
  createDirIfNotExists(fs, "../experiment-results/" + framework);
  createDirIfNotExists(
    fs,
    "../experiment-results/" +
      framework +
      "/" +
      device.stdout.trim().replace(/ /g, "-")
  );
  const dir =
    "../experiment-results/" +
    framework +
    "/" +
    "/" +
    device.stdout.trim().replace(/ /g, "-") +
    "/" +
    currentTest;
  createDirIfNotExists(fs, dir);

  const meminfo = { name: "meminfo", params: ` ${packageName} -d` };
  const batterystats = { name: "batterystats", params: "" };
  const procstats = { name: "procstats", params: ` --hours 1` };

  await runCommand(meminfo, targetDevice, dir, fileName);
  await runCommand(batterystats, targetDevice, dir, fileName);
  await runCommand(procstats, targetDevice, dir, fileName);
}

async function outputBatteryStatsTest(
  framework,
  currentTest,
  counter,
  packageName
) {
  outputBatteryStatsTo("", framework, currentTest, counter, packageName);
}

async function startApp(targetDevice, applicationId, mainActivity) {
  try {
    await exec(
      "adb" +
        getTarget(targetDevice) +
        "shell am start -n " +
        applicationId +
        "/" +
        mainActivity
    );
  } catch (error) {
    console.log(`ERROR STARTING APP ${targetDevice}`);
  }
}

async function startUITest(className, methodName, packageName, sufix) {
  try {
    await exec(
      `adb shell am instrument -w -e debug false -e class ${packageName}.${className}#${methodName} ${packageName}.${sufix}/androidx.test.runner.AndroidJUnitRunner`
    );
  } catch (error) {
    console.log(error);
    console.log(`ERROR STARTING APP`);
  }
}

async function killApp(targetDevice, applicationId) {
  try {
    await exec(
      "adb" + getTarget(targetDevice) + "shell pm clear " + applicationId
    );
  } catch (error) {
    console.log(`ERROR KILLING APP ${targetDevice} ${applicationId}`);
  }
}

async function done(targetDevice, applicationId, mainActivity) {
  await killApp(targetDevice, applicationId);
  await startApp(targetDevice, applicationId, mainActivity);
}

function createDirIfNotExists(fs, dir) {
  if (!fs.existsSync(dir)) {
    fs.mkdirSync(dir);
  }
}

module.exports = {
  cleanBatteryStatus,
  outputBatteryStatsTo,
  outputBatteryStatsTest,
  done,
  startUITest,
  connectWifi,
};
