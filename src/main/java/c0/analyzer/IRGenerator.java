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
import c0.entity.Function;
import c0.entity.Variable;
import c0.type.TypeVal;

import java.nio.channels.NonReadableChannelException;
import java.util.ArrayList;
import java.util.List;

public class IRGenerator implements Visitor<List<Instruction>> {
    IR ir;

    @Override
    public List<Instruction> visit(Variable variable) {
        var id = variable.getId();
        var instructions = new ArrayList<Instruction>();
        instructions.add(switch (variable.getVarType()) {
            case GLOBAL -> new Instruction(InstructionOp.GLOBA, id);
            case LOCAL -> new Instruction(InstructionOp.LOCA, id);
            case ARG -> new Instruction(InstructionOp.ARGA, id);
        });
        instructions.addAll(variable.getExpr().accept(this));
        instructions.add(new Instruction(InstructionOp.STORE));
        return instructions;
    }

    @Override
    public List<Instruction> visit(Function function) {
        var func = new IRFunction();
        ir.add(func);

        function.getLocals().forEach(x -> func.add(x.accept(this)));
        function.getParams().forEach(x -> func.add(x.accept(this)));
        return null;
    }

    @Override
    public List<Instruction> visit(AST node) {
        var start = new IRFunction();
        ir.add(start);

        node.getGlobals().forEach(x -> start.add(x.accept(this)));
        node.getFunctions().forEach(x -> x.accept(this));
        return null;
    }

    @Override
    public List<Instruction> visit(AssignNode node) {
        var instructions = new ArrayList<Instruction>();
        instructions.addAll(node.getLhs().accept(this));
        instructions.addAll(node.getRhs().accept(this));
        instructions.add(new Instruction(InstructionOp.STORE));
        return instructions;
    }

    @Override
    public List<Instruction> visit(BinaryOpNode node) {
        var instructions = new ArrayList<Instruction>();
        instructions.addAll(node.getLeft().accept(this));
        instructions.addAll(node.getRight().accept(this));

        if (node.getType().equals(TypeVal.UINT)) {
            instructions.add(new Instruction(switch (node.getOp()) {
                case PLUS -> InstructionOp.ADDI;
                case MINUS -> InstructionOp.SUBI;
                case MUL -> InstructionOp.MULI;
                case DIV -> InstructionOp.DIVI;
                default -> throw new NonReadableChannelException();
            }));
        } else {
            instructions.add(new Instruction(switch (node.getOp()) {
                case PLUS -> InstructionOp.ADDF;
                case MINUS -> InstructionOp.SUBF;
                case MUL -> InstructionOp.MULF;
                case DIV -> InstructionOp.DIVF;
                default -> throw new NonReadableChannelException();
            }));
        }
        return instructions;
    }

    @Override
    public List<Instruction> visit(CastNode node) {
        return null;
    }

    @Override
    public List<Instruction> visit(FunctionCallNode node) {
        return null;
    }

    @Override
    public List<Instruction> visit(LiteralNode node) {
        return null;
    }

    @Override
    public List<Instruction> visit(UnaryOpNode node) {
        return null;
    }

    @Override
    public List<Instruction> visit(VariableNode node) {
//        var id = variable.getId();
//        cur.add(switch (variable.getVarType()) {
//            case GLOBAL -> new Instruction(InstructionOp.GLOBA, id);
//            case LOCAL -> new Instruction(InstructionOp.LOCA, id);
//            case ARG -> new Instruction(InstructionOp.ARGA, id);
//            default -> throw new NonReadableChannelException();
//        });
//        variable.getExpr().accept(this);
//        cur.add(new Instruction(InstructionOp.STORE));
        return null;
    }

    @Override
    public List<Instruction> visit(BlockNode node) {
        return null;
    }

    @Override
    public List<Instruction> visit(EmptyNode node) {
        return null;
    }

    @Override
    public List<Instruction> visit(ExprStmtNode node) {
        return null;
    }

    @Override
    public List<Instruction> visit(ReturnNode node) {
        return null;
    }
}
