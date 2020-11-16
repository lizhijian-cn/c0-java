package c0.scope;

import c0.entity.Entity;

import java.util.LinkedHashMap;
import java.util.Map;

public class TopLevelScope extends Scope {
    Map<String, Entity> entities;
    public TopLevelScope() {
        this.entities = new LinkedHashMap<>();
    }
}
