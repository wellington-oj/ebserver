var express = require("express");
var url = require("url");
var http = require("http");
var fs = require("fs");
var path = require("path");
var exec = require("child_process").exec;
var wc = require("./Logic/web-commands.js");
var ac = require("./Logic/app-commands.js");
var app = express();
var filePath =
  "C:\\Users\\welli\\Frameworks-Project\\Framework-Project\\ionicapp.apk";

app.use(express.static(__dirname + "/View"));
var myIpAddress = "10.42.0.1";
var myPort = 3000;
var targetDeviceIP = "10.42.0.23";

process.argv.forEach(function (val, index, array) {
  if (index === 2) {
    myPort = val;
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

function targetDevice() {
  if (targetDeviceIP) {
    return "-s " + targetDeviceIP;
  } else {
    return "";
  }
}

var adbCommandStrings = {
  cleanBatteryStats:
    "adb " + targetDevice() + " shell dumpsys batterystats --reset",
  cleanGeneralData:
    "adb " + targetDevice() + " shell dumpsys procstats --clear",
  outputBatteryStatsTo: function (destiny) {
    return "adb " + targetDevice() + " shell dumpsys batterystats > " + destiny;
  },
  outputMemoryData: function (destiny, applicationId) {
    return (
      "adb " +
      targetDevice() +
      " shell dumpsys meminfo " +
      applicationId +
      " -d > " +
      destiny
    );
  },
  outputGeneralData: function (destiny) {
    return "adb shell dumpsys procstats --hours 1 >" + destiny;
  },
  listDevices: "adb devices -l",
  deviceIpAddressCommand: "shell ip -f inet addr show wlan0",
  showDeviceIpAddress: "adb shell ip -f inet addr show wlan0",
  wakeUpDevice:
    "adb " + targetDevice() + " shell input keyevent KEYCODE_WAKEUP",
  startApp: function (
    benchmark,
    nWarmUpIterations,
    nIterations,
    applicationId,
    mainActivity
  ) {
    let result =
      "adb " +
      targetDevice() +
      " shell am start -n " +
      applicationId +
      "/" +
      mainActivity +
      " -e param " +
      benchmark +
      " -e ipAddress " +
      myIpAddress +
      " -e port " +
      myPort;
    if (nWarmUpIterations) {
      result += " -e nWarmUpIterations " + nWarmUpIterations;
    }
    if (nIterations) {
      result += " -e nIterations " + nIterations;
    }
    return result;
  },
  getAppPid: function (applicationId) {
    return "adb " + targetDevice() + " shell pgrep " + applicationId;
  },
  killApp: function (applicationId) {
    return "adb " + targetDevice() + " shell pm clear " + applicationId;
    //return "adb "+targetDevice()+" shell am force-stop "+applicationId
  },
  deleteApk: function (applicationId) {
    return "adb " + targetDevice() + " uninstall " + applicationId;
    //return "adb "+targetDevice()+" shell am force-stop "+applicationId
  },
  installApk: function () {
    return "adb " + targetDevice() + " install " + filePath;
  },
  finishedBeep: "[console]::beep(150,50)",
};

var sessionData = {
  currentTest: ac.benchmarks()[0],
  counter: { current: 1, until: 30 },
  device: "DEBUG DEVICE",
  framework: "Ionic",
  stop: true,
  fullRun: false,
  nWarmUpIterations: undefined,
  nIterations: undefined,
  applicationId: "io.ionic.starter",
  //applicationId: "com.rnapp",
  mainActivity: ".MainActivity",
};

/*
USED BY THE APP
*/
app.get("/what_now", function (req, res) {
  ac.whatNow(exec, adbCommandStrings, sessionData, res);
});
app.get("/done", function (req, res) {
  ac.done(exec, adbCommandStrings, sessionData, http, myPort, res);
});
app.get("/cleanbattery", function (req, res) {
  ac.cleanbattery(exec, adbCommandStrings, res);
});
app.get("/logdata", function (req, res) {
  ac.logdata(exec, adbCommandStrings, sessionData, res, http, fs, myPort);
});
/*****************/

/**
USED BY THE WEB APPLICATION
*/
app.get("/", function (req, res) {
  res.sendFile("index.html");
});

app.get("/start", function (req, res) {
  wc.start(exec, adbCommandStrings, sessionData, res);
});
app.get("/full_run", function (req, res) {
  wc.fullRun(exec, adbCommandStrings, sessionData, res);
});
app.get("/stop", function (req, res) {
  wc.stop(exec, adbCommandStrings, sessionData, res);
});
app.get("/info", function (req, res) {
  wc.info(sessionData, res);
});
app.get("/set", function (req, res) {
  wc.set(sessionData, req, res);
});
app.get("/list_devices", function (req, res) {
  wc.listDevices(exec, adbCommandStrings, res);
});
app.get("/show_device_ip_address", function (req, res) {
  wc.showDeviceIpAddress(exec, adbCommandStrings, res);
});
app.get("/wake_up_device", function (req, res) {
  wc.wakeUpDevice(exec, adbCommandStrings, res);
});
app.get("/install_apk", function (req, res) {
  wc.installApk(
    exec,
    adbCommandStrings,
    req.query.file_path,
    targetDevice(),
    res
  );
});
/*********************/

app.listen(myPort, function () {
  console.log("Dashboard app listening on port " + myPort + "!");
});
