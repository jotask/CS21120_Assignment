package cs21120.assignment.solution;

// FIXME this class needs to be an BTSingleElimjov2 inner class

import cs21120.assignment.provided.IBinaryTree;

public class Node implements IBinaryTree {

    Node parent;

    Node left;
    Node right;

    String player;

    int score;

    public Node(Node parent) {
        this.parent = parent;
    }

    public Node() {
    }

    @Override
    public IBinaryTree getLeft() {
        return left;
    }

    @Override
    public IBinaryTree getRight() {
        return right;
    }

    @Override
    public String getPlayer() {
        return player;
    }

    @Override
    public int getScore() {
        return score;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }


}
