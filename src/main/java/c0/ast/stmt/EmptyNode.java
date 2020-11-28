package c0.ast.stmt;

import c0.analyzer.Visitor;

public class EmptyNode extends StmtNode {
    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }
}
