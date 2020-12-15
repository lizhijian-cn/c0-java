package c0.entity;

import c0.ast.AbstractNode;
import lombok.Getter;
import lombok.Setter;

@Getter
public abstract class Entity extends AbstractNode {
    @Setter
    String name;
    @Setter
    int offset;

    Entity(String name) {
        this.name = name;
    }
}
