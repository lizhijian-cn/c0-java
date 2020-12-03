package c0.entity;

import c0.analyzer.Visitor;
import lombok.Getter;

@Getter
public class StringVariable extends Entity {
    public StringVariable(String name) {
        super(name);
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
