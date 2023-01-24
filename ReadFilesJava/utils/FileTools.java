package utils;
import enums.interfaces.InputData;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import enums.FileType;
import enums.Metrics;
import enums.interfaces.AppName;

public class FileTools {
    
    public final static String DATA_PATH = "CSV/apps/";

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

    public static void outputCSV(String device, List<Metrics> metrics, InputData input, 
                                AppName framework,
                                Map<Integer, Map<Metrics, Double>> mapData) throws IOException {

        String path = FileTools.DATA_PATH+"/"+framework+"/";

        createDirectory(path);
        File csvFile = new File(path+device+"-"+framework+"-"+input.shortVersion()+".csv");

        try (PrintWriter pw = new PrintWriter(csvFile)){
            pw.write("nExec,"
                +metrics.stream()
                    .map(m -> m.shortVersion())
                    .collect(Collectors.joining(", "))
                +"\n");
            for(Integer i : mapData.keySet()){
                StringBuilder sb = new StringBuilder();
                sb.append(i+",");
                for(Metrics m : metrics){
                    sb.append(mapData.get(i).get(m)+",");
                }
                sb.append("\n");
                pw.write(sb.toString());
            }
        }
    }
}
