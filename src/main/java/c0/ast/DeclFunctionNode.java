package c0.ast;

import c0.entity.Function;

public class DeclFunctionNode extends AbstractNode {
    String name;
    Function function;

    public DeclFunctionNode(String name, Function function) {
        this.name = name;
        this.function = function;
    }
}
