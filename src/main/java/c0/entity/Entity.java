package c0.entity;

import c0.ast.AbstractNode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class Entity extends AbstractNode {
    String name;
}
