package enums;

public enum DeviceNames {
    S6TABLITE("SM-P615"), S20FE4G("SM-G780G"), S20FE5G("SM-G781B"),TEST("TEST");
    final private String value;
    private DeviceNames(String value) {this.value = value;}
    @Override public String toString() {return value;}
}
