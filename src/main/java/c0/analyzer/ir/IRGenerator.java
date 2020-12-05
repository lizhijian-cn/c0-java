package c0.analyzer.ir;

import c0.analyzer.Visitor;
import c0.ast.AST;
import c0.ast.expr.*;
import c0.ast.stmt.*;
import c0.entity.Function;
import c0.entity.Variable;
import c0.error.UnreachableException;
import c0.type.TypeVal;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.OutputStream;
import java.nio.channels.NonReadableChannelException;

@Getter
public class IRGenerator implements Visitor {
    int magic;
    int version;

    int globalCount;
    int stringCount;
    int funcCount;

    ControlFlow flow;
    RichDataOutputStream s;

    public IRGenerator(OutputStream out) {
        this.s = new RichDataOutputStream(out);
        this.flow = new ControlFlow(s);
        this.magic = 0x72303b3e;
        this.version = 1;
    }

    void pointer(Variable variable) {
        var off = variable.getOffset();
        flow.write(switch (variable.getVarType()) {
            case LOCAL -> Instruction.LOCA;
            case ARG -> Instruction.ARGA;
            case GLOBAL -> Instruction.GLOBA;
        }, off);
    }

    @Override
    public void visit(Variable variable) {
        if (variable.getType().equals(TypeVal.STRING)) {
            return;
        }

        pointer(variable);
        variable.getExpr().accept(this);
        flow.write(Instruction.STORE64);
    }

    @Override
    public void visit(Function function) {
        s.writeInt(function.getOffset() + globalCount + stringCount);
        s.writeInt(function.getReturnType().equals(TypeVal.VOID) ? 0 : 1);
        int paramCount = function.getParams().size();
        int localCount = function.getLocals().size();
        for (int i = 0; i < paramCount; i++) {
            function.getParams().get(i).setOffset(i + 1);
        }
        for (int i = 0; i < localCount; i++) {
            function.getLocals().get(i).setOffset(i);
        }
        s.writeInt(paramCount);
        s.writeInt(localCount);

        function.getLocals().forEach(x -> x.accept(this));
        function.getBlockStmt().accept(this);
        if (!function.getName().equals("_start") && function.getReturnType().equals(TypeVal.VOID)) {
            flow.write(Instruction.RET);
        }
        s.writeInt(flow.getLine());
        flow.flush();
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
        pointer(node.getLhs().getVariable());
        node.getRhs().accept(this);
        flow.write(Instruction.STORE64);
    }

    @Override
    public void visit(BinaryOpNode node) {
        node.getLeft().accept(this);
        node.getRight().accept(this);

        if (node.getType().equals(TypeVal.UINT)) {
            switch (node.getOp()) {
                case PLUS -> flow.write(Instruction.ADDI);
                case MINUS -> flow.write(Instruction.SUBI);
                case MUL -> flow.write(Instruction.MULI);
                case DIV -> flow.write(Instruction.DIVI);
                case LT -> {
                    flow.write(Instruction.CMPI);
                    flow.write(Instruction.SETLT);
                }
                case GT -> {
                    flow.write(Instruction.CMPI);
                    flow.write(Instruction.SETGT);
                }
                case LE -> {
                    flow.write(Instruction.CMPI);
                    flow.write(Instruction.SETGT);
                    flow.write(Instruction.NOT);
                }
                case GE -> {
                    flow.write(Instruction.CMPI);
                    flow.write(Instruction.SETLT);
                    flow.write(Instruction.NOT);
                }
                case EQ -> {
                    flow.write(Instruction.CMPI);
                    flow.write(Instruction.NOT);
                }
                case NEQ -> flow.write(Instruction.CMPI);
                default -> throw new NonReadableChannelException();
            }
        } else {
            switch (node.getOp()) {
                case PLUS -> flow.write(Instruction.ADDF);
                case MINUS -> flow.write(Instruction.SUBF);
                case MUL -> flow.write(Instruction.MULF);
                case DIV -> flow.write(Instruction.DIVF);
                case LT -> {
                    flow.write(Instruction.CMPF);
                    flow.write(Instruction.SETLT);
                }
                case GT -> {
                    flow.write(Instruction.CMPF);
                    flow.write(Instruction.SETGT);
                }
                case LE -> {
                    flow.write(Instruction.CMPF);
                    flow.write(Instruction.SETGT);
                    flow.write(Instruction.NOT);
                }
                case GE -> {
                    flow.write(Instruction.CMPF);
                    flow.write(Instruction.SETLT);
                    flow.write(Instruction.NOT);
                }
                case EQ -> {
                    flow.write(Instruction.CMPF);
                    flow.write(Instruction.NOT);
                }
                case NEQ -> flow.write(Instruction.CMPF);
                default -> throw new NonReadableChannelException();
            }
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
            flow.write(Instruction.ITOF);
        } else {
            flow.write(Instruction.FTOI);
        }
    }

    @Override
    public void visit(FunctionCallNode node) {
        if (!node.getType().equals(TypeVal.VOID)) {
            flow.write(Instruction.STACKALLOC, 1);
        }
        node.getArgs().forEach(x -> x.accept(this));
        flow.write(Instruction.CALL, node.getFunction().getOffset());
    }

    @Override
    public void visit(STLFunctionCallNode node) {
        if (!node.getType().equals(TypeVal.VOID)) {
            flow.write(Instruction.STACKALLOC, 1);
        }
        node.getArgs().forEach(x -> x.accept(this));
        flow.write(Instruction.CALLNAME, node.getFunction().getOffset());
    }

    @Override
    public void visit(LiteralNode node) {
        switch (node.getType().getType()) {
            case UINT -> flow.write(Instruction.PUSH, Long.parseLong(node.getValue()));
            case DOUBLE -> flow.write(Instruction.PUSH, Double.doubleToLongBits(Double.parseDouble(node.getValue())));
            default -> throw new UnreachableException();
        }
    }

    @Override
    public void visit(UnaryOpNode node) {
        node.getExpr().accept(this);
        if (node.getType().equals(TypeVal.UINT)) {
            flow.write(Instruction.NEGI);
        } else {
            flow.write(Instruction.NEGF);
        }
    }

    @Override
    public void visit(VariableNode node) {
        pointer(node.getVariable());
        flow.write(Instruction.LOAD64);
    }

    @Override
    public void visit(ExprStmtNode node) {
        node.getExpr().accept(this);
        if (!node.getExpr().getType().equals(TypeVal.VOID)) {
            flow.write(Instruction.POPN, 1);
        }
    }

    @Override
    public void visit(StringNode node) {
        flow.write(Instruction.PUSH, (long) node.getVariable().getOffset());
    }

    @Override
    public void visit(BlockNode node) {
        node.getStmts().forEach(x -> x.accept(this));
    }

    @Override
    public void visit(ReturnNode node) {
        if (node.getReturnValue().isPresent()) {
            flow.write(Instruction.ARGA, 0);
            node.getReturnValue().get().accept(this);
            flow.write(Instruction.STORE64);
        }
        flow.write(Instruction.RET);
    }

    @Override
    public void visit(IfNode node) {
        var thenLabel = new Label(flow);
        var elseLabel = new Label(flow);
        var endLabel = new Label(flow);
        node.getCond().accept(this);
        flow.getJumpers().add(new CondJump(flow, thenLabel, elseLabel));
        thenLabel.set();
        node.getThenBody().accept(this);
        flow.getJumpers().add(new UnCondJump(flow, endLabel));
        elseLabel.set();
        node.getElseBody().ifPresent(x -> x.accept(this));
        endLabel.set();
    }

    @Override
    public void visit(WhileNode node) {
        var beginLabel = new Label(flow);
        var bodyLabel = new Label(flow);
        var endLabel = new Label(flow);

        flow.getBreakStack().push(endLabel);
        flow.getContinueStack().push(beginLabel);

        beginLabel.set();
        node.getCond().accept(this);
        flow.getJumpers().add(new CondJump(flow, bodyLabel, endLabel));
        bodyLabel.set();
        node.getBody().accept(this);
        flow.getJumpers().add(new UnCondJump(flow, beginLabel));
        endLabel.set();

        flow.getBreakStack().pop();
        flow.continueStack.pop();
    }

    @Override
    public void visit(BreakNode node) {
        flow.getJumpers().add(new UnCondJump(flow, flow.getBreakStack().element()));
    }

    @Override
    public void visit(ContinueNode node) {
        flow.getJumpers().add(new UnCondJump(flow, flow.getContinueStack().element()));
    }
}
