package edu.austral.ingsis.ast;

import edu.austral.ingsis.Token;
import edu.austral.ingsis.TokenType;
import edu.austral.ingsis.ast.exceptions.SyntaxException;
import java.util.List;
import java.util.Stack;

public class ShuntingYard {

  private static boolean isTokenType(Token token, TokenType type) {
    return token.getType() == type;
  }

  private static int precedence(Token token){
    switch (token.getType()){
      case R_PARENTHESES:
        return 3;
      case STAR_SYMBOL:
      case SLASH_SYMBOL:
        return 2;
      case PLUS_SYMBOL:
      case MINUS_SYMBOL:
        return 1;
      case L_PARENTHESES:
        return 0;
    }

    return -1;
  }

  public static Stack<Token> parse2(List<Token> tokens) {
    final Stack<Token> operatorStack = new Stack<>();
    final Stack<Token> expStack = new Stack<>();

    for (Token token : tokens) {
      if (isTokenType(token, TokenType.L_PARENTHESES)) {
        operatorStack.push(token);
      } else if (isTokenType(token, TokenType.LITERAL)
          || isTokenType(token, TokenType.IDENTIFIER)) {
        expStack.push(token);
      } else if (isTokenType(token, TokenType.STAR_SYMBOL)
          || isTokenType(token, TokenType.SLASH_SYMBOL)) {
        operatorStack.push(token);
      } else if (isTokenType(token, TokenType.PLUS_SYMBOL)
          || isTokenType(token, TokenType.MINUS_SYMBOL)) {
        if (!operatorStack.empty()) {
          // Check precedence
          while (isTokenType(operatorStack.peek(), TokenType.STAR_SYMBOL)
              || isTokenType(operatorStack.peek(), TokenType.SLASH_SYMBOL)) {
            Token top = operatorStack.pop();
            expStack.push(top);
          }
        }
        operatorStack.push(token);
      } else if (isTokenType(token, TokenType.R_PARENTHESES)) {
        Token popped;
        while (!isTokenType((popped = operatorStack.pop()), TokenType.L_PARENTHESES)) {
          expStack.push(popped);
        }
      } else {
        throw new SyntaxException(token);
      }
    }



    expStack.addAll(operatorStack);

    return expStack;
  }

  public static Stack<Token> parse(List<Token> tokens) {
    final Stack<Token> operatorStack = new Stack<>();
    final Stack<Token> expStack = new Stack<>();

    for (Token token : tokens) {
      if (isTokenType(token, TokenType.L_PARENTHESES)) {
        operatorStack.push(token);
      } else if (isTokenType(token, TokenType.LITERAL)
              || isTokenType(token, TokenType.IDENTIFIER)) {
        expStack.push(token);
      } else if(isTokenType(token, TokenType.R_PARENTHESES)){
        Token popped;
        while (!isTokenType((popped = operatorStack.pop()), TokenType.L_PARENTHESES)) {
          expStack.push(popped);
        }
      }else {
          while (!operatorStack.empty() && precedence(operatorStack.peek()) >= precedence(token)) {
            Token top = operatorStack.pop();
            expStack.push(top);
          }
          operatorStack.push(token);
        }
      }

    expStack.addAll(operatorStack);

    return expStack;
  }
}
