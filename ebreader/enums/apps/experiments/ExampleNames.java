package enums.apps.experiments;

import enums.interfaces.AppName;

public enum ExampleNames implements AppName {

    APP_1("com.example.starter"), 
    APP_2("com.android.starter");

    private final String appName;
    private String appID;

    private ExampleNames(String appName) {
        this.appName = appName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }

    public String getAppID() {
        return appID;
    }
}