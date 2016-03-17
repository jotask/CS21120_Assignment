package cs21120.assignment.solution;

import cs21120.assignment.provided.*;

import java.io.FileNotFoundException;
import java.util.*;

public class BTSingleElimJov2 implements IManager {

    enum DIR {LEFT, RIGHT}

    Queue<Node> queue;
    Stack<Node> stack;

    private Node root;

    private MatchExtend lastMatch;

    public BTSingleElimJov2() {

        // Initialize the root
        this.root = new Node();
        this.root.left = new Node(root);
        this.root.right = new Node(root);

        // Initialize the queue and stack
        queue = new LinkedList<>();
        stack = new Stack<>();
    }

    @Override
    public void setPlayers(ArrayList<String> players) {

        // setPlayers and tree construction (30%): The tree should be constructed when the setPlayers method of
        // the IManager interface is called. The tree should be initialised with the players at the leaves of the
        // tree, with roughly half of the players in the left subtree and half in the right subtree at every node.
        // A suggestion	is to use a recursive method, passing half the list of players to the left subtree and half
        // to the right subtree recursively until the list of players is only one long, where the name of the player
        // (or team) is stored in the node and the recursion stops.

        List<String> left;
        List<String> right;

        if(players.size() == 1){
            root.left.setPlayer(players.get(0));
            return;
        }

        int middle = players.size() / 2;

        left = players.subList(0, middle);
        right = players.subList(middle, players.size());

        addToTree(DIR.LEFT, root.left, left);
        addToTree(DIR.RIGHT, root.right, right);

        buildMatches();

    }

    private void addToTree(DIR dir, Node n, List<String> players){

        if(players.size() == 1){
            n.setPlayer(players.get(0));
            return;
        }

        if(n.left == null) {
            n.left = new Node();
            n.left.setParent(n);
        }

        int middle = players.size() / 2;

        List<String> left = new ArrayList<>(players.subList(0, middle));
        List<String> right = new ArrayList<>(players.subList(middle, players.size()));

        if(n.left == null){
            n.left = new Node(n);
        }

        addToTree(DIR.LEFT, n.left, left);

        if(n.right == null){
            n.right = new Node(n);
        }

        addToTree(DIR.RIGHT, n.right, right);

    }

    private void buildMatches(){

        queue.clear();
        stack.clear();

        queue.offer(root);

        while(!queue.isEmpty()){
            Node tmp = queue.poll();

            if(tmp.left != null)
                queue.offer(tmp.left);
            if(tmp.right != null)
                queue.offer(tmp.right);

            if(tmp.left != null && tmp.right != null)
                stack.add(tmp);

        }

    }

    @Override
    public boolean hasNextMatch() {
        // The hasNextMatch method just needs to check that the stack is not empty.
        return !stack.isEmpty();
    }

    @Override
    public Match nextMatch() throws NoNextMatchException {

        // TODO

        if(!hasNextMatch())
            throw new NoNextMatchException("NoNextMatchException");

        Node n = stack.pop();

        lastMatch = new MatchExtend(n.left, n.right);

        return lastMatch;
    }

    @Override
    public void setMatchScore(int p1, int p2) {

        lastMatch.p1.setScore(p1);
        lastMatch.p2.setScore(p2);

        if(p2 < p1){
            Node parent = lastMatch.p1.parent;
            parent.setPlayer(lastMatch.p1.getPlayer());

        }else if(p1 < p2){
            Node parent = lastMatch.p2.parent;
            parent.setPlayer(lastMatch.p2.getPlayer());
        }

    }

    @Override
    public String getPosition(int n) {

        // TODO

        return root.getPlayer();

    }

    @Override
    public IBinaryTree getCompetitionTree() {
        return root;
    }

    public static void main(String[] args) {
        String file = "data/data.test";
        CompetitionManager cm = new CompetitionManager(new BTSingleElimJov2());
        try {
            cm.runCompetition(file);
        } catch (FileNotFoundException e) {
            System.err.println("FILE NOT FOUND");
            e.printStackTrace();
        }
    }

}
