public class CompilerFrontendImpl extends CompilerFrontend {
    public CompilerFrontendImpl() {
        super();
    }

    public CompilerFrontendImpl(boolean debug_) {
        super(debug_);
    }

    /*
     * Initializes the local field "lex" to be equal to the desired lexer.
     * The desired lexer has the following specification:
     * 
     * NUM: [0-9]*\.[0-9]+
     * PLUS: \+
     * MINUS: -
     * TIMES: \*
     * DIV: /
     * WHITE_SPACE (' '|\n|\r|\t)*
     */
    @Override
    protected void init_lexer() {
        LexerImpl lexer = new LexerImpl();

        Automaton a_num = new AutomatonImpl();
        a_num.addState(0, true, false); 
        a_num.addState(1, false, true); 

        for (char i = '0'; i <= '9'; i++) {
            a_num.addTransition(0, i, 1); 
            a_num.addTransition(1, i, 1); 
        }

        a_num.addTransition(0, '.', 1); 
        a_num.addTransition(1, '.', 1); 

        for (char i = '0'; i <= '9'; i++) {
            a_num.addTransition(1, i, 1); 
        }

        lexer.add_automaton(TokenType.NUM, a_num);

        Automaton a_plus = new AutomatonImpl();
        a_plus.addState(0, true, false);
        a_plus.addState(1, false, true);
        a_plus.addTransition(0, '+', 1);
        lexer.add_automaton(TokenType.PLUS, a_plus);

        Automaton a_minus = new AutomatonImpl();
        a_minus.addState(0, true, false);
        a_minus.addState(1, false, true);
        a_minus.addTransition(0, '-', 1);
        lexer.add_automaton(TokenType.MINUS, a_minus);

        Automaton a_times = new AutomatonImpl();
        a_times.addState(0, true, false);
        a_times.addState(1, false, true);
        a_times.addTransition(0, '*', 1);
        lexer.add_automaton(TokenType.TIMES, a_times);

        Automaton a_div = new AutomatonImpl();
        a_div.addState(0, true, false);
        a_div.addState(1, false, true);
        a_div.addTransition(0, '/', 1);
        lexer.add_automaton(TokenType.DIV, a_div);

        Automaton a_ws = new AutomatonImpl();
        a_ws.addState(0, true, false); 
        a_ws.addState(1, false, true);

        a_ws.addTransition(0, ' ', 1);
        a_ws.addTransition(0, '\n', 1);
        a_ws.addTransition(0, '\r', 1);
        a_ws.addTransition(0, '\t', 1);

        a_ws.addTransition(1, ' ', 1);
        a_ws.addTransition(1, '\n', 1);
        a_ws.addTransition(1, '\r', 1);
        a_ws.addTransition(1, '\t', 1);

        lexer.add_automaton(TokenType.WHITE_SPACE, a_ws);

        Automaton a_lparen = new AutomatonImpl();
        a_lparen.addState(0, true, false);
        a_lparen.addState(1, false, true);
        a_lparen.addTransition(0, '(', 1);
        lexer.add_automaton(TokenType.LPAREN, a_lparen);

        Automaton a_rparen = new AutomatonImpl();
        a_rparen.addState(0, true, false);
        a_rparen.addState(1, false, true);
        a_rparen.addTransition(0, ')', 1);
        lexer.add_automaton(TokenType.RPAREN, a_rparen);

        this.lex = lexer;
    }

}
