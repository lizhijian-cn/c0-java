package c0.scope;

import c0.entity.Entity;
import c0.entity.Function;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Deprecated
public class TopLevelScope extends Scope {
    Map<String, Function> functions;

    public TopLevelScope() {
        this.functions = new LinkedHashMap<>();
    }

    public Optional<Function> getFunction(String name) {
        if (!functions.containsKey(name)) {
            throw new RuntimeException(String.format("function %s is not defined in the scope", name));
        }
        return Optional.ofNullable(functions.get(name));
    }

}
