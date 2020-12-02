package c0.ast.stmt;

import c0.analyzer.Visitor;

public class EmptyNode extends StmtNode {
    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
