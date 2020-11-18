package c0.ast.stmt;

import java.util.List;

public class BlockNode extends StmtNode {
    List<StmtNode> stmts;
    public BlockNode(List<StmtNode> stmts) {
        this.stmts = stmts;
    }
}

