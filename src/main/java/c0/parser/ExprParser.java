package c0.parser;

import c0.ast.expr.*;
import c0.entity.StringVariable;
import c0.lexer.Lexer;
import c0.lexer.TokenType;
import c0.type.Type;
import lombok.AllArgsConstructor;

import java.nio.file.ClosedDirectoryStreamException;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class ExprParser {
    Lexer lexer;
    VariableChecker checker;

    /**
     * OPG
     * A -> i = A | B    // look ahead 2 token
     * B -> B < C | B > C | B <= C | B >= C | B == C | B != C | C
     * C -> C + D | C - D | D
     * D -> D * E | D / E | E
     * E -> E as id | F
     * F -> -F | G
     * G -> id() | id(H) | I // look ahead 2 token
     * H -> A, H | A
     * I -> (A) | id | literal
     * <p>
     * a grammar that removing left recursion, parsed by LL(2)
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
        // TODO check if lhs is type
        if (lexer.check(TokenType.IDENT)) {
            var name = lexer.next().getValue();
            var variable = checker.getVariable(name);
            var lhs = new VariableNode(name, variable);
            if (lexer.test(TokenType.ASSIGN)) {
                if (variable.isConst()) {
                    throw new RuntimeException("constant must not be assigned");
                }
                var rhs = a();
                return new AssignNode(lhs, rhs);
            } else {
                lexer.unread();
            }
        }
        return b();
    }

    ExprNode b() {
        ExprNode left = c();
        while (lexer.check(x ->
                List.of(TokenType.GT, TokenType.LT, TokenType.GE, TokenType.LE, TokenType.EQ, TokenType.NEQ)
                        .contains(x.getTokenType()))) {
            var op = lexer.next().getTokenType();
            ExprNode right = c();
            left = new BinaryOpNode(left, op, right);
        }
        return left;
    }

    ExprNode c() {
        ExprNode left = d();
        while (lexer.check(x ->
                List.of(TokenType.PLUS, TokenType.MINUS).contains(x.getTokenType()))) {
            var op = lexer.next().getTokenType();
            ExprNode right = d();
            left = new BinaryOpNode(left, op, right);
        }
        return left;
    }

    ExprNode d() {
        ExprNode left = e();
        while (lexer.check(x ->
                List.of(TokenType.MUL, TokenType.DIV).contains(x.getTokenType()))) {
            var op = lexer.next().getTokenType();
            ExprNode right = e();
            left = new BinaryOpNode(left, op, right);
        }
        return left;
    }

    ExprNode e() {
        ExprNode expr = f();
        while (lexer.test(TokenType.AS)) {
            var type = new Type(lexer.expect(TokenType.IDENT).getValue());
            expr = new CastNode(expr, type);
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
            String name = lexer.next().getValue();
            if (lexer.test(TokenType.L_PAREN)) {
                var args = new ArrayList<ExprNode>();
                if (!lexer.check(TokenType.R_PAREN)) {
                    args.add(a());
                    while (lexer.test(TokenType.COMMA)) {
                        args.add(a());
                    }
                }
                lexer.expect(TokenType.R_PAREN);
                if (List.of(
                        "getint", "getdouble", "getchar",
                        "putint", "putdouble", "putstr", "putln")
                        .contains(name)) {
                    return new STLFunctionCallNode(new StringVariable(name), args);
                }
                return new FunctionCallNode(name, args, checker.getFunction(name));
            } else {
                lexer.unread();
            }
        }
        return i();
    }

    ExprNode i() {
        if (lexer.test(TokenType.L_PAREN)) {
            ExprNode expr = a();
            lexer.expect(TokenType.R_PAREN);
            return expr;
        }
        if (lexer.check(TokenType.STRING_LITERAL)) {
            var string = new StringVariable(lexer.next().getValue());
            checker.add(string);
            return new StringNode(string);
        }
        if (lexer.check(x ->
                List.of(TokenType.CHAR_LITERAL, TokenType.DOUBLE_LITERAL, TokenType.UINT_LITERAL)
                        .contains(x.getTokenType()))) {
            return LiteralNode.FactoryConstructor(lexer.next());
        }
        if (lexer.check(TokenType.IDENT)) {
            var name = lexer.next().getValue();
            return new VariableNode(name, checker.getVariable(name));
        }
        throw new RuntimeException("unrecognized Token " + lexer.peek());
    }
}
