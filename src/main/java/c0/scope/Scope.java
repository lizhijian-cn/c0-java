package c0.scope;

import c0.entity.Entity;
import c0.entity.Function;
import c0.entity.Variable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class Scope {
    Map<String, Entity> entities = new LinkedHashMap<>();

    public void add(Entity entity) {
        var name = entity.getName();
        if (entities.containsKey(name)) {
            throw new RuntimeException(String.format("%s is already defined in the scope", name));
        }
        entities.put(name, entity);
    }

    public Optional<Entity> get(String name) {
//        if (!variables.containsKey(name)) {
//            throw new RuntimeException(String.format("variable %s is not defined in the scope", name));
        return Optional.ofNullable(entities.get(name));
    }


}
