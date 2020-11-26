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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof TypeVal typeVal) return type == typeVal;
        if (o instanceof Type type2) return type == type2.type;
        return false;
    }

    @Override
    public int hashCode() {
        return type != null ? type.hashCode() : 0;
    }

    @Override
    public String toString() {
        return String.format("Type(%s)", type);
    }

}
