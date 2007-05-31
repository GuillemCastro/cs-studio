package org.csstudio.util.formula.node;

import org.csstudio.util.formula.Node;

public class MulNode implements Node
{
    private Node left;
    private Node right;
    
    public MulNode(Node left, Node right)
    {
        this.left = left;
        this.right = right;
    }
    
    public double eval()
    {
        double a = left.eval();
        double b = right.eval();
        return a * b;
    }
    
    @SuppressWarnings("nls")
    @Override
    public String toString()
    {
        return "(" + left + " * " + right + ")";
    }

}
