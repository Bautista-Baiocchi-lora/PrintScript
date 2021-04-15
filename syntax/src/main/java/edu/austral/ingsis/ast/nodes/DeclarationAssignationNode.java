package edu.austral.ingsis.ast.nodes;


import edu.austral.ingsis.ast.Token;
import edu.austral.ingsis.ast.visitor.Visitable;
import edu.austral.ingsis.ast.visitor.Visitor;

public class DeclarationAssignationNode extends AbstractNode implements Visitable {

    private DeclarationNode declaration;
    private ExpressionNode value;

    public DeclarationAssignationNode(Token token) {
        super(token);
    }

    public void setLeft(DeclarationNode node) {
        this.declaration = node;
    }

    public DeclarationNode getLeft() {
        return this.declaration;
    }

    public void setRight(ExpressionNode node) {
        this.value = node;
    }

    public ExpressionNode getRight() {
        return this.value;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}