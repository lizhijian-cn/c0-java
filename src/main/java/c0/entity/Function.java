package c0.entity;

import c0.analyzer.Visitor;
import c0.ast.stmt.BlockNode;
import c0.type.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class Function extends Entity {
    Type returnType;
    List<Variable> args;
    List<Variable> locals;
    BlockNode blockStmt;

    public Function(String name, Type type, List<Variable> args, List<Variable> locals, BlockNode blockStmt) {
        super(name);
        this.returnType = type;
        this.args = args;
        this.locals = locals;
        this.blockStmt = blockStmt;
    }

    @Override
    public <T, E> E accept(Visitor<T, E> v) {
        return v.visit(this);
    }
}
