package c0.entity;

import c0.ast.AbstractNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
public abstract class Entity extends AbstractNode {
    String name;
    @Setter
    int id;

    Entity(String name) {
        this.name = name;
    }
}
