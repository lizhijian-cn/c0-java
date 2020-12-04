//package c0.parser;
//
//import c0.ast.expr.ExprNode;
//import c0.lexer.Lexer;
//import c0.lexer.Token;
//import c0.lexer.TokenType;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.LinkedList;
//import java.util.List;
//
//@Deprecated
//public class ExprOPG {
//    Lexer lexer;
//    ArrayList<TokenType> vtSet;
//    HashMap<TokenType, Integer> indexes;
//    Integer[][] table;
//
//    public ExprOPG(Lexer lexer) {
//        this.lexer = lexer;
//        vtSet = new ArrayList<>(List.of(
//                TokenType.IDENT, TokenType.ASSIGN,
//                TokenType.LT, TokenType.GT, TokenType.LE, TokenType.GE, TokenType.EQ, TokenType.NEQ,
//                TokenType.PLUS, TokenType.MINUS,
//                TokenType.MUL, TokenType.DIV,
//                TokenType.AS,
//                TokenType.L_PAREN, TokenType.R_PAREN,
//                TokenType.COLON
//        ));
//        int n = vtSet.size();
//        for (int i = 0; i < n; i++) {
//            indexes.put(vtSet.get(i), i);
//        }
//        table = new Integer[][]{
//                {null, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1},
//                {-1, null, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, -1, 1, 1},
//                {-1, null, 1, 1, 1, 1, 1, 1, -1, -1, -1, -1, 1, -1, 1, 1},
//                {-1, null, 1, 1, 1, 1, 1, 1, -1, -1, -1, -1, 1, -1, 1, 1},
//                {-1, null, 1, 1, 1, 1, 1, 1, -1, -1, -1, -1, 1, -1, 1, 1},
//                {-1, null, 1, 1, 1, 1, 1, 1, -1, -1, -1, -1, 1, -1, 1, 1},
//                {-1, null, 1, 1, 1, 1, 1, 1, -1, -1, -1, -1, 1, -1, 1, 1},
//                {-1, null, 1, 1, 1, 1, 1, 1, -1, -1, -1, -1, 1, -1, 1, 1},
//                {-1, null, 1, 1, 1, 1, 1, 1, 1, 1, -1, -1, 1, -1, 1, 1},
//                {-1, null, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 1, 1},
//                {-1, null, 1, 1, 1, 1, 1, 1, 1, -1, 1, 1, 1, -1, 1, 1},
//                {-1, null, 1, 1, 1, 1, 1, 1, 1, -1, 1, 1, 1, -1, 1, 1},
//                {0, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
//                {-1, null, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, -1},
//                {null, null, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, null, 1, 1},
//                {-1, null, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 1, -1},
//                {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, null, null}
//        };
//    }
//
//    /**
//     * A -> i = A | B
//     * B -> B < C | B > C | B <= C | B >= C | B == C | B != C | C
//     * C -> C + D | C - D | D
//     * D -> D * E | D / E | E
//     * E -> A as id | F
//     * F -> -A | G
//     * G -> id() | id(H) | I
//     * H -> A , H | A
//     * I -> (A) | id | uint | double | string | char
//     */
//    public ExprNode parseExpr() {
//        var operator = new LinkedList<>();
//        var operand = new LinkedList<>();
//        return null;
//    }
//
//    int compare(Token a, Token b) {
//        return table[indexes.get(a.getTokenType())][indexes.get(b.getTokenType())];
//    }
//}
