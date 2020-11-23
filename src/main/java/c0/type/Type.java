package c0.type;


import lombok.Getter;

@Getter
public class Type {
    public enum TypeVal {
        INT, VOID
    }
    TypeVal type;

    public Type(String value) {
        this.type = switch (value) {
            case "int" -> TypeVal.INT;
            case "void" -> TypeVal.VOID;
            default -> throw new RuntimeException("invalid type");
        };
    }

    @Override
    public String toString() {
        return String.format("Type(%s)", type);
    }
}
