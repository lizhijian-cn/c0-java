package c0.type;


import lombok.Getter;

@Getter
public class Type {
    TypeVal type;

    public Type(TypeVal type) {
        this.type = type;
    }
    public Type(String value) {
        this.type = switch (value) {
            case "int" -> TypeVal.UINT;
            case "void" -> TypeVal.VOID;
            case "double" -> TypeVal.DOUBLE;
            default -> throw new RuntimeException("invalid type");
        };
    }

    @Override
    public String toString() {
        return String.format("Type(%s)", type);
    }

}
