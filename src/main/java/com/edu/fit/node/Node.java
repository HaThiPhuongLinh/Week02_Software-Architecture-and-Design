package com.edu.fit.node;

import java.util.ArrayList;
import java.util.List;

public class Node {
    public String value;
    public List<Node> childNodes;

    public Node(String value) {
        this.value = value;
        this.childNodes = new ArrayList<>();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<Node> getLst() {
        return childNodes;
    }

    public void setLst(List<Node> childNodes) {
        this.childNodes = childNodes;
    }

    public void addChild(Node node){
        childNodes.add(node);
    }

    public List<Node> getChildNodes() {
        return childNodes;
    }

    @Override
    public String toString() {
        return "Node{" +
                "value='" + value + '\'' +
                ", childNodes=" + childNodes +
                '}';
    }
}
