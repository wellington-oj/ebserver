const adb = require("./adbCommands")

async function runTests(args){

    const execution_index = 1
    const end_execution = 45
    const app_name = 'JAVA'
    
    if(args.length > 2 && args.length < 5){
        console.log("To execute the dashboard using tests inform in order:" 
        +"\n(1) the test class" 
        +"\n(2) the package name" 
        +"\n(3) the method to be executed")
        process.exit()
    }else{
        console.log(">> Running Test Suite Mode")
        const className = args[2]
        const packageName = args[3]
        const methodName = args[4]
        
        console.log(`starting test: running <${methodName}> on <${className}> `);
        while(execution_index < end_execution){
            console.log("exec #"+i);
            await adb.cleanBatteryStatus("")
            await adb.startUITest(className, methodName, packageName)
            await adb.outputBatteryStatsTest(
                    app_name,
                    methodName,
                    execution_index,
                    packageName
                )    
            execution_index++;
        }
        done()
    }
}

module.exports = {
    runTests
}
