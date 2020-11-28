package c0.ast;

import c0.analyzer.Visitor;

public abstract class AbstractNode {
    public abstract <T> T accept(Visitor<T> v);
}
