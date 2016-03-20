package cs21120.assignment.solution;

import cs21120.assignment.provided.*;

import java.io.FileNotFoundException;
import java.util.*;

public class BTSingleElimJov2 implements IManager {

    Stack<Node> stack;

    private Node root;

    private MatchExtend lastMatch;

    public BTSingleElimJov2() {

        // Initialize the root
        this.root = new Node();
        this.root.left = new Node(root);
        this.root.right = new Node(root);

        // Initialize the queue and stack
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

        if(players.size() == 1){
            root.left.setPlayer(players.get(0));
            return;
        }

        int middle = players.size() / 2;

        List<String> left = players.subList(0, middle);
        List<String> right = players.subList(middle, players.size());

        addToTree(root.left, left);
        addToTree(root.right, right);

        buildMatches();

    }

    private void addToTree(Node n, List<String> players){

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

        addToTree(n.left, left);

        if(n.right == null){
            n.right = new Node(n);
        }

        addToTree(n.right, right);

    }

    private void buildMatches(){

        Queue<Node> queue = new LinkedList<>();

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

        ArrayList<Node> nodes = new ArrayList<>();

        help(root, nodes);

        return nodes.get(n).getPlayer();

    }

    private void help(Node n, ArrayList<Node> q){
        Node left = n.left;
        Node right = n.right;

        if(left == null || right == null)
            return;

        if(left.getScore() < right.getScore()){
            if(!contains(right, q))q.add(right);
            if(!contains(left, q))q.add(left);
        }else{
            if(!contains(left, q))q.add(left);
            if(!contains(right, q))q.add(right);
        }

        help(left, q);
        help(right, q);

    }

    private boolean contains(Node n, ArrayList<Node> q){

        for(Node no: q){
            if(no.getPlayer().equals(n.getPlayer()))
                return true;
        }
        return false;

    }


    @Override
    public IBinaryTree getCompetitionTree() {
        return root;
    }

    public Stack<Node> getStack() {
        return stack;
    }

    public Node getRoot() {
        return root;
    }

    public MatchExtend getLastMatch() {
        return lastMatch;
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
