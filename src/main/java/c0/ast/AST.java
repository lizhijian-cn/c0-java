package c0.ast;

import c0.entity.Entity;

import java.util.ArrayList;
import java.util.List;


public class AST extends AbstractNode {
    List<Entity> entities;

    public AST() {
        this.entities = new ArrayList<>();
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }
}
