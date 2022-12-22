package utils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import enums.interfaces.InputData;
import enums.interfaces.Modes;
import enums.interfaces.AppName;

public class RTools {
    
    public final static String EOL = System.getProperty("line.separator");

    public static String putInRFormat(InputData benchmark, List<Double> execData, String dif) {
         String inRFormat = benchmark.shortVersion()+"_"+ dif  + " = c(" + 
                execData.stream().map(Object::toString).collect(Collectors.joining(",")) + ")";
        
        return inRFormat;    
    }

    public static String putInRFormat(Modes mode, InputData benchmark, AppName framework, List<Double> execData) {

        String varName = benchmark.shortVersion() + "_" + framework.toString()+ "_" + mode.getShortVersion();
        String inRFormat = varName + " = c("
                + execData.stream().map(Object::toString).collect(Collectors.joining(",")) + ")" + EOL;

       return inRFormat;
    }

    public static String printMeanAndSD(InputData[] benchmarks, Modes mode, AppName framework) {

        StringBuilder print = new StringBuilder();
        StringBuilder data = new StringBuilder();

        for (InputData benchmark : benchmarks) {
            String varName = benchmark.shortVersion() + "_" + framework.toString() + "_" +mode.getShortVersion();
            String dataBenchDataName = varName+"_Data";
            data.append(dataBenchDataName + " = c(");
            data.append("mean("+ varName + ")");
            data.append(",");
            data.append("sd("+ varName + ")");
            data.append(")");
            data.append(EOL);
            print.append(dataBenchDataName + EOL);
        }
        return data.append(print.toString()).toString();

    }

    public static String getBarplotR(InputData[] benchmarks, Modes mode, AppName framework) {


        StringBuilder data = new StringBuilder();

        for (InputData bench : benchmarks) {

            String varName = bench.shortVersion() + "_" + framework.toString() + "_" +mode.getShortVersion();

            data.append("barplot(").append(varName)
                .append(", main=paste('")
                .append(bench.toString()).append("-")
                .append(bench.getWorkload())
                .append(" avg:' ,mean(")
                .append(varName + ")))")
                .append(EOL);
        }
        return data.toString();
    }

    public static String makeRBind(String prefix, InputData[] benchmarks, List<Modes> modes){
        StringBuilder sb = new StringBuilder();
        StringBuilder output = new StringBuilder();
        for (Modes mode : modes) {
            sb.append(prefix).append("_").append("allData_").append(mode).append("<- rbind(");
            for (InputData benchmark : benchmarks) {
                sb.append("data_")
                .append(benchmark.shortVersion()+mode.getShortVersion())
                .append(",");
            }
            output.append(sb.substring(0,sb.length()-1)+")"+EOL);
            sb = new StringBuilder();
        }
        return output.toString();
    }
    
    public static String getParMfrow(InputData[] benchmarks) {

        int i = benchmarks.length;

        //if (mode == ModeEnum.MEMORY) 
        //    i = i * 3;

        int x = 0;
        int y = 1;

        while (x * y < i) {
            y++;
            if (x * y < i) 
                x++;
        }
        
        return //mode == ModeEnum.MEMORY 
                //? "par(mfrow=c(" + y + "," + x + "))" + EOL 
                //: 
                "par(mfrow=c(" + x + "," + y + "))" + EOL;
    }



    public static String createBoxPlotData(Map<AppName, List<InputData>>  allValidBench, 
                                            InputData[] benchmarks, List<Modes> modes) {
        StringBuilder sb = new StringBuilder();

        for (InputData bench : benchmarks) {
            List<AppName> validFrameworksForBench = getValidFrameworksForBench(allValidBench,bench);
            if(validFrameworksForBench.size() > 0){
                for (Modes mode : modes) {
                    String varName = bench.graphForm();
                    StringBuilder frameworks = new StringBuilder();

                    frameworks.append("framework")
                    .append(" <- c(");

                    for (AppName iAppNames : validFrameworksForBench) {
                        frameworks.append("rep(").append("\"").append(iAppNames).append("\"").append(", 30),");
                    }
                    StringBuilder temp = new StringBuilder(frameworks.substring(0,frameworks.length()-1)); 
                    temp.append(")")
                    .append(EOL);
                    
                    sb.append(temp);

                    sb.append("benchmark")
                    .append(" <- c(rep(\"")
                    .append(varName)
                    .append("\",")
                    .append(validFrameworksForBench.size())
                    .append("))")
                    .append(EOL);
        
                    sb.append(getValueData(validFrameworksForBench, bench, mode));

                    sb.append("data_"+bench.shortVersion()+mode.getShortVersion())
                    .append(" <- data.frame(")
                    .append("benchmark")
                    .append(",")
                    .append("framework,")
                    .append("value)")
                    .append(EOL);

                }
            }
        }
        
        
        return sb.toString();
    }

    private static String getValueData(List<AppName> validFrameworksForBench, InputData bench, Modes mode) {
        StringBuilder validBenchs = new StringBuilder();
        StringBuilder output = new StringBuilder();

        for (AppName iAppNames : validFrameworksForBench) {
            validBenchs.append(getValidData(iAppNames, bench, mode));
        }

        output.append("value")
                   .append(" <- c(")
                   .append(validBenchs.substring(0,validBenchs.length()-1))
                   .append(")").append(EOL);
        return output.toString();
    }

    private static List<AppName> getValidFrameworksForBench(Map<AppName, List<InputData>> allValidBench,
            InputData bench) {
                List<AppName> validFrameworks = new ArrayList<>();
                Set<AppName> mapFrameworks = allValidBench.keySet();
                for (AppName framework : mapFrameworks) {
                    if(allValidBench.get(framework).contains(bench)){
                       validFrameworks.add(framework);
                    }
                }
        return validFrameworks;
    }

    private static String getValidData(AppName framework, InputData bench, Modes mode) {
        return bench.shortVersion() + "_" + framework + "_" +mode.getShortVersion()+",";
    }

    public static String getGGPLOT(String prefix, boolean classic, 
                                   List<String> benchmarks, boolean graphInfo, 
                                   int nColumns, List<Modes> allModes){

        StringBuilder sb = new StringBuilder();

        double sizeFont = 11;
        double sizeMedian = 3.2;

        for (Modes iModeEnum : allModes) {
            String data = prefix+"_allData_"+iModeEnum;
            sb.append("ggplot(data=")
            .append(data)
            .append(", aes(x=framework,y=value, fill=framework))")
            .append("+")
            .append(EOL)
            .append("geom_bar(stat=\"summary\", fun=\"mean\") +")
            //.append("geom_boxplot(alpha=0.7) +")
            .append(EOL);

            if(graphInfo)
                sb.append("stat_summary(fun.data = fun_mean, geom=\"text\", vjust=1.5, size=").append(sizeMedian).append(")").append("+").append(EOL);
            
            if(classic)
                sb.append("theme_classic()  + ");

            sb.append("theme(text = element_text(size =").append(sizeFont).append(")) +")
            .append(EOL)
            .append(" ggh4x::facet_wrap2(");
            if(benchmarks != null){
                sb.append(EOL)
                .append("factor(benchmark, levels = c(")
                .append(getAllBenchmarks(benchmarks))
                .append(")) ~.");
            }
            else 
                sb.append("~benchmark");

            if(classic)
                sb.append(", scales = \"free_y\"");

            sb.append(", ncol=").append(nColumns).append(")")
            .append(EOL)
            .append("theme(axis.text.x = element_text(angle=90))")
            .append(EOL)
            .append(EOL);
        }
        return sb.toString();  
     }

     private static String getAllBenchmarks(List<String> benchmarks) {
        StringBuilder sb = new StringBuilder();
        for (String bench : benchmarks) {
            sb.append("\"")
            .append(bench)
            .append("\",");
        }
        return sb.substring(0, sb.length()-1);
    }

    public static String getFuncMean(){
        return "fun_mean <- function(x){return(data.frame(y=round(mean(x),2),label=round(mean(x,na.rm=T),2)))}"+EOL;
     }




}
