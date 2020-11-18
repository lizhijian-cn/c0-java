package c0.parser;

import c0.ast.expr.*;
import c0.lexer.Lexer;
import c0.lexer.TokenType;
import c0.type.Type;

import java.util.ArrayList;
import java.util.List;

public class ExprParser {
    Lexer lexer;

    public ExprParser(Lexer lexer) {
        this.lexer = lexer;
    }

    /**
     * OPG
     * A -> i = A | B
     * B -> B < C | B > C | B <= C | B >= C | B == C | B != C | C
     * C -> C + D | C - D | D
     * D -> D * E | D / E | E
     * E -> E as id | F
     * F -> -F | G
     * G -> id() | id(H) | I
     * H -> A, H | A
     * I -> (A) | id | literal
     *
     * removing left recursion
     * A -> i = A | B
     * B -> C { < C | > C | <= C | >= C | == C | != C }
     * C -> D { + D | - D }
     * D -> E { * E | / E }
     * E -> F { as id }
     * F -> -F | G
     * G -> id() | id(H) | I
     * H -> A, H | A
     * I -> (A) | id | literal
     */
    public ExprNode parse() {
        return a();
    }

    ExprNode a() {
        if (lexer.check(TokenType.IDENT)) {
            var lhs = new VariableNode(lexer.next().getString());
            lexer.expect(TokenType.ASSIGN);
            var rhs = a();
            return new AssignNode(lhs, rhs);
        }
        return b();
    }

    ExprNode b() {
        ExprNode left = c();
        if (lexer.check(x ->
                List.of(TokenType.GT, TokenType.LT, TokenType.GE, TokenType.LE, TokenType.EQ, TokenType.NEQ)
                        .contains(x.getTokenType()))) {
            var op = lexer.next().getTokenType();
            ExprNode right = c();
            return new BinaryOpNode(left, op, right);
        }
        return left;
    }

    ExprNode c() {
        ExprNode left = d();
        if (lexer.check(x ->
                List.of(TokenType.PLUS, TokenType.MINUS).contains(x.getTokenType()))) {
            var op = lexer.next().getTokenType();
            ExprNode right = d();
            return new BinaryOpNode(left, op, right);
        }
        return left;
    }

    ExprNode d() {
        ExprNode left = e();
        if (lexer.check(x ->
                List.of(TokenType.MUL, TokenType.DIV).contains(x.getTokenType()))) {
            var op = lexer.next().getTokenType();
            ExprNode right = e();
            return new BinaryOpNode(left, op, right);
        }
        return left;
    }

    ExprNode e() {
        ExprNode expr = f();
        if (lexer.test(TokenType.AS)) {
            var type = new Type(lexer.expect(TokenType.IDENT).getString());
            expr = new AsNode(expr, type);
        }
        return expr;
    }

    ExprNode f() {
        if (lexer.test(TokenType.MINUS)) {
            ExprNode expr = f();
            return new UnaryOpNode(TokenType.MINUS, expr);
        }
        return g();
    }

    ExprNode g() {
        if (lexer.check(TokenType.IDENT)) {
            String funcName = lexer.next().getString();
            var args = new ArrayList<ExprNode>();
            lexer.expect(TokenType.L_PAREN);
            if (lexer.test(TokenType.R_PAREN)) {
                return new FunctionCallNode(funcName, args);
            }
            args.add(a());
            while (lexer.test(TokenType.COMMA)) {
                args.add(a());
            }
            return new FunctionCallNode(funcName, args);
        }
        return i();
    }

    ExprNode i() {
        if (lexer.test(TokenType.L_PAREN)) {
            ExprNode expr = a();
            lexer.expect(TokenType.R_PAREN);
            return expr;
        }
        if (lexer.check(x ->
                List.of(TokenType.CHAR_LITERAL, TokenType.DOUBLE_LITERAL, TokenType.STRING_LITERAL, TokenType.UINT_LITERAL)
                        .contains(x.getTokenType()))) {
            return LiteralNode.FactoryConstructor(lexer.next());
        }
        if (lexer.check(TokenType.IDENT)) {
            return new VariableNode(lexer.next().getString());
        }
        throw new RuntimeException("unrecognized Token " + lexer.peek());
    }
}
