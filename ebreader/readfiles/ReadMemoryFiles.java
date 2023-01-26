package readfiles;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import enums.interfaces.ExpData;
import enums.Commands;
import enums.Metrics;
import enums.interfaces.AppName;

public class ReadMemoryFiles extends ReadFiles{
   
    private enum MemoryMetrics implements Metrics{
        MEMORY("memory"),
        HEAP_SIZE("heap_size"),
        HEAP_ALLOC("heap_alloc"),
        HEAP_FREE("heap_free");
    
        private final String value;
        MemoryMetrics(String value){
            this.value = value;
        }
        public String value(){
            return value;
        }
    }

    public static Metrics[] metrics(){
        return MemoryMetrics.values();
    }

    private Map<Integer, Map<Metrics, Double>> map = new HashMap<>();
    private Map<Metrics, Double> mapMetrics = new HashMap<>();


    public ReadMemoryFiles(AppName framework, Commands metricCMD){
        super(framework, metricCMD);
    }

    public Map<Integer, Map<Metrics, Double>> readSingle(ExpData benchmark) {
        int i = 1;
        for (; i < numberOfFiles + 1; i++){
            mapMetrics = new HashMap<>();
            executeReadFile(i, benchmark);
            map.put(i, mapMetrics);
        }
        return map;
    }

    private void executeReadFile(int fileNumber, ExpData benchmark) {

        final double fromKBtoMB = 1000.0;
        try ( Scanner scnr = new Scanner(getFile(benchmark, fileNumber));){
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