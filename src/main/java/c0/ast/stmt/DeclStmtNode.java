package c0.ast.stmt;

import c0.entity.Variable;
import c0.visitor.Visitor;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DeclStmtNode extends StmtNode {
    String name;
    Variable variable;

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
