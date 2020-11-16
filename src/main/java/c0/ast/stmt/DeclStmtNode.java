package c0.ast.stmt;

import c0.entity.Variable;

public class DeclStmtNode extends StmtNode {
    String name;
    Variable variable;

    public DeclStmtNode(String name, Variable variable) {
        this.name = name;
        this.variable = variable;
    }
}
