package c0.parser;

import c0.entity.Entity;
import c0.entity.Function;
import c0.entity.StringVariable;
import c0.entity.Variable;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

class VariableChecker {
    Deque<Scope> scopeStack;
    @Getter
    List<StringVariable> strings;

    VariableChecker() {
        this.scopeStack = new LinkedList<>();
        this.strings = new ArrayList<>();
    }

    private Scope top() {
        if (scopeStack.isEmpty()) {
            throw new RuntimeException("scope stack is empty");
        }
        return scopeStack.peekLast();
    }

    private Scope current() {
        if (scopeStack.isEmpty()) {
            throw new RuntimeException("scope stack is empty");
        }
        return scopeStack.peek();
    }

    void add(Entity entity) {
        current().add(entity);
    }

    void add(StringVariable string) {
        strings.add(string);
    }

    Function getFunction(String name) {
        var entity = top().get(name);
        if (entity.isPresent() && entity.get() instanceof Function function) {
            return function;
        } else {
            throw new RuntimeException(String.format("function %s is not defined in the scope", name));
        }
    }

    Variable getVariable(String name) {
        for (var scope : scopeStack) {
            var entity = scope.get(name);
            if (entity.isPresent() && entity.get() instanceof Variable variable) {
                return variable;
            }
        }
        throw new RuntimeException(String.format("variable %s is not defined in the scope", name));
    }

    void push() {
        scopeStack.push(new Scope());
    }

    Scope pop() {
        return scopeStack.pop();
    }
}
