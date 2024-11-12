
public class ParserImpl extends Parser {

    /*
     * Implements a recursive-descent parser for the following CFG:
     * 
     * T -> F AddOp T              { if ($2.type == TokenType.PLUS) { $$ = new PlusExpr($1,$3); } else { $$ = new MinusExpr($1, $3); } }
     * T -> F                      { $$ = $1; }
     * F -> Lit MulOp F            { if ($2.type == TokenType.Times) { $$ = new TimesExpr($1,$3); } else { $$ = new DivExpr($1, $3); } }
     * F -> Lit                    { $$ = $1; }
     * Lit -> NUM                  { $$ = new FloatExpr(Float.parseFloat($1.lexeme)); }
     * Lit -> LPAREN T RPAREN      { $$ = $2; }
     * AddOp -> PLUS               { $$ = $1; }
     * AddOp -> MINUS              { $$ = $1; }
     * MulOp -> TIMES              { $$ = $1; }
     * MulOp -> DIV                { $$ = $1; }
     */
    @Override
    public Expr do_parse() throws Exception {
        return T(); 
    }

    private Expr T() throws Exception {
        Expr left = F();  

        if (peek(TokenType.PLUS, 0) || peek(TokenType.MINUS, 0)) {
            Token operator = consume(peek(TokenType.PLUS, 0) ? TokenType.PLUS : TokenType.MINUS); 
            Expr right = T(); 
            if (operator.ty == TokenType.PLUS) {
                return new PlusExpr(left, right); 
            } else {
                return new MinusExpr(left, right); 
            }
        }

        return left; 
    }

    private Expr F() throws Exception {
        Expr left = Lit();  
        
        if (peek(TokenType.TIMES, 0) || peek(TokenType.DIV, 0)) {
            Token operator = consume(peek(TokenType.TIMES, 0) ? TokenType.TIMES : TokenType.DIV); 
            Expr right = F(); 
            if (operator.ty == TokenType.TIMES) {
                return new TimesExpr(left, right);
            } else {
                return new DivExpr(left, right); 
            }
        }

        return left;  
    }

    private Expr Lit() throws Exception {
        if (peek(TokenType.NUM, 0)) {
            Token token = consume(TokenType.NUM);  
            return new FloatExpr(Float.parseFloat(token.lexeme));  
        } else if (peek(TokenType.LPAREN, 0)) {
            consume(TokenType.LPAREN);  
            Expr expr = T(); 
            if (peek(TokenType.RPAREN, 0)) {
                consume(TokenType.RPAREN); 
                return expr; 
            } else {
                throw new Exception("Expected closing parenthesis RPAREN");
            }
        } else {
            throw new Exception("Expected NUM or LPAREN");
        }
    }
}
