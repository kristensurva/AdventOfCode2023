package UtilityClasses;

public class MapNode {
    String name;
    MapNode left;
    MapNode right;
    MapNode pathEndNode;

    public MapNode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public MapNode getLeft() {
        return left;
    }

    public void setLeft(MapNode left) {
        this.left = left;
    }

    public MapNode getRight() {
        return right;
    }

    public void setRight(MapNode right) {
        this.right = right;
    }

    public MapNode getPathEndNode() {
        return pathEndNode;
    }

    public void setPathEndNode(MapNode pathEndNode) {
        this.pathEndNode = pathEndNode;
    }
}
