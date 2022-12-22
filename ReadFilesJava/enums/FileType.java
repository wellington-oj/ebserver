package enums;

public enum FileType{
    ENERGY("energy"), MEMORY("memory"), DATA("data");
    final private String value;
    private FileType(String value){this.value = value;}
    @Override public String toString() {return value;}
}