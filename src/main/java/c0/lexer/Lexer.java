package c0.lexer;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class Lexer implements Iterator<Token> {
    CharInterator charIter;

    @Override
    public Token next() {
        if (!charIter.hasNext()) {
            return new Token(TokenType.EOF);
        }
        var nextChar = charIter.peek();
        if (Character.isWhitespace(nextChar)) {
            return skipSpace();
        }
        if (isUnderlineOrLetterOrDigit(nextChar)) {
            return lexIdentOrKeyword();
        }
        if (Character.isDigit(nextChar)) {
            return lexUInt();
        }
        if (nextChar == '"') {
            return lexString();
        }
        if (nextChar == '\'') {
            return lexChar();
        }
        if (nextChar == '/') {
            // TODO lexComment
        }
        if (isOperator(nextChar)) {
            return lexOperator();
        }
        throw new RuntimeException("unrecognized character");
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    Token skipSpace() {
        while (charIter.test(Character::isWhitespace).isPresent()) {
            continue;
        }
        return next();
    }

    boolean isUnderlineOrLetterOrDigit(Character ch) {
        return Character.isLetterOrDigit(ch) || ch == '_';
    }

    Token lexIdentOrKeyword() {
        var sb = new StringBuilder();
        while (true) {
            var op = charIter.test(this::isUnderlineOrLetterOrDigit);
            if (op.isEmpty()) {
                break;
            }
            sb.append(op.get());
        }
        var value = sb.toString();
        return switch (value) {
            case "let" -> new Token(TokenType.LET);
            case "const" -> new Token(TokenType.CONST);
            case "as" -> new Token(TokenType.AS);
            case "return" -> new Token(TokenType.RETURN);
            default -> new Token(TokenType.IDENT, value);
        };
    }

    Token lexUInt() {
        var sb = new StringBuilder();
        while (true) {
            var op = charIter.test(Character::isDigit);
            if (op.isEmpty()) {
                break;
            }
            sb.append(op.get());
        }
        return new Token(TokenType.UINT_LITERAL, Integer.parseUnsignedInt(sb.toString()));
    }

    Optional<Character> lexSpecialOrLiteralChar() {
        if (!charIter.hasNext()) {
            return Optional.empty();
        }
        var nextChar = charIter.peek();
        if (nextChar == '"') {
            return Optional.empty();
        }

        return switch (nextChar) {
            case '\\' -> {
                charIter.next();
                if (!charIter.hasNext()) {
                    throw new RuntimeException("lack of special character");
                }
                yield switch (charIter.next()) {
                    case '\\' -> Optional.of('\\');
                    case '"' -> Optional.of('"');
                    case '\'' -> Optional.of('\'');
                    case 'n' -> Optional.of('\n');
                    case 'r' -> Optional.of('\r');
                    case 't' -> Optional.of('\t');
                    default -> throw new RuntimeException("Unrecognised Character");
                };
            }
            default -> Optional.of(charIter.next());
        };
    }
    Token lexString() {
        var sb = new StringBuilder();
        charIter.expect('"');
        while (true) {
            var op = lexSpecialOrLiteralChar();
            if (op.isEmpty()) {
                break;
            }
            sb.append(op.get());
        }
        charIter.expect('"');
        return new Token(TokenType.STRING_LITERAL, sb.toString());
    }

    Token lexChar() {
        charIter.expect('\'');
        var op = lexSpecialOrLiteralChar();
        var value = op.orElseThrow(() -> new RuntimeException("char literal must not be empty"));
        charIter.expect('\'');
        return new Token(TokenType.CHAR_LITERAL, value);
    }

    boolean isOperator(char ch) {
        return List.of('+', '-', '*', '/', '=', '!', '<', '>', '(', ')', '{', '}', ',', ':', ';').contains(ch);
    }

    Token lexOperator() {
        return switch (charIter.next()) {
            case '+' -> new Token(TokenType.PLUS);
            case '-' -> {
                if (charIter.test('>')) {
                    yield new Token(TokenType.ARROW);
                }
                yield new Token(TokenType.MINUS);
            }
            case '*' -> new Token(TokenType.MUL);
            case '/' -> new Token(TokenType.DIV);
            case '=' -> {
                if (charIter.test('=')) {
                    yield new Token(TokenType.EQ);
                }
                yield new Token(TokenType.ASSIGN);
            }
            case '!' -> {
                charIter.expect('=');
                yield new Token(TokenType.NEQ);
            }
            case '<' -> {
                if (charIter.test('=')) {
                    yield new Token(TokenType.LE);
                }
                yield new Token(TokenType.LT);
            }
            case '>' -> {
                if (charIter.test('=')) {
                    yield new Token(TokenType.GE);
                }
                yield new Token(TokenType.GT);
            }
            case '(' -> new Token(TokenType.L_PAREN);
            case ')' -> new Token(TokenType.R_PAREN);
            case '{' -> new Token(TokenType.L_BRACE);
            case '}' -> new Token(TokenType.R_BRACE);
            case ',' -> new Token(TokenType.COMMA);
            case ':' -> new Token(TokenType.COLON);
            case ';' -> new Token(TokenType.SEMICOLON);
            default -> throw new RuntimeException("unrecognized character");
        };
    }
}
