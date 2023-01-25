package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import enums.Commands;
import enums.Metrics;
import enums.interfaces.AppName;
import enums.interfaces.ExpData;
import readfiles.ReadBatteryFiles;
import readfiles.ReadMemoryFiles;
import readfiles.ReadProcFiles;
import utils.FileTools;

public class RunAnalysis {
    
    private AppData appData;

    public RunAnalysis(AppData appData){
        
        this.appData = appData;
        System.out.println("\n<< Reading " + appData.prefix() + " >>");
    }

    public void run(){
        executeReading(appData.apps(), appData.benchs());
    }

    private void executeReading(AppName[] appNames, ExpData[] benchs) {
        for (AppName framework : appNames)
            readFiles(benchs, framework);
    }

    private void readFiles(ExpData[] benchmarks, AppName framework) {

        System.out.println("\n    <<< Starting " + framework + " >>>> \n");

        try{
            for (ExpData inputData : benchmarks) {
                List<Metrics> metrics = new ArrayList<>();
                Map<Integer, Map<Metrics, Double>> mainMap = 
                    new ReadBatteryFiles(framework, Commands.BATTERY_STATS).readSingle(inputData);
                Map<Integer, Map<Metrics, Double>> mapMemory = 
                    new ReadMemoryFiles(framework, Commands.MEM_INFO).readSingle(inputData);
                Map<Integer, Map<Metrics, Double>> mapData = 
                    new ReadProcFiles(framework, Commands.PROC_STATS).readSingle(inputData);
                
                mergeMaps(mainMap, mapMemory);
                mergeMaps(mainMap, mapData);
                
                metrics.addAll(List.of(ReadBatteryFiles.metrics()));
                metrics.addAll(List.of(ReadMemoryFiles.metrics()));
                metrics.addAll(List.of(ReadProcFiles.metrics()));

                FileTools.outputCSV(Main.DEVICE_NAME, metrics, inputData, framework, mainMap);
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mergeMaps(Map<Integer, Map<Metrics, Double>> mapData, Map<Integer, Map<Metrics, Double>> mapMemory){
        for (Integer key : mapData.keySet()) {
            Map<Metrics, Double> map = mapData.get(key);
            map.putAll(mapMemory.get(key));
        }
    }
}






