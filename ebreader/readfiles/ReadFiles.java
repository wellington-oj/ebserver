package readfiles;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import enums.Commands;
import enums.Metrics;
import enums.interfaces.AppName;
import enums.interfaces.ExpData;
import main.Main;
import utils.FileTools;

public abstract class ReadFiles {
    
    protected Map<Integer, Map<Metrics, Double>> map = new HashMap<>();
    protected Map<Metrics, Double> mapMetrics;

    private String path = Main.PATH_TO_FILES;
    private String device = Main.DEVICE_NAME;
    private Commands metricCMD;

    protected int numberOfFiles = Main.NUM_FILES;
    protected AppName framework;

    public ReadFiles(AppName framework, Commands metricCMD){
        this.metricCMD = metricCMD;
        this.framework = framework;
    }

    public File getFile(ExpData exp, int i){
        return new File(FileTools.getFilePath(metricCMD, path, framework, device, exp, i));
    }
}