package enums;

public enum DeviceNames {
    S20FE4G("SM-G780G"),TEST("TEST");
    final private String value;
    private DeviceNames(String value) {this.value = value;}
    @Override public String toString() {return value;}
}
