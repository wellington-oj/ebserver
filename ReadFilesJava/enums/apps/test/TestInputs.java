package enums.apps.test;

import enums.interfaces.InputData;

public enum TestInputs implements InputData {

    LOCAL_LOGIN_1000("localLogin","1000", "ll1000"),
    LOCAL_LOGIN_ENC_1000("localLoginEnc","1000", "lle1000");

    private final String value;
    private final String workload;    
    private final String shortVersion;

    private TestInputs( String value, String workload, String shortVersion) {
        this.value = value;
        this.workload = workload;
        this.shortVersion = shortVersion;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public String shortVersion() {
        return shortVersion;
    }

    @Override
    public String getWorkload() {
        return workload;
    }

    @Override
    public String graphForm(){
        return value;
    }

}