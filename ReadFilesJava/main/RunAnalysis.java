package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import enums.Metrics;
import enums.interfaces.AppName;
import enums.interfaces.InputData;
import readfiles.ReadDataFiles;
import readfiles.ReadFiles;
import readfiles.ReadMemoryFiles;
import utils.FileTools;

public class RunAnalysis {
    
    private String pathToFiles = Main.PATH_TO_FILES;
    private String device = Main.DEVICE_NAME;
    private int numberOfFiles = Main.NUM_FILES;

    private AppData appData;

    public RunAnalysis(AppData appData){
        
        this.appData = appData;

        System.out.println();
        System.out.println("<< Reading " + appData.prefix() + " >>");
    }

    public void run(){
        executeReading(appData.apps(), appData.benchs());
    }

    private void executeReading(AppName[] appNames, InputData[] benchs) {
        for (AppName framework : appNames)
            readFiles(benchs, framework);
    }

    private void readFiles(InputData[] benchmarks, AppName framework) {

        System.out.println("\n    <<< Starting " + framework + " >>>> \n");

        try{
            for (InputData inputData : benchmarks) {
                List<Metrics> metrics = new ArrayList<>();
                Map<Integer, Map<Metrics, Double>> mainMap = new ReadFiles(pathToFiles, framework, numberOfFiles, device).readSingle(inputData);
                Map<Integer, Map<Metrics, Double>> mapMemory = new ReadMemoryFiles(pathToFiles, framework, numberOfFiles, device).readSingle(inputData);
                Map<Integer, Map<Metrics, Double>> mapData = new ReadDataFiles(pathToFiles, framework, numberOfFiles, device).readSingle(inputData);
                
                mergeMaps(mainMap, mapMemory);
                mergeMaps(mainMap, mapData);
                
                metrics.addAll(List.of(ReadFiles.metrics()));
                metrics.addAll(List.of(ReadMemoryFiles.metrics()));
                metrics.addAll(List.of(ReadDataFiles.metrics()));

                FileTools.outputCSV(device, metrics, inputData, framework, mainMap);
                
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






