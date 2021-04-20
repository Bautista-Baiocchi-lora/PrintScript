package edu.austral.ingsis.ast.parsers;

import edu.austral.ingsis.Token;
import edu.austral.ingsis.TokenType;
import edu.austral.ingsis.ast.TokenPattern;
import edu.austral.ingsis.ast.nodes.ReferenceNode;
import java.util.List;

public class ReferenceParser implements NodeParser<ReferenceNode> {

  public boolean predicate(List<Token> tokens) {
    return TokenPattern.Builder.of(TokenType.IDENTIFIER).build().matches(tokens);
  }

  public ReferenceNode parse(List<Token> tokens) {
    return new ReferenceNode(tokens.get(0));
  }
}