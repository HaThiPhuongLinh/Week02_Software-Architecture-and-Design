package com.edu.fit.node;

public class TestNode {
    public static void main(String[] args) {
        //Create simple AST tree
        Node rootNode = new Node("Program");
        Node classNode = new Node("ClassDeclaration");
        Node methodNode = new Node("MethodDeclaration");
        Node statementNode = new Node("Statement");
        Node variableNode = new Node("VariableDeclaration");

        // Connect nodes together
        rootNode.addChild(classNode);
        classNode.addChild(methodNode);
        methodNode.addChild(statementNode);
        statementNode.addChild(variableNode);

        //Use Lambda Expresstion to define NodeHandler
        NodeIterator.NodeHandler printNodeHandler = node -> {
            System.out.println(node.getChildNodes());
            return true;
        };

        NodeIterator iterator = new NodeIterator(printNodeHandler);
        iterator.explore(rootNode);
    }
}
