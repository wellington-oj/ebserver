const adb = require("./adbCommands")

async function runTests(args){
    if(args[2] === "-h" || args.length < 5){
        console.log("To execute the dashboard using tests inform in order:" 
        +"\n(1) the test class" 
        +"\n(2) the package name" 
        +"\n(3) the method to be executed")
        process.exit()
    }else{
        console.log(">> Running test mode")
        const className = args[2]
        const packageName = args[3]
        const methodName = args[4]
        
        var i = 1;
        console.log(`starting test: running <${methodName}> on <${className}> `);
        while(i < 45){
            console.log("exec #"+i);
            await adb.cleanBatteryStatus("")
            await adb.startTest(className, methodName, packageName)
            await adb.outputBatteryStatsTest(
                    'JAVA',
                    methodName,
                    i,
                    packageName
                )    
            i++;
        }
    }
    
}


module.exports = {
    runTests
}