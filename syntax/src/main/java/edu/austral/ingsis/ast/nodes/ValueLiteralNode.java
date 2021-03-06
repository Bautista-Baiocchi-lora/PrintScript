package edu.austral.ingsis.ast.nodes;

import edu.austral.ingsis.ast.visitor.Visitable;
import edu.austral.ingsis.ast.visitor.Visitor;
import edu.austral.ingsis.tokens.Token;

public class ValueLiteralNode extends ExpressionNode implements Visitable {

  public ValueLiteralNode(Token token) {
    super(token);
  }

  @Override
  public String getNodeType() {
    return "VALUE_LITERAL";
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
