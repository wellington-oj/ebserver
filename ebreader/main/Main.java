package main;
import enums.DeviceNames;
import enums.apps.experiments.SampleExpData;
import enums.apps.experiments.SampleAppName;
import enums.interfaces.AppName;
import enums.interfaces.ExpData;

record AppData(String prefix, AppName[] apps, ExpData[] benchs){}

public class Main {
    
    public static final int DOUBLE_PRECISION = 5;

    public static final String PATH_TO_FILES = "../ebserver/src/experiment-results/";
    public static final String DEVICE_NAME = DeviceNames.S20FE4G.toString();
    public static final int NUM_FILES = 45;
    
    public static void main(final String[] args) {
 
        AppData test = new AppData("test", SampleAppName.values(), SampleExpData.values());
        new RunAnalysis(test).run();
    }    
}
