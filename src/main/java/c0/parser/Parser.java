package c0.parser;

import c0.ast.AST;
import c0.ast.expr.ExprNode;
import c0.ast.expr.LiteralNode;
import c0.ast.stmt.*;
import c0.entity.Function;
import c0.entity.Variable;
import c0.lexer.Lexer;
import c0.lexer.TokenType;
import c0.type.Type;

import java.util.ArrayList;

public class Parser {
    Lexer lexer;
    ExprParser exprParser;
    VariableChecker checker;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
        this.checker = new VariableChecker();
        this.exprParser = new ExprParser(lexer, checker);
    }

    public AST parse() {
        return parseProgram();
    }

    AST parseProgram() {
        checker.push();
        boolean exit = false;
        while (!exit) {
            switch (lexer.peek().getTokenType()) {
                case FN -> checker.add(parseFunction());
                case LET, CONST -> checker.add(parseVariable());
                default -> exit = true;
            }
        }
        lexer.expect(TokenType.EOF);

        var scope = checker.pop();
        var functions = new ArrayList<Function>();
        var variables = new ArrayList<Variable>();
        for (var entity : scope.getEntities().values()) {
            if (entity instanceof Variable variable) {
                variable.setGlobal(true);
                variables.add(variable);
            }
            if (entity instanceof Function function) {
                functions.add(function);
            }
        }
        return new AST(functions, variables);
    }

    // TODO: now only parse main function
    Function parseFunction() {
        lexer.expect(TokenType.FN);
        lexer.expect(TokenType.IDENT);
        var name = "main";
        lexer.expect(TokenType.L_PAREN);
        lexer.expect(TokenType.R_PAREN);
        lexer.expect(TokenType.ARROW);
        var type = new Type(lexer.expect(TokenType.IDENT).getString());
        var args = new ArrayList<Variable>();

        var blockStmt = parseBlockStmt();
        return new Function(name, type, args, blockStmt.getLocals(), blockStmt);
    }

    Variable parseVariable() {
        boolean isConst;
        String name;
        Type type;
        ExprNode expr;
        if (lexer.test(TokenType.CONST)) {
            isConst = true;
            name = lexer.expect(TokenType.IDENT).getString();
            lexer.expect(TokenType.COLON);
            type = new Type(lexer.expect(TokenType.IDENT).getString());
            if (lexer.test(TokenType.ASSIGN)) {
                expr = parseExpr();
            } else {
                throw new RuntimeException("Constant must be initialized");
            }
        } else {
            isConst = false;
            lexer.expect(TokenType.LET);
            name = lexer.expect(TokenType.IDENT).getString();
            lexer.expect(TokenType.COLON);
            type = new Type(lexer.expect(TokenType.IDENT).getString());
            if (lexer.test(TokenType.ASSIGN)) {
                expr = parseExpr();
            } else {
                expr = LiteralNode.defaultValue(type);
            }
        }
        lexer.expect(TokenType.SEMICOLON);
        return new Variable(name, type, expr, isConst);
    }

    ExprNode parseExpr() {
        return exprParser.parse();
    }

    BlockNode parseBlockStmt() {
        checker.push();
        lexer.expect(TokenType.L_BRACE);

        var variables = new ArrayList<Variable>();
        var stmts = new ArrayList<StmtNode>();
        boolean exit = false;
        while (!exit) {
            switch (lexer.peek().getTokenType()) {
                case SEMICOLON -> {
                    lexer.next();
                    stmts.add(new EmptyNode());
                }
                case LET, CONST -> checker.add(parseVariable());
                case L_PAREN -> {
                    var blockStmt = parseBlockStmt();
                    for (var local : blockStmt.getLocals()) {
                        variables.add(local);
                    }
                    stmts.add(blockStmt);
                }
                case RETURN -> stmts.add(parseReturnStmt());
                case R_BRACE, EOF -> exit = true;
                default -> stmts.add(parseExprStmt());
            }
        }
        lexer.expect(TokenType.R_BRACE);

        var scope = checker.pop();
        for (var entity : scope.getEntities().values()) {
            if (entity instanceof Variable variable) {
                variables.add(variable);
            }
        }
        return new BlockNode(variables, stmts);
    }

    ReturnNode parseReturnStmt() {
        lexer.expect(TokenType.RETURN);
        ExprNode returnValue = null;
        if (!lexer.test(TokenType.SEMICOLON)) {
            returnValue = parseExpr();
            lexer.expect(TokenType.SEMICOLON);
        }
        return new ReturnNode(returnValue);
    }

    // TODO try to parse expr statement, if success, drop it, otherwise, throw a exception
    ExprStmtNode parseExprStmt() {
        ExprNode expr = parseExpr();
        lexer.expect(TokenType.SEMICOLON);
        return new ExprStmtNode(expr);
    }

    // TODO if & while & break & continue statement
}
