package readfiles;

import java.io.FileNotFoundException;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import enums.interfaces.ExpData;
import enums.Commands;
import enums.Metrics;
import enums.interfaces.AppName;
import utils.ConversionTools;

public class ReadBatteryFiles extends ReadFiles{

    private enum MetricsEnergy implements Metrics{
        ENERGY("energy"),
        FOREGROUND_TIME("exec_time"),
        CPU_TIME("cpu_time");
    
        private final String value;
        MetricsEnergy(String value){
            this.value = value;
        }
        public String value(){
            return value;
        }
    }

    public static Metrics[] metrics(){
        return MetricsEnergy.values();
    }

    private record TimeData(double foregroundTime, double cpuTime){}
    private enum SearchState {VOLTAGE, APP_ID, ENERGY, FOREGROUND_TIME, OVER;}

    public ReadBatteryFiles(AppName framework, Commands metricsCMD) {
        super(framework, metricsCMD);
    }

    public Map<Integer, Map<Metrics, Double>> readSingle(ExpData benchmark) {
        int i = 1;
        try {
            for (; i < numberOfFiles + 1; i++){
                mapMetrics = new HashMap<>();
                executeReadFile(i, benchmark);
                map.put(i, mapMetrics);
            }

        } catch (InvalidParameterException e) {
            e.printStackTrace();
        }
        catch (FileNotFoundException e) {
            System.out.println("file (" +i+ ") not found for " +  benchmark + 
                        "-" + benchmark.getWorkload());
        }
        return map;
    }

    private void executeReadFile(int fileNumber, ExpData benchmark) throws FileNotFoundException {
        SearchState searchState = SearchState.VOLTAGE;

        try (Scanner scnr = new Scanner(getFile(benchmark, fileNumber));) {
            while (scnr.hasNextLine() && searchState != SearchState.OVER) {
                    Double voltage = findVoltage(scnr);
                    searchState = nextState(searchState);
                    
                    framework.setAppID(findAppId(scnr)); 
                    searchState = nextState(searchState);

                    mapMetrics.put(MetricsEnergy.ENERGY,findEnergyData(voltage, scnr));
                    searchState = nextState(searchState);

                    TimeData timeData = findTimeData(scnr);
                    mapMetrics.put(MetricsEnergy.FOREGROUND_TIME,timeData.foregroundTime());
                    mapMetrics.put(MetricsEnergy.CPU_TIME,timeData.cpuTime());

                    searchState = nextState(searchState);
                }
            } 
        }

    private SearchState nextState(SearchState searchState) {
        return SearchState.values()[searchState.ordinal()+1];
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