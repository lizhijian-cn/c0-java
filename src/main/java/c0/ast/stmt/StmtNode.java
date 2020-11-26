package c0.ast.stmt;

import c0.analyzer.Visitor;
import c0.ast.AbstractNode;

public abstract class StmtNode extends AbstractNode {
    public abstract <T, E> E accept(Visitor<T, E> v);
}

