package c0.parser.scope;

import c0.entity.Entity;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Getter
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
        return Optional.ofNullable(entities.get(name));
    }
}
