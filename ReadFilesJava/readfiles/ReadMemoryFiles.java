package readfiles;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import enums.interfaces.InputData;
import enums.FileType;
import enums.interfaces.AppName;
import enums.interfaces.Modes;
import utils.FileTools;
import utils.RTools;

public class ReadMemoryFiles {
   
    private enum ModeEnumMemory implements Modes{
        MEMORY("mm"),
        HEAP_SIZE("hs"),
        HEAP_ALLOC("ha"),
        HEAP_FREE("hf");
    
        private final String shortVersion;
        ModeEnumMemory(String shortVersion){
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

    private List<Double> consumption = new ArrayList<>();
    private List<Double> heapSize = new ArrayList<>();
    private List<Double> heapAlloc = new ArrayList<>();
    private List<Double> heapFree = new ArrayList<>();

    private Map<Modes, String> map = new HashMap<>();

    public static List<? extends Modes> modes(){
        return List.of(ModeEnumMemory.values());
    }

    public ReadMemoryFiles(String path, AppName framework, int numberOfFiles, String device){
        this.path = path;
        this.framework = framework;
        this.numberOfFiles = numberOfFiles;
        this.device = device;
    }

    public Map<Modes, String> readAll(InputData[] benchmarkSet) {
        Arrays.asList(benchmarkSet).forEach(benchmark -> readSingle(benchmark));
        return map;
    }

    public void readSingle(InputData benchmark) {
        int i = numberOfFiles > 30 ? numberOfFiles-30 : 1;
        for (; i < numberOfFiles + 1; i++) 
            executeReadFile(i, benchmark);
        storeData(benchmark);
    }

    private void storeData(InputData benchmark) {
        mergeData(benchmark);
        clearData();
    }

    private void clearData() {
        consumption.clear();
        heapAlloc.clear();
        heapFree.clear();
        heapSize.clear();
    }

    private void mergeData(InputData benchmark) {
        map.merge(ModeEnumMemory.MEMORY,
                RTools.putInRFormat(ModeEnumMemory.MEMORY,benchmark, framework, consumption),
                (x,y) -> x+y);
        map.merge(ModeEnumMemory.HEAP_ALLOC,
                RTools.putInRFormat(ModeEnumMemory.HEAP_ALLOC,benchmark, framework, heapAlloc),
                (x,y) -> x+y);
        map.merge(ModeEnumMemory.HEAP_FREE,
                RTools.putInRFormat(ModeEnumMemory.HEAP_FREE,benchmark, framework, heapFree),
                (x,y) -> x+y);
        map.merge(ModeEnumMemory.HEAP_SIZE,
                RTools.putInRFormat(ModeEnumMemory.HEAP_SIZE, benchmark, framework, heapSize),
                (x,y) -> x+y);
    }

    private void executeReadFile(int fileNumber, InputData benchmark) {
        
      File file = new File(
                        FileTools.getFilePath(
                            FileType.MEMORY, path, 
                            framework, device, 
                            benchmark, fileNumber)); 

        final double fromKBtoMB = 1024.0;

        try ( Scanner scnr = new Scanner(file);){
            while (scnr.hasNextLine()) {
                String line = scnr.nextLine();
                if (line.contains("TOTAL  ")){
                    List<String> elems = 
                    List.of(line.split(" ")).stream().filter(
                        (str) -> !str.equals("")).collect(Collectors.toList());
                    consumption.add(Double.parseDouble(elems.get(1))/fromKBtoMB);
                    heapFree.add(Double.parseDouble(elems.get(elems.size()-1))/fromKBtoMB);
                    heapAlloc.add(Double.parseDouble(elems.get(elems.size()-2))/fromKBtoMB);
                    heapSize.add(Double.parseDouble(elems.get(elems.size()-3))/fromKBtoMB);
                }
            }
        } catch (FileNotFoundException e) {
             e.printStackTrace();
        }  
    }
}