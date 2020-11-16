package c0.scope;

import c0.entity.Variable;

import java.util.LinkedHashMap;
import java.util.Map;

public class LocalScope extends Scope {
    Map<String, Variable> variables;
    Scope parent;
    public LocalScope(Scope parent) {
        this.variables = new LinkedHashMap<>();
        this.parent = parent;
    }
}
