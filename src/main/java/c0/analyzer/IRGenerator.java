package c0.analyzer;

import c0.ast.AST;
import c0.ast.expr.*;
import c0.ast.stmt.BlockNode;
import c0.ast.stmt.ExprStmtNode;
import c0.ast.stmt.ReturnNode;
import c0.entity.Function;
import c0.entity.Variable;
import c0.error.UnreachableException;
import c0.type.TypeVal;
import c0.util.RichDataOutputStream;
import lombok.Getter;
import lombok.SneakyThrows;

import java.nio.channels.NonReadableChannelException;

@Getter
public class IRGenerator implements Visitor {
    int magic;
    int version;

    int globalCount;
    int stringCount;
    int funcCount;

    RichDataOutputStream s;

    public IRGenerator(RichDataOutputStream s) {
        this.s = s;
        this.magic = 0x72303b3e;
        this.version = 1;
    }

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
        s.writeInt(function.getOffset() + globalCount + stringCount);
        s.writeInt(function.getReturnType().equals(TypeVal.VOID) ? 0 : 1);
        int paramCount = function.getParams().size();
        int localCount = function.getLocals().size();
        for (int i = 0; i < paramCount; i++) {
            function.getParams().get(i).setOffset(i);
        }
        for (int i = 0; i < localCount; i++) {
            function.getLocals().get(i).setOffset(i);
        }
        s.writeInt(paramCount);
        s.writeInt(localCount);

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
        s.writeInt(magic);
        s.writeInt(version);

        globalCount = node.getGlobals().size();
        stringCount = node.getStrings().size();
        funcCount = node.getFunctions().size();
        s.writeInt(globalCount + stringCount + funcCount);
        for (int i = 0; i < globalCount; i++) {
            node.getGlobals().get(i).setOffset(i);
            s.writeByte(0);
            s.writeInt(8);
            s.writeLong(0);
        }
        for (int i = 0; i < stringCount; i++) {
            var stringVariable = node.getStrings().get(i);
            stringVariable.setOffset(i + globalCount);

            s.writeByte(0);
            s.writeInt(stringVariable.getName().length());
            s.writeString(stringVariable.getName());
        }
        for (int i = 0; i < funcCount; i++) {
            var function = node.getFunctions().get(i);
            function.setOffset(i);
            s.writeByte(0);
            s.writeInt(function.getName().length());
            s.writeString(function.getName());
        }
        s.writeInt(funcCount);
        node.getFunctions().forEach(x -> x.accept(this));
    }

    @Override
    public void visit(AssignNode node) {
        node.getLhs().accept(this);
        node.getRhs().accept(this);
        s.write(Instruction.STORE64);
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
        s.write(Instruction.CALL, node.getFunction().getOffset());
    }

    @Override
    public void visit(STLFunctionCallNode node) {
        node.getArgs().forEach(x -> x.accept(this));
        s.write(Instruction.CALLNAME, node.getFunction().getOffset());
    }

    @Override
    public void visit(LiteralNode node) {
        switch (node.getType().getType()) {
            case UINT -> s.write(Instruction.PUSH, Long.parseLong(node.getValue()));
            case DOUBLE -> s.write(Instruction.PUSH, Double.doubleToLongBits(Double.parseDouble(node.getValue())));
            default -> throw new UnreachableException();
        }
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
        s.write(Instruction.LOAD64);
    }

    @Override
    public void visit(ExprStmtNode node) {
        node.getExpr().accept(this);
        if (!node.getExpr().getType().equals(TypeVal.VOID)) {
            s.write(Instruction.POPN, 1);
        }
    }

    @Override
    public void visit(StringNode node) {
        s.write(Instruction.GLOBA, node.getVariable().getOffset());
    }

    @Override
    public void visit(BlockNode node) {
        node.getStmts().forEach(x -> x.accept(this));
    }

    @Override
    public void visit(ReturnNode node) {
        if (node.getReturnValue().isPresent()) {
            s.write(Instruction.ARGA, 0);
            node.getReturnValue().get().accept(this);
            s.write(Instruction.STORE64);
        }
        s.write(Instruction.RET);
    }
}
