const adb = require("./adbCommands");
const fs = require("fs");

async function run(args) {
    try {
        const configFilePath = "configUI/config.json";
        const configFileContent = fs.readFileSync(configFilePath, "utf8");
        const config = JSON.parse(configFileContent);

        // Extract values from the config object
        const app_name = config.app_name;
        const methodName = config.method_name;
        const className = config.class_name;
        const packageName = config.package_name;
        const sufix = config.sufix;
        
        var execution_index = config.exec_start;
        const end_execution = config.exec_end;

        

        console.log(`starting test: running <${methodName}> on <${className}> `);
        while(execution_index < end_execution){
            console.log("exec #"+execution_index);
            await adb.cleanBatteryStatus("");
            await adb.startUITest(className, methodName, packageName, sufix);
            await adb.outputBatteryStatsTest(
                app_name,
                methodName,
                execution_index,
                packageName+'.'+sufix
            );
            execution_index++;
        }
        //done();
    } catch (error) {
        console.error("Error reading or parsing the JSON configuration file:", error);
        process.exit(1);
    }
}

module.exports = {
    run
};

