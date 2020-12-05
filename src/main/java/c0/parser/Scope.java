package c0.parser;

import c0.entity.Entity;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Getter
class Scope {
    Map<String, Entity> entities = new LinkedHashMap<>();

    void add(Entity entity) {
        var name = entity.getName();
        if (entities.containsKey(name)) {
            throw new RuntimeException(String.format("%s is already defined in the scope", name));
        }
        entities.put(name, entity);
    }

    Optional<Entity> get(String name) {
        return Optional.ofNullable(entities.get(name));
    }
}
