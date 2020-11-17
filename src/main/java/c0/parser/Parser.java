package c0.parser;

import c0.ast.AST;
import c0.ast.expr.ExprNode;
import c0.ast.expr.LiteralNode;
import c0.ast.stmt.BlockNode;
import c0.entity.Function;
import c0.entity.Variable;
import c0.lexer.Lexer;
import c0.lexer.TokenType;
import c0.type.Type;

import java.util.ArrayList;

public class Parser {
    Lexer lexer;
    ExprOPG exprOPG;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
        this.exprOPG = new ExprOPG(lexer);
    }
    public AST parse() {
        return parseProgram();
    }

    AST parseProgram() {
        var ast = new AST();
        while (true) {
            if (lexer.check(TokenType.FN)) {
                ast.addEntity(parseFunction());
            } else if (lexer.check(TokenType.LET) || lexer.check(TokenType.CONST)) {
                ast.addEntity(parseVariable());
            } else {
                break;
            }
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
        return exprOPG.parseExpr();
    }
    BlockNode parseBlockStmt() {
        return null;
    }


}
