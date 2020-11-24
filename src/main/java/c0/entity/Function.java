package c0.entity;

import c0.analyzer.Visitor;
import c0.ast.stmt.BlockNode;
import c0.type.Type;
import lombok.Getter;

import java.util.List;

@Getter
public class Function extends Entity {
    List<Variable> args;
    Type returnType;
    BlockNode blockStmt;

    public Function(String name, List<Variable> args, Type type, BlockNode blockStmt) {
        super(name);
        this.args = args;
        this.returnType = type;
        this.blockStmt = blockStmt;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
