package c0.analyzer;

import c0.analyzer.ir.IR;
import c0.analyzer.ir.IRFunction;
import c0.analyzer.ir.instruction.Instruction;
import c0.ast.AST;
import c0.ast.expr.*;
import c0.ast.stmt.BlockNode;
import c0.ast.stmt.EmptyNode;
import c0.ast.stmt.ExprStmtNode;
import c0.ast.stmt.ReturnNode;
import c0.entity.Function;
import c0.entity.Variable;
import c0.error.UnreachableException;
import c0.type.Type;
import c0.type.TypeVal;
import c0.util.RichDataOutputStream;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;

import java.nio.channels.NonReadableChannelException;
import java.util.List;

@Getter
@AllArgsConstructor
public class IRGenerator implements Visitor {
    IR ir;
    int globalNumberCount;
    RichDataOutputStream s;

    @Override
    public void visit(Variable variable) {
        if (variable.getType().equals(TypeVal.STRING)) {
            return;
        }
        var off = variable.getOffset();
        s.write(switch (variable.getVarType()) {
            case LOCAL -> Instruction.LOCA;
            case ARG -> Instruction.ARGA;
            case GLOBAL -> Instruction.GLOBA;
        }, off);
        variable.getExpr().accept(this);
        s.write(Instruction.STORE64);
    }

    @Override
    public void visit(Function function) {
        s.writeInt(globalNumberCount + function.getOffset());
        s.writeInt(function.getReturnType().equals(TypeVal.VOID) ? 0 : 1);
        s.writeInt(function.getParams().size());
        s.writeInt(function.getLocals().size());

        s.setCache();
        function.getLocals().forEach(x -> x.accept(this));
        function.getBlockStmt().accept(this);
        int n = s.cancelCache();
        byte[] cache = s.getBytes();
        s.writeInt(n);
        s.writeBytes(cache);
    }

    @Override
    @SneakyThrows
    public void visit(AST node) {
        s.writeInt(ir.getMagic());
        s.writeInt(ir.getVersion());
        globalNumberCount = node.getGlobals().size();
        s.writeInt(globalNumberCount + node.getFunctions().size());
        for (Variable variable : node.getGlobals()) {
            s.writeByte(0);
            switch (variable.getType().getType()) {
                case STRING -> {
                    if (variable.getExpr() instanceof LiteralNode literal && literal.getType().equals(TypeVal.STRING)) {
                        var string = (String) literal.getValue();
                        s.writeInt(string.length());
                        s.writeString(string);
                    }
                }
                case UINT, DOUBLE -> {
                    s.writeInt(8);
                    s.writeLong(0);
                }
                default -> throw new UnreachableException();
            }
        }
        node.getFunctions().add(new Function("_start", new Type(TypeVal.VOID), List.of(), List.of(), null));
        for (Function function : node.getFunctions()) {
            s.writeByte(0);
            s.writeInt(function.getName().length());
            s.writeString(function.getName());
        }

        node.getFunctions().forEach(x -> x.accept(this));
    }

    @Override
    public void visit(AssignNode node) {
        var instructions = new Arrayvoid();
        instructions.addAll(node.getLhs().accept(this));
        instructions.addAll(node.getRhs().accept(this));
        instructions.add(new Instruction(Instruction.STORE));
    }

    @Override
    public void visit(BinaryOpNode node) {
        node.getLeft().accept(this);
        node.getRight().accept(this);

        if (node.getType().equals(TypeVal.UINT)) {
            s.write(switch (node.getOp()) {
                case PLUS -> Instruction.ADDI;
                case MINUS -> Instruction.SUBI;
                case MUL -> Instruction.MULI;
                case DIV -> Instruction.DIVI;
                default -> throw new NonReadableChannelException();
            });
        } else {
            s.write(switch (node.getOp()) {
                case PLUS -> Instruction.ADDF;
                case MINUS -> Instruction.SUBF;
                case MUL -> Instruction.MULF;
                case DIV -> Instruction.DIVF;
                default -> throw new NonReadableChannelException();
            });
        }
    }

    @Override
    public void visit(CastNode node) {
        node.getExpr().accept(this);
        var type = node.getExpr().getType();
        var castType = node.getCastType();
        if (type.equals(castType)) {
            return;
        }
        // int as double
        if (type.equals(TypeVal.UINT)) {
            s.write(Instruction.ITOF);
        } else {
            s.write(Instruction.FTOI);
        }
    }

    @Override
    public void visit(FunctionCallNode node) {
        if (!node.getType().equals(TypeVal.VOID)) {
            s.write(Instruction.STACKALLOC, 1);
        }
        node.getArgs().forEach(x -> x.accept(this));
        // TODO CALL
    }

    @Override
    public void visit(LiteralNode node) {
        var instruction = switch (node.getType().getType()) {
            case UINT -> new Instruction(Instruction.PUSH, (long) node.getValue());
            case DOUBLE -> new Instruction(Instruction.PUSH, (long) node.getValue());
            default -> throw new UnreachableException();
        };
    }

    @Override
    public void visit(UnaryOpNode node) {
        node.getExpr().accept(this);
        if (node.getType().equals(TypeVal.UINT)) {
            s.write(Instruction.NEGI);
        } else {
            s.write(Instruction.NEGF);
        }
    }

    @Override
    public void visit(VariableNode node) {
        var variable = node.getVariable();
        var off = variable.getOffset();

        s.write(switch (variable.getVarType()) {
            case LOCAL -> Instruction.LOCA;
            case ARG -> Instruction.ARGA;
            case GLOBAL -> Instruction.GLOBA;
        }, off);
        instructions.add(new Instruction(Instruction.LOAD));
    }

    @Override
    public void visit(BlockNode node) {
        node.getStmts().forEach(x -> x.accept(this));
    }

    @Override
    public void visit(ExprStmtNode node) {
    }

    @Override
    public void visit(ReturnNode node) {
        if (node.getReturnValue().isPresent()) {
            node.getReturnValue().get().accept(this);
            s.write(Instruction.STORE64);
        }
        s.write(Instruction.RET);
    }
}
