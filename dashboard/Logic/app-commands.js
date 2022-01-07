// app-commands.js
// ========

//var benchmarks = ["binarytree","fannkuch","fasta","knucleotide","mandelbrot","nbody","pidigits","regex","revcomp","spectral"]
//"pidigits"
//var benchmarks = ["binarytree","fannkuch","fasta","fastaold","knucleotide","nbody","pidigits","regex","regexold","revcomp","revcompold","spectral"]

//THIS ONE
//var benchmarks = ["binarytree","fannkuch","fastaold","knucleotide","nbody","pidigits","regexold","revcompold","spectral","blank"]
//THIS ONE

//var benchmarks = ["nbody6250000", "nbody12500000", "nbody25000000"];
var benchmarks = [
  "rotating-micro-7",
  "rotating-micro-14",
  "rotating-micro-28",
  "rotating-small-7",
  "rotating-small-14",
  "rotating-small-28",
  "rotating-medium-7",
  "rotating-medium-14",
  "rotating-medium-28",
];
//var benchmarks = ["binarytree","fasta","fastaold","regex","regexold","revcomp","revcompold","spectral"]

function getNextBenchmark(sessionData) {
  for (let index = 0; index < benchmarks.length; index++) {
    if (sessionData.currentTest == benchmarks[index]) {
      return benchmarks[index + 1];
    }
  }
}

function createDirIfNotExists(fs, dir) {
  if (!fs.existsSync(dir)) {
    fs.mkdirSync(dir);
  }
}
function callStartApp(exec, adbCommandStrings, http, myPort) {
  http
    .get(
      { host: "localhost", port: myPort, path: "/start" },
      function (resp) {}
    )
    .on("error", function (e) {
      console.log(e.message);
    });
}
function logDataFunc(exec, adbCommandStrings, sessionData, fs, callback) {
  var fileName =
    sessionData.framework +
    "-" +
    sessionData.device +
    "-" +
    sessionData.currentTest +
    "-" +
    sessionData.counter.current +
    ".txt";
  var fileNameMemory =
    "memory-" +
    sessionData.framework +
    "-" +
    sessionData.device +
    "-" +
    sessionData.currentTest +
    "-" +
    sessionData.counter.current +
    ".txt";
  var fileGeneralData =
    "data-" +
    sessionData.framework +
    "-" +
    sessionData.device +
    "-" +
    sessionData.currentTest +
    "-" +
    sessionData.counter.current +
    ".txt";
  createDirIfNotExists(fs, "experiment-results");
  createDirIfNotExists(fs, "experiment-results/" + sessionData.framework);
  createDirIfNotExists(
    fs,
    "experiment-results/" + sessionData.framework + "/" + sessionData.device
  );
  var dir =
    "experiment-results/" +
    sessionData.framework +
    "/" +
    "/" +
    sessionData.device +
    "/" +
    sessionData.currentTest;
  createDirIfNotExists(fs, dir);
  if (callback) {
    exec(
      adbCommandStrings.outputMemoryData(
        dir + "/" + fileNameMemory,
        sessionData.applicationId
      ),
      function (error, stdout, stderr) {
        exec(
          adbCommandStrings.outputGeneralData(dir + "/" + fileGeneralData),
          function (error, stdout, stderr) {
            exec(
              adbCommandStrings.outputBatteryStatsTo(dir + "/" + fileName),
              function (error, stdout, stderr) {
                callback();
              }
            );
          }
        );
      }
    );
  }
}
module.exports = {
  benchmarks: function () {
    return benchmarks;
  },
  whatNow: function (exec, adbCommandStrings, sessionData, res) {
    exec(adbCommandStrings.cleanBatteryStats, function (error, stdout, stderr) {
      res.send(sessionData.currentTest);
    });
  },
  /*logdata : function(exec,adbCommandStrings,sessionData,res,http,fs,myPort){
		logData(exec,adbCommandStrings,sessionData,fs,function(){
			sessionData.currentTest = getNextBenchmark(sessionData);
			if(sessionData.counter.current < sessionData.counter.until || sessionData.currentTest != undefined){
				if(sessionData.currentTest == undefined){
					sessionData.counter.current++;	
					sessionData.currentTest = benchmarks[0];
				}
				res.send("done");
			}
		});
	},*/
  done: function (exec, adbCommandStrings, sessionData, http, myPort, res) {
    exec(adbCommandStrings.killApp(sessionData.applicationId));
    sessionData.counter.current++;
    if (sessionData.counter.current > sessionData.counter.until) {
      sessionData.currentTest = getNextBenchmark(sessionData);
      sessionData.counter.current = 1;
    }
    setTimeout(function () {
      res.send("close");
    }, 1000);
    exec(adbCommandStrings.finishedBeep);
    setTimeout(function () {
      callStartApp(exec, adbCommandStrings, http, myPort);
    }, 3000);
  },
  logdata: function (
    exec,
    adbCommandStrings,
    sessionData,
    res,
    http,
    fs,
    myPort
  ) {
    logDataFunc(exec, adbCommandStrings, sessionData, fs, function () {
      res.send("done");
    });
  },

  cleanbattery: function (exec, adbCommandStrings, res) {
    exec(adbCommandStrings.cleanBatteryStats);
    exec(adbCommandStrings.cleanGeneralData);
    setTimeout(function () {
      res.send("cleaned");
    }, 3000);
  },

  //this code is oriented to the second version of dashboard
};
