package c0.ast;

import c0.entity.Entity;
import c0.visitor.Visitor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


@Getter
public class AST extends AbstractNode {
    List<Entity> entities = new ArrayList<>();

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
