package utils;
import enums.interfaces.InputData;
import enums.interfaces.Modes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import enums.FileType;
import enums.interfaces.AppName;

public class FileTools {
    
    public final static String DATA_PATH = "RData/apps/";

    public static void createDirectory(String path){
        try {
            Files.createDirectories(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getFilePath(FileType type, String path, AppName appName, 
                                    String device, InputData benchmark, int fileNumber) {
        return  pathToFolder(path,appName,device,benchmark) + type + "-" +fileNumber + ".txt";
    }

    private static String pathToFolder(String path, AppName appName, 
                                    String device, InputData benchmark){
        return  path + appName.toString() + "/" + device + "/" + 
                benchmark.toString() +"-"+ benchmark.getWorkload() + "/";
    }


    public static File getDumpFile(String prefix) {

        String path = DATA_PATH+prefix;

        FileTools.createDirectory(path);
        File allDataFile = new File(path+"/allData.r");

        try {
            allDataFile.delete();
            allDataFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allDataFile;
    }

    public static void dataToFile(InputData[] benchmarks, String prefix, String device, AppName framework, 
                                    Modes mode, Map<Modes, String> mapData) throws FileNotFoundException {

        String filesData = mapData.get(mode);

        String parmflow = RTools.getParMfrow(benchmarks);
        String barplots = RTools.getBarplotR(benchmarks, mode, framework);
        String meansAndSDs = RTools.printMeanAndSD(benchmarks, mode, framework);

        filesData += parmflow + barplots + meansAndSDs;
        
        String path = FileTools.DATA_PATH+prefix+"/"+framework+"/";

        createDirectory(path);

        File rFile = new File(path+device+"-"+framework+"-"+mode+".r");
        try (PrintWriter pw = new PrintWriter(rFile)){ 
            pw.write(filesData);
        }
    }

    public static void printData(StringBuilder allData, File allDataFile) throws IOException {
        try (FileWriter fw = new FileWriter(allDataFile,true)){ 
            fw.write(allData.toString());
        }
    }

    public static void createBoxPlotFile(InputData[] benchmarks, String prefix, 
                                         Map<AppName, List<InputData>> allValidBenchmarks, 
                                         List<Modes> modes ) {
        String boxPlot = RTools.createBoxPlotData(allValidBenchmarks, benchmarks, modes);
        String path = DATA_PATH+prefix+"/plot/";

        FileTools.createDirectory(path);

        File boxPlotFile = new File(path+"boxPlot.r");
        try(PrintWriter pw = new PrintWriter(boxPlotFile)){
            pw.write(boxPlot);
            pw.write(RTools.makeRBind(prefix,benchmarks, modes));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createGGPLOT(String prefix, boolean classic, InputData[] benchmarks, 
                                    boolean graphInfo, int nColumns, List<Modes> modes ) {

        String boxPlot = RTools.getGGPLOT(prefix, classic, getListBenchmarks(benchmarks), graphInfo, nColumns, modes);
        String path = DATA_PATH+prefix+"/plot/";

        FileTools.createDirectory(path);

        File boxPlotFile = new File(path+"ggPlot.r");
        try(PrintWriter pw = new PrintWriter(boxPlotFile)){
            pw.write(RTools.getFuncMean());
            pw.write(boxPlot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<String> getListBenchmarks(InputData[] values) {
        List<String> lista = new ArrayList<String>();
        for (InputData inputData : values) {
          lista.add(inputData.graphForm());
        }
        return lista;
      }    

}
