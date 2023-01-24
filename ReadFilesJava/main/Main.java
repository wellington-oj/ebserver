package main;
import enums.DeviceNames;
import enums.apps.experiments.ExampleInputs;
import enums.apps.experiments.ExampleNames;
import enums.interfaces.AppName;
import enums.interfaces.InputData;

record AppData(String prefix, AppName[] apps, InputData[] benchs){}

public class Main {
    
    public static final int DOUBLE_PRECISION = 5;

    public static final String PATH_TO_FILES = "../new_dashboard/src/experiment-results/";
    public static final String DEVICE_NAME = DeviceNames.S20FE4G.toString();
    public static final int NUM_FILES = 45;
    
    public static void main(final String[] args) {
 
        AppData spectral = new AppData("test", ExampleNames.values(), ExampleInputs.values());
        new RunAnalysis(spectral).run();
    }    
}