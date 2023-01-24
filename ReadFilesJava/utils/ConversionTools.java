package utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

import main.Main;

public class ConversionTools{

    public static final int PRECISION = Main.DOUBLE_PRECISION;

    public static double round(double value, int places) {
        if (places < 0)
            throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        return bd.setScale(places, RoundingMode.HALF_UP).doubleValue();
    }


    public static Double fromMAHtoJOULE(String consumption, double voltage) {
        return round((Double.parseDouble(consumption) * 3.6 * voltage), PRECISION);
    }

    public static  Double fromStringToSec(String timeExec) {

        if(!timeExec.contains("s "))
            return round((Double.parseDouble(timeExec) / 1000), PRECISION);

        double  min = 0.0, sec = 0.0, mil = 0.0; 
        String[] time = timeExec.split("s ");
        mil = Double.parseDouble(time[1]);
        if (time[0].contains("m ")) {
            String[] minAndSec = time[0].split("m ");
            sec = Double.parseDouble(minAndSec[1]);
            min = Double.parseDouble(minAndSec[0]);
        } else 
            sec = Double.parseDouble(time[0]);
        
        return round(min * 60 + sec + (mil / 1000), PRECISION);
    }

    public static Double memoryInMB(String data) {
        if(data.contains("MB"))
            return Double.parseDouble(data.split("MB")[0]);
        else if(data.contains("GB"))
            return Double.parseDouble(data.split("GB")[0])*1000;
        else if(data.contains("KB"))
            return Double.parseDouble(data.split("KB")[0])/1000;
        else
            return null;
    }
}