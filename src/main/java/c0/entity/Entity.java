package c0.entity;

import c0.ast.AbstractNode;
import c0.ast.stmt.StmtNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.w3c.dom.Node;

@Getter
public abstract class Entity extends AbstractNode {
    String name;
    @Setter
    int offset;

    Entity(String name) {
        this.name = name;
    }
}
