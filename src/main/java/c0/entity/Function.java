package c0.entity;

import c0.analyzer.Visitor;
import c0.ast.stmt.BlockNode;
import c0.type.Type;
import lombok.Getter;

import java.util.List;

@Getter
public class Function extends Entity {
    Type returnType;
    List<Variable> params;
    List<Variable> locals;
    BlockNode blockStmt;

    public Function(String name, Type type, List<Variable> args, List<Variable> locals, BlockNode blockStmt) {
        super(name);
        this.returnType = type;
        this.params = args;
        this.locals = locals;
        this.blockStmt = blockStmt;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }
}
