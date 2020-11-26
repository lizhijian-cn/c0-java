package c0.ast.stmt;

import c0.analyzer.Visitor;

public class EmptyNode extends StmtNode {
    @Override
    public <T, E> E accept(Visitor<T, E> v) {
        return v.visit(this);
    }
}
