const adb = require("./adbCommands")

async function runTests(args){

    var execution_index = 1
    const end_execution = 45
    const app_name = 'JAVA'
    
    if(args.length > 2 && args.length < 6){
        console.log("To execute the dashboard using tests inform in order:" 
        +"\n(1) the method to be executed"
        +"\n(2) the test class"
        +"\n(3) the package name"
        +"\n(4) the package test sufix"
        )
        process.exit()
    }else{
        console.log(">> Running Test Suite Mode")
        
        const methodName = args[2]
        const className = args[3]
        const packageName = args[4]
        const sufix = args[5]	
        
        
        console.log(`starting test: running <${methodName}> on <${className}> `);
        while(execution_index < end_execution){
            console.log("exec #"+execution_index);
            await adb.cleanBatteryStatus("")
            await adb.startUITest(className, methodName, packageName, sufix)
            await adb.outputBatteryStatsTest(
                    app_name,
                    methodName,
                    execution_index,
                    packageName+'.'+sufix
                )    
            execution_index++;
        }
        done()
    }
}

module.exports = {
    runTests
}
