package c0.type;


import lombok.Getter;

@Getter
public class Type {
    TypeVal type;

    public Type(String value) {
        this.type = switch (value) {
            case "int" -> TypeVal.INT;
            case "void" -> TypeVal.VOID;
            case "double" -> TypeVal.DOUBLE;
            default -> throw new RuntimeException("invalid type");
        };
    }

    @Override
    public String toString() {
        return String.format("Type(%s)", type);
    }

    public enum TypeVal {
        INT, VOID, DOUBLE
    }
}
