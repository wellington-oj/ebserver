package enums.apps.test;

import enums.interfaces.AppName;

public enum TestNames implements AppName {

    JAVA("com.example.cadprodutos");

    private final String appName;
    private String appID;

    private TestNames(String appName) {
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