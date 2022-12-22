package readfiles;
import java.io.File;
import java.io.FileNotFoundException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import enums.FileType;
import enums.interfaces.AppName;
import enums.interfaces.InputData;
import enums.interfaces.Modes;
import utils.FileTools;
import utils.RTools;
import utils.ConversionTools;

public class ReadDataFiles {
   
    private enum ModeEnumData implements Modes{
        DATA_MEMORY("dmemory"),
        DATA_TIME("dtime"),
        DATA_TOTAL("dtotal");
    
        private String shortVersion;
        ModeEnumData(String shortVersion){
            this.shortVersion = shortVersion;
        }
        public String getShortVersion(){
            return shortVersion;
        }
    }

    private String path;
    private String device;
    private AppName framework;
    private int numberOfFiles;

    private Map<Modes, String> map = new HashMap<>();

    private List<Double> memoryLow = new ArrayList<>();
    private List<Double> memoryMed = new ArrayList<>();
    private List<Double> memoryHigh = new ArrayList<>();

    private double memDataLow;
    private double memDataMed;
    private double memDataHigh;

    private List<Double> time = new ArrayList<>();
    private List<Double> total = new ArrayList<>();

    public ReadDataFiles(String path, int precision, AppName framework, int numberOfFiles, String device){
        this.path = path;
        this.framework = framework;
        this.numberOfFiles = numberOfFiles;
        this.device = device;
    }

    public Map<Modes, String>  readAll(InputData[] benchmarkSet) {
        Arrays.asList(benchmarkSet).forEach(benchmark -> readSingle(benchmark));
        return map;
    }

    public void readSingle(InputData benchmark) throws InvalidParameterException{
        for (int i = 1; i < numberOfFiles + 1; i++) {
            executeReadFile(i, benchmark);
        }
        String lowMem = RTools.putInRFormat(benchmark, memoryLow, "ml");
        memoryLow.clear();

        String midmem = RTools.putInRFormat(benchmark, memoryMed, "mm");
        memoryMed.clear();

        String highmem = RTools.putInRFormat(benchmark, memoryHigh, "mh");
        memoryHigh.clear();

        map.put(ModeEnumData.DATA_MEMORY, lowMem+midmem+highmem);

        map.put(ModeEnumData.DATA_TIME, RTools.putInRFormat(benchmark, time,"ti"));
        time.clear();

        map.put(ModeEnumData.DATA_TOTAL,RTools.putInRFormat(benchmark, total,"to"));
        total.clear();
    }

    private void executeReadFile(int fileNumber, InputData benchmark) {
        
        File text = new File(FileTools.getFilePath(FileType.DATA,
        path, framework, device, benchmark, fileNumber));

        try ( Scanner scnr = new Scanner(text);){
            while (scnr.hasNextLine()) {
                String line = scnr.nextLine();
                if(line.contains(framework.getAppName())){
                    getMemoryConsumption(scnr.nextLine());
                }
                if (line.contains("TOTAL  : ")){
                    line = line.split("TOTAL  : ")[1];
                    if(line.contains("GB")){
                        total.add(Double.parseDouble(line.split("GB")[0])*1000);
                    }
                    else{
                        total.add(Double.parseDouble(line.split("MB")[0]));
                    }
                }
                if(line.contains("  Total elapsed time: +")){
                    String timeToExec = line.split("Total elapsed time:")[1].substring(2).split("ms")[0];
                    time.add(ConversionTools.fromStringToSec(timeToExec));
                }
            }
            setMemoryConsumption();
        } catch (FileNotFoundException e) {
             e.printStackTrace();
        }  
    }

    private void setMemoryConsumption() {
        memoryLow.add(memDataLow);
        memoryMed.add(memDataMed);
        memoryHigh.add(memDataHigh);
        
        memDataLow = 0;
        memDataMed = 0;
        memDataHigh = 0;
    }

    private void getMemoryConsumption(String line) {
        if(line.contains("(Cached)") || !line.contains("TOTAL: 100% ")) return; 
        String bar = "/";
        String totalString = "TOTAL: 100% ";
        String memory = line.split(totalString)[1].substring(1).split(bar)[0]; //TOTAL: 100% (181MB-680MB-848MB/176MB-671MB-838MB over 4)
        
        for (int i = 0; i < 3; i++) {
            Double memoryData = memoryInMB(memory.split("-")[i]);
            switch(i){
                case 0: memDataLow += memoryData; break;
                case 1: memDataMed += memoryData; break;
                case 2: memDataHigh += memoryData; break;
                default: System.out.println("error"); break;
            }
        }

    }

    private Double memoryInMB(String data) {
        if(data.contains("MB"))
            return Double.parseDouble(data.split("MB")[0]);
        else if(data.contains("GB"))
            return Double.parseDouble(data.split("GB")[0])*1000;
        else
            return null;
    }
}