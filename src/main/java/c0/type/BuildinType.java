package c0.type;


public class Type {
    enum TypeVal {
        INT, VOID
    }

    ;
    TypeVal type;

    public Type(String value) {
        this.type = switch (value) {
            case "int" -> TypeVal.INT,
            case "void" -> TypeVal.VOID,
            default -> throw new RuntimeException("invalid type");
        };
    }
}
