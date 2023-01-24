package readfiles;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import enums.interfaces.InputData;
import enums.FileType;
import enums.Metrics;
import enums.interfaces.AppName;
import utils.FileTools;

public class ReadMemoryFiles {
   
    private enum MemoryMetrics implements Metrics{
        MEMORY("memory"),
        HEAP_SIZE("heap_size"),
        HEAP_ALLOC("heap_alloc"),
        HEAP_FREE("heap_free");
    
        private final String shortVersion;
        MemoryMetrics(String shortVersion){
            this.shortVersion = shortVersion;
        }
        public String shortVersion(){
            return shortVersion;
        }
    }

    public static Metrics[] metrics(){
        return MemoryMetrics.values();
    }

    private String path;
    private String device;
    private AppName framework;
    private int numberOfFiles;

    private Map<Integer, Map<Metrics, Double>> map = new HashMap<>();
    private Map<Metrics, Double> mapMetrics = new HashMap<>();


    public ReadMemoryFiles(String path, AppName framework, int numberOfFiles, String device){
        this.path = path;
        this.framework = framework;
        this.numberOfFiles = numberOfFiles;
        this.device = device;
    }

    public Map<Integer, Map<Metrics, Double>> readSingle(InputData benchmark) {
        int i = 1;
        for (; i < numberOfFiles + 1; i++){
            mapMetrics = new HashMap<>();
            executeReadFile(i, benchmark);
            map.put(i, mapMetrics);
        }
        return map;
    }

    private void executeReadFile(int fileNumber, InputData benchmark) {
        
      File file = new File(
                        FileTools.getFilePath(
                            FileType.MEMORY, path, 
                            framework, device, 
                            benchmark, fileNumber)); 

        final double fromKBtoMB = 1000.0;

        try ( Scanner scnr = new Scanner(file);){
            while (scnr.hasNextLine()) {
                String line = scnr.nextLine();
                if (line.contains("TOTAL  ")){
                    List<String> elems = 
                    List.of(line.split(" ")).stream().filter(
                        (str) -> !str.equals("")).collect(Collectors.toList());
                    mapMetrics.put(MemoryMetrics.MEMORY,Double.parseDouble(elems.get(1))/fromKBtoMB);
                    mapMetrics.put(MemoryMetrics.HEAP_FREE,Double.parseDouble(elems.get(elems.size()-1))/fromKBtoMB);
                    mapMetrics.put(MemoryMetrics.HEAP_ALLOC,Double.parseDouble(elems.get(elems.size()-2))/fromKBtoMB);
                    mapMetrics.put(MemoryMetrics.HEAP_SIZE,Double.parseDouble(elems.get(elems.size()-3))/fromKBtoMB);
                }
            }
        } catch (FileNotFoundException e) {
             e.printStackTrace();
        }  
    }
}