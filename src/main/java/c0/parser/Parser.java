package c0.parser;

import c0.ast.AST;
import c0.ast.expr.ExprNode;
import c0.ast.expr.FunctionCallNode;
import c0.ast.expr.LiteralNode;
import c0.ast.stmt.*;
import c0.entity.Function;
import c0.entity.Variable;
import c0.error.UnreachableException;
import c0.lexer.Lexer;
import c0.lexer.TokenType;
import c0.type.Type;
import c0.type.TypeVal;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class Parser {
    private final Lexer lexer;
    private final ExprParser exprParser;
    private final VariableChecker checker;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
        this.checker = new VariableChecker();
        this.exprParser = new ExprParser(lexer, checker);
    }

    public AST parse() {
        return parseProgram();
    }

    private AST parseProgram() {
        checker.push();
        boolean exit = false;
        while (!exit) {
            switch (lexer.peek().getTokenType()) {
                // add undefined function to scope before parsing body of function to support recursion
                // case FN -> checker.add(parseFunction());
                case FN -> parseFunction();
                case LET, CONST -> checker.add(parseVariable());
                default -> exit = true;
            }
        }
        lexer.expect(TokenType.EOF);

        var scope = checker.pop();
        var functions = new LinkedList<Function>();
        var globals = new ArrayList<Variable>();
        var strings = checker.getStrings();
        for (var entity : scope.getEntities().values()) {
            if (entity instanceof Variable variable) {
                variable.setVarType(Variable.VariableTypeOp.GLOBAL);
                globals.add(variable);
            }
            if (entity instanceof Function function) {
                functions.add(function);
            }
        }
        var main = functions.stream().filter(x -> x.getName().equals("main")).findAny().orElseThrow(() -> new RuntimeException("no main function"));
        var _startBlockStmt = new BlockNode(List.of(new ExprStmtNode(new FunctionCallNode("main", List.of(), main))));
        var _start = new Function("_start", new Type(TypeVal.VOID), List.of(), globals, _startBlockStmt);
        functions.addFirst(_start);
        return new AST(functions, globals, strings);
    }

    /**
     *
     */
    private void parseFunction() {
        lexer.expect(TokenType.FN);
        var name = lexer.expect(TokenType.IDENT).getValue();
        var function = new Function(name);
        checker.add(function);

        checker.push();

        lexer.expect(TokenType.L_PAREN);
        if (!lexer.check(TokenType.R_PAREN)) {
            checker.add(parseParam());
            while (lexer.test(TokenType.COMMA)) {
                checker.add(parseParam());
            }
        }
        lexer.expect(TokenType.R_PAREN);
        lexer.expect(TokenType.ARROW);
        var type = new Type(lexer.expect(TokenType.IDENT).getValue());

        var blockStmt = parseBlockStmt();
        var locals = new ArrayList<Variable>();
        var params = new ArrayList<Variable>();
        var scope = checker.pop();
        for (var entity : scope.getEntities().values()) {
            if (entity instanceof Variable variable) {
                switch (variable.getVarType()) {
                    case LOCAL -> locals.add(variable);
                    case ARG -> params.add(variable);
                    default -> throw new UnreachableException();
                }
            }
        }
        checker.getFunction(name).set(type, params, locals, blockStmt);
    }

    private Variable parseParam() {
        boolean isConst = false;
        if (lexer.test(TokenType.CONST)) {
            isConst = true;
        }
        var name = lexer.expect(TokenType.IDENT).getValue();
        lexer.expect(TokenType.COLON);
        var type = new Type(lexer.expect(TokenType.IDENT).getValue());
        var variable = new Variable(name, type, new LiteralNode(type), isConst);
        variable.setVarType(Variable.VariableTypeOp.ARG);
        return variable;
    }

    private Variable parseVariable() {
        boolean isConst;
        String name;
        Type type;
        ExprNode expr;
        if (lexer.test(TokenType.CONST)) {
            isConst = true;
            name = lexer.expect(TokenType.IDENT).getValue();
            lexer.expect(TokenType.COLON);
            type = new Type(lexer.expect(TokenType.IDENT).getValue());
            if (lexer.test(TokenType.ASSIGN)) {
                expr = parseExpr();
            } else {
                throw new RuntimeException("Constant must be initialized");
            }
        } else {
            isConst = false;
            lexer.expect(TokenType.LET);
            name = lexer.expect(TokenType.IDENT).getValue();
            lexer.expect(TokenType.COLON);
            type = new Type(lexer.expect(TokenType.IDENT).getValue());
            if (lexer.test(TokenType.ASSIGN)) {
                expr = parseExpr();
            } else {
                expr = new LiteralNode(type);
            }
        }
        lexer.expect(TokenType.SEMICOLON);
        return new Variable(name, type, expr, isConst);
    }

    private ExprNode parseExpr() {
        return exprParser.parse();
    }

    private BlockNode parseBlockStmt() {
        checker.push();
        lexer.expect(TokenType.L_BRACE);

        var stmts = new ArrayList<StmtNode>();
        boolean exit = false;
        while (!exit) {
            switch (lexer.peek().getTokenType()) {
                case SEMICOLON -> {
                    lexer.next();
                    stmts.add(new EmptyNode());
                }
                case LET, CONST -> checker.add(parseVariable());
                case L_BRACE -> {
                    var blockStmt = parseBlockStmt();
                    stmts.add(blockStmt);
                }
                case RETURN -> stmts.add(parseReturnStmt());
                case IF -> stmts.add(parseIfStmt());
                case WHILE -> {
                    lexer.next();
                    var cond = parseExpr();
                    var body = parseBlockStmt();
                    stmts.add(new WhileNode(cond, body));
                }
                case BREAK -> {
                    lexer.next();
                    lexer.expect(TokenType.SEMICOLON);
                    stmts.add(new BreakNode());
                }
                case CONTINUE -> {
                    lexer.next();
                    lexer.expect(TokenType.SEMICOLON);
                }
                case R_BRACE, EOF -> exit = true;
                default -> stmts.add(parseExprStmt());
            }
        }
        lexer.expect(TokenType.R_BRACE);

        var scope = checker.pop();
        for (var entity : scope.getEntities().values()) {
            entity.setName(LocalTime.now().toString());
            checker.add(entity);
        }
        return new BlockNode(stmts);
    }

    private ReturnNode parseReturnStmt() {
        lexer.expect(TokenType.RETURN);
        ExprNode returnValue = null;
        if (!lexer.test(TokenType.SEMICOLON)) {
            returnValue = parseExpr();
            lexer.expect(TokenType.SEMICOLON);
        }
        return new ReturnNode(Optional.ofNullable(returnValue));
    }

    // TODO try to parse expr statement, if success, drop it, otherwise, throw a exception
    private ExprStmtNode parseExprStmt() {
        ExprNode expr = parseExpr();
        lexer.expect(TokenType.SEMICOLON);
        return new ExprStmtNode(expr);
    }

    private IfNode parseIfStmt() {
        lexer.expect(TokenType.IF);
        var cond = parseExpr();
        var thenBody = parseBlockStmt();
        Optional<StmtNode> elseBody = Optional.empty();
        if (lexer.test(TokenType.ELSE)) {
            if (lexer.check(TokenType.IF)) {
                elseBody = Optional.of(parseIfStmt());
            } else {
                elseBody = Optional.of(parseBlockStmt());
            }
        }
        return new IfNode(cond, thenBody, elseBody);
    }
}
