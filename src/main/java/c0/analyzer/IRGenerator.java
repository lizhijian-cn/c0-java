package c0.analyzer;

import c0.analyzer.ir.IR;
import c0.analyzer.ir.IRFunction;
import c0.analyzer.ir.instruction.Instruction;
import c0.analyzer.ir.instruction.InstructionOp;
import c0.ast.AST;
import c0.ast.expr.*;
import c0.ast.stmt.BlockNode;
import c0.ast.stmt.EmptyNode;
import c0.ast.stmt.ExprStmtNode;
import c0.ast.stmt.ReturnNode;
import c0.entity.Entity;
import c0.entity.Function;
import c0.entity.Variable;

import java.nio.channels.NonReadableChannelException;
import java.util.ArrayList;
import java.util.List;

public class IRGenerator implements Visitor {
    ArrayList<Instruction> instructions;
    IR ir;
    IRFunction cur;

    void setId(List<? extends Entity> list, int id) {
        for (var e : list) {
            e.setId(id++);
        }
    }

    @Override
    public void visit(Variable variable) {
        var id = variable.getId();
        cur.add(switch (variable.getVarType()) {
            case GLOBAL -> new Instruction(InstructionOp.GLOBA, id);
            case LOCAL -> new Instruction(InstructionOp.LOCA, id);
            case ARG -> new Instruction(InstructionOp.ARGA, id);
            default -> throw new NonReadableChannelException();
        });
        variable.getExpr().accept(this);
        cur.add(new Instruction(InstructionOp.STORE));
    }

    @Override
    public void visit(Function function) {
        var func = new IRFunction();
        ir.add(func);
        cur = func;

        setId(function.getArgs(), 0);
        setId(function.getLocals(), 0);
        for (var local : function.getLocals()) {
            local.accept(this);
        }
    }

    @Override
    public void visit(AST node) {
        var start = new IRFunction();
        ir.add(start);
        cur = start;
        setId(node.getGlobals(), 0);
        for (var global : node.getGlobals()) {
            global.accept(this);
        }

        setId(node.getFunctions(), 1);
        for (var function : node.getFunctions()) {
            function.accept(this);
        }
    }

    @Override
    public void visit(AssignNode node) {

    }

    @Override
    public void visit(BinaryOpNode node) {
        node.getLeft().accept(this);
        node.getRight().accept(this);
        // op.f & op.i
        cur.add(new Instruction(switch (node.getOp()) {
            case PLUS -> InstructionOp.ADDI;
            case MINUS -> InstructionOp.SUBI;
            case MUL -> InstructionOp.MULI;
            case DIV -> InstructionOp.DIVI;
            default -> throw new NonReadableChannelException();
        }));
    }

    @Override
    public void visit(CastNode node) {
        // itof ftoi
    }

    @Override
    public void visit(FunctionCallNode node) {

    }

    @Override
    public void visit(LiteralNode node) {

    }

    @Override
    public void visit(UnaryOpNode node) {

    }

    @Override
    public void visit(VariableNode node) {

    }

    @Override
    public void visit(BlockNode node) {

    }

    @Override
    public void visit(EmptyNode node) {

    }

    @Override
    public void visit(ExprStmtNode node) {

    }

    @Override
    public void visit(ReturnNode node) {

    }
}
