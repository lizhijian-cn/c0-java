package c0.parser;

import c0.ast.AST;
import c0.ast.expr.ExprNode;
import c0.ast.expr.LiteralNode;
import c0.ast.stmt.BlockNode;
import c0.ast.stmt.DeclStmtNode;
import c0.ast.stmt.ReturnNode;
import c0.ast.stmt.StmtNode;
import c0.entity.Entity;
import c0.entity.Function;
import c0.entity.Variable;
import c0.lexer.Lexer;
import c0.lexer.TokenType;
import c0.type.Type;

import java.util.ArrayList;

public class Parser {
    Lexer lexer;
    ExprParser exprParser;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
        this.exprParser = new ExprParser(lexer);
    }
    public AST parse() {
        return parseProgram();
    }

    AST parseProgram() {
        var ast = new AST();
        boolean exit = false;
        while (!exit) {
            switch (lexer.peek().getTokenType()) {
                case FN -> ast.addEntity(parseFunction());
                case LET, CONST -> ast.addEntity(parseVariable());
                default -> exit = true;
            };
        }
        lexer.expect(TokenType.EOF);
        return ast;
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
        return new Function(name, args, type, blockStmt);
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
            lexer.expect(TokenType.EQ);
            expr = parseExpr();
        } else {
            isConst = false;
            lexer.expect(TokenType.LET);
            name = lexer.expect(TokenType.IDENT).getString();
            lexer.expect(TokenType.COLON);
            type = new Type(lexer.expect(TokenType.IDENT).getString());
            if (lexer.test(TokenType.EQ)) {
                expr = parseExpr();
            } else {
                expr = LiteralNode.defaultValue(type);
            }
        }
        return new Variable(name, type, expr, isConst);
    }

    ExprNode parseExpr() {
        return exprParser.parse();
    }

    BlockNode parseBlockStmt() {
        lexer.expect(TokenType.L_PAREN);

        var stmts = new ArrayList<StmtNode>();
        boolean exit = false;
        while (!exit) {
            switch (lexer.peek().getTokenType()) {
                case SEMICOLON -> lexer.next();
                case LET, CONST -> stmts.add(parseDeclStmt());
                case L_PAREN -> stmts.add(parseBlockStmt());
                case RETURN -> stmts.add(parseReturnStmt());
                case EOF -> exit = true;
                default -> parseExprStmt();
            }
        }
        lexer.expect(TokenType.R_PAREN);
        return new BlockNode(stmts);
    }

    DeclStmtNode parseDeclStmt() {
        var variable = parseVariable();
        return new DeclStmtNode(variable.getName(), variable);
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
    ExprNode parseExprStmt() {
        ExprNode expr = parseExpr();
        lexer.expect(TokenType.SEMICOLON);
        return expr;
    }

    // TODO if & while & break & continue statement
}
