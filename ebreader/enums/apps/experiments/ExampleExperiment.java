package enums.apps.experiments;

import enums.interfaces.ExpData;

public enum ExampleExperiment implements ExpData {
    
    INPUT_FOO_1("foo","10", "foo10"),
    INPUT_FOO_2("foo","15", "foo15"),     
    INPUT_BOO_1("boo","10", "boo10");
    
    private final String value;
    private final String workload;
    private final String shortVersion;
   
    private ExampleExperiment(String value, String workload, String shortVersion){
        this.value = value;
        this.workload = workload;
        this.shortVersion = shortVersion;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public String shortVersion(){
        return shortVersion;
    }

    @Override
    public String getWorkload() {
        return workload;
    }

    @Override
    public ExpData[] getValues() {
        return values();
    }


}