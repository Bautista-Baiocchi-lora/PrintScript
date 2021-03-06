package edu.austral.ingsis.ast.exceptions;

import edu.austral.ingsis.tokens.Token;
import edu.austral.ingsis.tokens.TokenType;

public class SyntaxTokenExpectedException extends RuntimeException {

  public SyntaxTokenExpectedException(Token token, TokenType expected) {
    super("Error: " + expected.toString() + " expected at " + token.toString());
  }

  public SyntaxTokenExpectedException(int line, TokenType expected) {
    super("Error: " + expected.toString() + " expected on line " + line);
  }
}
