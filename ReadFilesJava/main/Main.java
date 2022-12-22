package main;
import java.util.ArrayList;
import java.util.List;

import enums.DeviceNames;
import enums.apps.test.TestInputs;
import enums.apps.test.TestNames;
import enums.interfaces.AppName;
import enums.interfaces.InputData;
import enums.interfaces.Modes;
import readfiles.ReadFiles;
import readfiles.ReadMemoryFiles;

record AppData(String prefix, AppName[] apps, InputData[] benchs){}

public class Main {
    
    public static final int DOUBLE_PRECISION = 5;

    public static final String PATH_TO_FILES = "../new_dashboard/src/experiment-results/";
    public static final String DEVICE_NAME = DeviceNames.TEST.toString();
    public static final int NUM_FILES = 30;
    
    public static void main(final String[] args) {

        List<Modes> modes = new ArrayList<>();
        modes.addAll(ReadFiles.modes());
        modes.addAll(ReadMemoryFiles.modes());

        AppData test = new AppData("test", TestNames.values(), TestInputs.values());
        RunAnalysis runTest = new RunAnalysis(test,modes);
        runTest.runCreateRFiles(true, TestInputs.values(), true, 1);

    }

    private static List<String> getListBenchmarks(InputData[] values) {
      List<String> lista = new ArrayList<String>();
      for (InputData inputData : values) {
        lista.add(inputData.graphForm());
      }
      return lista;
    }    
}