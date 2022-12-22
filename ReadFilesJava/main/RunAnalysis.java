package main;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import enums.FileType;
import enums.interfaces.AppName;
import enums.interfaces.InputData;
import enums.interfaces.Modes;
import readfiles.ReadFiles;
import readfiles.ReadMemoryFiles;
import utils.FileTools;

public class RunAnalysis {
    
    private String pathToFiles = Main.PATH_TO_FILES;
    private String device = Main.DEVICE_NAME;
    private int numberOfFiles = Main.NUM_FILES;

    private AppData appData;

    private Map<AppName, List<InputData>> allValidBenchmarks = new HashMap<>();
    private List<Modes> modes;
    private File allDataFile;

    public RunAnalysis(AppData appData, List<Modes> modes){
        
        this.appData = appData;
        this.modes = modes;
        this.allDataFile = FileTools.getDumpFile(appData.prefix());

        System.out.println();
        System.out.println("<< Reading " + appData.prefix() + " >>");
    }

    public void runCreateRFiles(boolean classic, List<String> benchmarks, boolean graphInfo, int nColumns){
        executeReading(appData.apps(), appData.benchs(), modes);
        FileTools.createBoxPlotFile(appData.benchs(),appData.prefix(),allValidBenchmarks, modes);
        FileTools.createGGPLOT(appData.prefix(), classic, benchmarks, graphInfo, nColumns, modes);  
    }

    public void run(){
        executeReading(appData.apps(), appData.benchs(), modes);
    }

    private InputData[] validBenchmarks(InputData[] benchmarks, AppName framework) {

        List<InputData> validBenchmarks = new ArrayList<InputData>();
        StringBuilder errorLog = new StringBuilder();

        for (InputData bench : benchmarks) {
            String lastFile = FileTools.getFilePath(FileType.ENERGY, pathToFiles, 
                                                    framework, device, bench,numberOfFiles);
            File file = new File(lastFile);
            if(file.exists())
                validBenchmarks.add(bench);
            else
                errorLog.append(
                    String.format("last file (%d) not found for %s - %s \n", 
                                numberOfFiles, bench, bench.getWorkload())
                );
        }
        System.out.print(errorLog);
        return validBenchmarks.toArray(InputData[]::new);
    }

    private void executeReading(AppName[] appNames, InputData[] benchs, List<Modes> modes) {
        for (AppName framework : appNames)
            readFiles(benchs, framework, modes);
    }

    private void readFiles(InputData[] benchmarks, AppName framework, List<Modes> modes) {

        System.out.println("\n    <<< Starting " + framework + " >>>> \n");

        StringBuilder allData = new StringBuilder();

        InputData[] validBenchmarks = validBenchmarks(benchmarks, framework);
        allValidBenchmarks.put(framework,Arrays.asList(benchmarks));

        try{
            Map<Modes, String> mapData = new ReadFiles(pathToFiles, framework, numberOfFiles, device).readAll(validBenchmarks);
            Map<Modes, String> mapMemory = new ReadMemoryFiles(pathToFiles, framework, numberOfFiles, device).readAll(validBenchmarks);
            mapData.putAll(mapMemory);
            
            for (Modes mode : modes){
                FileTools.dataToFile(benchmarks, appData.prefix(), device, framework, mode, mapData);
                allData.append(mapData.get(mode)); 
            }

            FileTools.printData(allData, allDataFile); 

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}






