package c0.parser;

import c0.ast.expr.ExprNode;
import c0.lexer.Lexer;
import c0.lexer.Token;
import c0.lexer.TokenType;

import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ExprOPG {
    Lexer lexer;
    Set<TokenType> validTokens;
    public ExprOPG(Lexer lexer) {
        this.lexer = lexer;
        validTokens = new HashSet<>(List.of(
                TokenType.IDENT, TokenType.L_PAREN, TokenType.R_PAREN,
                TokenType.MUL, TokenType.DIV,
                TokenType.PLUS, TokenType.MINUS,
                TokenType.GT, TokenType.LT, TokenType.GE, TokenType.LE, TokenType.EQ, TokenType.NEQ,
                TokenType.ASSIGN
        ));
    }

    /**
     * IDENT
     * (E)
     * IDNET (E {, E}* )
     * -E
     * E as type(IDENT)
     * * /
     * + -
     * > < >= <= == !=
     * IDENT = E
     *
     *
     */
    public ExprNode parseExpr() {
        Deque<Token>
        return null;
    }
}
