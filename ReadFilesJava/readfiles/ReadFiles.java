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

import enums.interfaces.InputData;
import enums.FileType;
import enums.interfaces.AppName;
import enums.interfaces.Modes;
import utils.FileTools;
import utils.RTools;
import utils.ConversionTools;

public class ReadFiles {

    private enum ModeEnum implements Modes{
        ENERGY("ee"),
        FOREGROUND_TIME("ef"),
        CPU_TIME("ec");
    
        private final String shortVersion;
        ModeEnum(String shortVersion){
            this.shortVersion = shortVersion;
        }
        public String getShortVersion(){
            return shortVersion;
        }
    }

    private record TimeData(double foregroundTime, double cpuTime){}
    private enum SearchState {VOLTAGE, APP_ID, ENERGY, FOREGROUND_TIME, OVER;}

    private String path;
    private String device;
    private AppName framework;
    private int numberOfFiles;

    private int countReplace;
    private String errorLog = "";

    private Map<Modes, String> map = new HashMap<>();
    private List<Double> consumption = new ArrayList<>();
    private List<Double> timeForeground = new ArrayList<>();
    private List<Double> timeCPU = new ArrayList<>();

    public ReadFiles(String path, AppName framework, int numberOfFiles, String device) {
        this.path = path;
        this.framework = framework;
        this.numberOfFiles = numberOfFiles;
        this.device = device;
    }

    public static List<? extends Modes> modes(){
        return List.of(ModeEnum.values());
    }

    public Map<Modes, String> readAll(InputData[] benchmarkSet) {
        Arrays.asList(benchmarkSet).forEach(benchmark -> readSingle(benchmark));
        if(!errorLog.equals(""))
            System.out.println(errorLog);

        return map;
    }

    public void readSingle(InputData benchmark) {
        int i = 1;
        if (numberOfFiles > 30) {
            i = numberOfFiles-30;
            countReplace = i;
        }
        try {
            for (; i < numberOfFiles + 1; i++){
                executeReadFile(i, benchmark);
            }

            mergeData(benchmark);
            clearData();

        } catch (InvalidParameterException e) {
            e.printStackTrace();
        }
        catch (FileNotFoundException e) {
            errorLog += "file (" +i+ ") not found for " +  benchmark + 
                        "-" + benchmark.getWorkload() + " (probably timeout) \n";
        }
    }

    private void clearData() {
        timeForeground.clear();
        timeCPU.clear();
        consumption.clear();
    }

    private void mergeData(InputData benchmark) {
        map.merge(ModeEnum.FOREGROUND_TIME,
                RTools.putInRFormat(ModeEnum.FOREGROUND_TIME,benchmark, framework, timeForeground), 
                (x,y) -> x+y);
        map.merge(ModeEnum.CPU_TIME,
                RTools.putInRFormat(ModeEnum.CPU_TIME,benchmark, framework, timeCPU), 
                (x,y) -> x+y);
        map.merge(ModeEnum.ENERGY,
                RTools.putInRFormat(ModeEnum.ENERGY,benchmark, framework, consumption), 
                (x,y) -> x+y);
    }

    private void executeReadFile(int fileNumber, InputData benchmark) throws FileNotFoundException {

        File file = getFile(fileNumber, benchmark);
        SearchState searchState = SearchState.VOLTAGE;

        try (Scanner scnr = new Scanner(file);) {
            while (scnr.hasNextLine() && searchState != SearchState.OVER) {
                    Double voltage = findVoltage(scnr);
                    searchState = nextState(searchState);
                    
                    framework.setAppID(findAppId(scnr)); 
                    searchState = nextState(searchState);

                    consumption.add(findEnergyData(voltage, scnr));
                    searchState = nextState(searchState);

                    TimeData timeData = findTimeData(scnr);
                    timeForeground.add(timeData.foregroundTime());
                    timeCPU.add(timeData.cpuTime());

                    searchState = nextState(searchState);
                }
            } catch (IllegalArgumentException e) {
                countReplace = findFileReplacement(fileNumber, benchmark, searchState);
                if(countReplace > 0)
                    executeReadFile(countReplace,benchmark);
                else
                    System.out.println("<< ERROR: can't replace because there are no more files >>");
            }
        }

    private SearchState nextState(SearchState searchState) {
        return SearchState.values()[searchState.ordinal()+1];
    }

    private int findFileReplacement(int fileNumber, InputData benchmark, SearchState searchState) {
        --countReplace;
        //if(countReplace > 0){
            System.out.println(
                String.format(
                "\n << ERROR: file %d of benchmark %s %s stopped at %s. Replacing it with file: %d >> \n", 
                fileNumber,
                benchmark.toString(),
                framework.getAppName(),
                searchState.name(),
                countReplace)
            );
        //}
        return countReplace;
    }

    private File getFile(int fileNumber, InputData benchmark) {
        String fullFilePath = 
            FileTools.getFilePath(FileType.ENERGY, path, framework, device, benchmark, fileNumber);
        return new File(fullFilePath);
    }

    private boolean isLineWithConsumption(String line, AppName framework, String uid) {
        return line.contains(framework.getAppID()) && line.contains(uid);
    }

    private Double findVoltage(Scanner scnr){
        while (scnr.hasNextLine()) {
            String line = scnr.nextLine();
            if(line.contains("volt")) 
                return (Double.parseDouble(line.split("volt=")[1].split("c")[0]) / 1000);
        }
        throw new IllegalArgumentException();
    }

    private String findAppId(Scanner scnr){
        while (scnr.hasNextLine()) {
            String line = scnr.nextLine();
            if(line.contains(framework.getAppName()))
                return line.split("=")[1].split(":")[0];
        }
        throw new IllegalArgumentException();
    }

    private double findEnergyData(double voltage, Scanner scnr) {
        while (scnr.hasNextLine()) {
            String line = scnr.nextLine();
            if(isLineWithConsumption(line, framework, "UID") || isLineWithConsumption(line, framework, "Uid"))
                return getConsumption(line, voltage);
        }
        throw new IllegalArgumentException();
    }

    private TimeData findTimeData(Scanner scnr) {
        while (scnr.hasNextLine()) {
            String line = scnr.nextLine();
            if (line.contains(framework.getAppID()) && !line.contains("=")){
                return new TimeData(findForeground(scnr),findCPUTime(scnr));
            }
        }
        throw new IllegalArgumentException();
    }
    private double findCPUTime(Scanner scnr) {
        while (scnr.hasNextLine()) {
            String line = scnr.nextLine();
            if(line.contains("Total cpu time")) 
                return ConversionTools.fromStringToSec(line.split("u=")[1].split("ms")[0]);
        }
        throw new IllegalArgumentException();
    }

    private double findForeground(Scanner scnr){
        while (scnr.hasNextLine()) {
            String line = scnr.nextLine();
            if(line.contains("Foreground activities:")) 
                return ConversionTools.fromStringToSec(line.split(":")[1].split("ms")[0]);
        }
        throw new IllegalArgumentException();
    }
    

    private double getConsumption(final String line,double voltage) {
        return ConversionTools.fromMAHtoJOULE(line.split(": ")[1].split("\\(")[0].replace(",", "."),voltage); 
    }
}