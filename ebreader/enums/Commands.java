package enums;

public enum Commands{
    BATTERY_STATS("batterystats"), MEM_INFO("meminfo"), PROC_STATS("procstats");
    final private String value;
    private Commands(String value){this.value = value;}
    @Override public String toString() {return value;}
}