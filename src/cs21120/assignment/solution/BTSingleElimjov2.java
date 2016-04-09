package cs21120.assignment.solution;

import cs21120.assignment.*;

import java.io.FileNotFoundException;
import java.util.*;

/**
 *
 * To initialize the tree we call recursively the function with the list and the root node. On each call we split
 * the list into two list, and re-call this function with the left node and the right node from the parent node, until
 * the list only contains one unique element. If have one element we create a leaf node for the tree. When the tree is
 * initialize the stack is build to know how many matches we have to do.
 *
 * I extended the match class to store the node that holds both teams, so it make more easy to get matches. With
 * this, this class have the logic for set the score and promote the winner to his parent.
 *
 * Each internal node on the tree represent a match with two adversaries. We have n-1 internal nodes on the tree
 * and n leaf nodes. For each match the winner is promoted to his parent.
 *
 * To know if we have more matches to do, we only need to know if the stack is empty. If its empty we don't have
 * match to do.
 *
 * Time complexity:
 *      - Time to play a match: 0(1) because we need only to pick the first node from the stack .This node contains
 *              both teams for the match
 *      - Initialize the tree: O(n) depends on how many teams we got for build the tree.
 *      - Set match scores: O(1) because we have a references from the last match played.
 *      - Get position O(n) because we need to iterate over the tree for find this position.
 *      - Get competition tree: O(1) return the root of the tree.
 *
 *
 *********************************************
 ************** Self Evaluation **************
 *********************************************
 *
 * Build the tree:
 *      My code to build the tree is called recursively. I'm quite happy with the result of this algorithm.
 *
 * Build the matches (stack):
 *      I'm happy with this algorithm too. It builds the stack nicely and have some check to possible errors.
 *
 * Has next Match, nextMatch, getCompetitionTree, and SetScores:
 *      Nothing to comment on this parts. Those are simple methods. Nothing to be proud.
 *
 * GetPosition:
 *      This was one of the most difficult methods for this assignment. And I'm not too much happy with
 *      the end up result because every time you want to know a position of one player, you need to create
 *      a queue and a new stack to build the position and iterate over the tree. This can be expensive
 *      for massive trees with long depths. But I didn't find a better way to solve this problem.
 *
 * Overall:
 *      This assignment was difficult to understand at the beginning how to achieve what we have asked
 *      for this assignment. But I manage to end up with a final solution. First, I tried to complete this
 *      assignment without any inheritance class. But was not very happy as it was developing the assignment.
 *      So I delete everything and I decide to do this assignment with inheritance to make more sense to the
 *      final result. I extend the class Match to hold more information because to me it makes sense for the
 *      final implementation. I think my work is between 65 and 75% if it works fine. I made it some unit testing
 *      to confirm it works, but I did the same for the first assignment for this module but when I used the JUnit
 *      provided by the lecturer I have many errors in my code. So I'm no really confident about this assignment.
 *      But it has passed my JUnit.
 *
 * @author Jose Vives Iznardo
 *
 */
public class BTSingleElimJov2 implements IManager {

    /**
     * Stack that holds all the nodes in the tree. With this we know how many match we need to run
     * for this competition.
     */
    private Stack<Node> stack;

    /**
     * Variable to know how many teams we have in all competition
     */
    private int competitionSize;

    /**
     * The Root node fro the three
     */
    private Node root;

    /**
     * The last match played
     */
    private MatchExtend lastMatch;

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

    /**
     *
     * Firstly, the root node is initialized. When whe have the root node, we call the method that is called recursively
     * for create the tree. When the tree is build, the stack its created.
     *
     * @param players the players or teams
     */
    @Override
    public void setPlayers(ArrayList<String> players) {

        // Initialize the root
        this.root = new Node(null);

        // Build the tree
        addToTree(root, players);

        // Build
        this.stack = buildMatches();

        // Know the size of this competition
        this.competitionSize = stack != null ? stack.size() : 0;

    }

    /**
     * This method is called recursively for build the competition tree.
     * Each time we check if we have only one time, if we have only one time left on the List, means that this
     * is going to be a leaf node. If not, we split the list into two list, and we send one list to the left and
     * the other to the right node of the current no.
     *
     * @param n
     *          The node we are currently on the tree
     *
     * @param players
     *          The list of players for this sub-part of the tree
     */
    private void addToTree(Node n, List<String> players) {

        // Check if we have only one player on the list
        if (players.size() == 1) {
            // If I have only one player on the list, set this node to this player and exit the function
            n.setPlayer(players.get(0));
            return;
        } else if (players.isEmpty()) {
            // If the list is empty is something wrong
            System.out.println("Something is wrong because the List is empty!");
            return;
        }

        // Find the middle of the list to know where we need to split the list
        int middle = players.size() / 2;

        // Split he list into two different lists
        List<String> left, right;
        left = new ArrayList<>(players.subList(0, middle));
        right = new ArrayList<>(players.subList(middle, players.size()));

        // Check if the children nodes are empty for initialize them
        if (n.left == null) n.left = new Node(n);
        if (n.right == null) n.right = new Node(n);

        // Recursively call this function with the divided list of name players
        addToTree(n.left, left);
        addToTree(n.right, right);

    }

    /**
     *  This method creates and store each match that needs to play. The tree its iterate from top to bottom, and
     *  each node is added to the queue. If the node is a leaf node, this node is added to the stack.
     *
     * @return
     *      The stack with all the matches that are going to run on this competition
     */
    private Stack<Node> buildMatches() {

        // Create a stack with all nodes
        Stack<Node> tempStack = new Stack<>();

        // Create a queue for transverse all three
        Queue<Node> queue = new LinkedList<>();

        // check if the root is null
        if (root == null) {
            System.err.println("root is null");
            return null;
        }

        // Add the root to the
        queue.offer(root);

        // Create all the matches
        while (!queue.isEmpty()) {

            // Get the first node
            Node tmp = queue.poll();

            // Check if left node is not null and and add left node to the queue
            if (tmp.left != null)
                queue.offer(tmp.left);

            // Check if right node is not null and and add right node to the queue
            if (tmp.right != null)
                queue.offer(tmp.right);

            // If have children add this node to the stack. Means this match has not been played yet
            if (tmp.left != null && tmp.right != null) {
                tempStack.add(tmp);
            }

        }

        // Return the stack with all the nodes
        return tempStack;

    }

    /**
     *
     * Just know if we have more matches to play. To know if we have more matches we only need to know if the
     * stack its not empty.
     *
     * @return
     *      If the stack is not empty
     */
    @Override
    public boolean hasNextMatch() {
        // Just return if the stack is not empty
        return !this.stack.isEmpty();
    }

    /**
     * Return a match created. To create a match we only need to pop the first node from the stack and build the match
     * from his two players. This match created its stored as a reference.
     *
     * @return
     *      The match that is going to be played
     *
     * @throws NoNextMatchException
     *      We don't have any match
     */
    @Override
    public Match nextMatch() throws NoNextMatchException {

        // Check if we have more matches
        if (!hasNextMatch())
            throw new NoNextMatchException("NoNextMatchException");

        // Get the first element on the stack
        Node n = stack.pop();

        // Create a match for this two player and store this match
        lastMatch = new MatchExtend(n.left, n.right);

        // Return the match
        return lastMatch;

    }

    /**
     * Set the score from the las match played. This his hold by the matchextend class, because for me does not have
     * any sense to this class have the logic for a match.
     *
     * @param p1
     *      The score of player 1
     * @param p2
     *      The score of player 2
     */
    @Override
    public void setMatchScore(int p1, int p2) {
        lastMatch.setMatchScore(p1, p2);
    }

    @Override
    public String getPosition(int n) {

        // If the position we want is greater that the number of competitors return null
        if (n > competitionSize)
            return null;

        // Temp stack and queue to hold data
        Stack<String> s = new Stack<>();
        Queue<Node> q = new LinkedList<>();

        // Add the root of the tree to the queue
        q.offer(root);

        // Know the position we are in each moment
        int position = 0;

        // While que Queue is not empty
        while (!q.isEmpty()) {

            // if the position is greater that the position we want exit the loop
            if (position > n) break;

            // Get the current node
            Node tmp = q.poll();

            // Check if left node is not null and and add left node to the queue
            if (tmp.left != null)
                q.offer(tmp.left);

            // Check if right node is not null and and add right node to the queue
            if (tmp.right != null)
                q.offer(tmp.right);

            // Check if is not null or if we don't have this team on our list
            String player = tmp.getPlayer();

            if (player != null && !s.contains(player)) {
                // If we are on the position we want return this player
                if (n == position)
                    return player;

                // Add this player to the stack
                s.add(player);

                // Increment the position
                position++;
            }

        }

        // Return null because we did't find anything
        return null;

    }

    /**
     * Get the root of the tree.
     *
     * @return
     *      The node that is the root of the tree
     */
    @Override
    public IBinaryTree getCompetitionTree() {
        return root;
    }

}

/**
 * Class that implements the IBinatyTree interface provided. Store information for each node in the tree
 */
class Node implements IBinaryTree {

    /**
     * The parent node of this node. Its final because can't be change
     */
    private final Node parent;

    /**
     * The left children of this node
     */
    Node left;

    /**
     * The right children of this node
     */
    Node right;

    /**
     * The team name for this node
     */
    private String player;

    /**
     * The score variable for this node
     */
    private int score;

    /**
     * Constructor for a node.
     *
     * @param parent
     *      The parent of this node
     */
    Node(Node parent) {
        this.parent = parent;
    }

    /**
     * Get the left node
     *
     * @return
     *      The left node
     */
    @Override
    public IBinaryTree getLeft() {
        return left;
    }

    /**
     * Get the right node
     *
     * @return
     *      The right node
     */
    @Override
    public IBinaryTree getRight() {
        return right;
    }

    /**
     * Get the player name for this node
     *
     * @return
     *      The player name
     */
    @Override
    public String getPlayer() {
        return player;
    }

    /**
     * Get the score for this team
     *
     * @return
     *      The score for this team
     */
    @Override
    public int getScore() {
        return score;
    }

    /**
     * Set the team name for this node
     *
     * @param player
     *      The team name for this node
     */
    void setPlayer(String player) {
        this.player = player;
    }


    /**
     * Set the score for the this node
     *
     * @param score
     *      The score value we want to set
     */
    void setScore(int score) {
        this.score = score;
    }

    /**
     * Get the parent of this node
     *
     * @return
     *      The parent node
     */
    Node getParent() {
        return parent;
    }

}

/**
 * This class extends class for hold the node know which nodes are going to make a match. So in each match we
 * can know more information about the match
 */
class MatchExtend extends Match {

    /**
     * Player one that is going to make a match with payer two
     */
    private final Node p1;

    /**
     * Player two that is going to make a match with player one
     */
    private final Node p2;

    /**
     * Package local constructor
     * @param player1
     *          The team name
     * @param player2
     *          The other team's name
     */
    MatchExtend(Node player1, Node player2) {
        super(player1.getPlayer(), player2.getPlayer());
        this.p1 = player1;
        this.p2 = player2;
    }

    /**
     * Set the score when the match has finished.
     * First the score it's set to this node, later check which team has won thi match, and we promote the winner
     * team to the parent node
     *
     * @param p1
     *          The score for the first team
     * @param p2
     *          The score for the second team
     */
    void setMatchScore(int p1, int p2) {

        // Set the score for both teams
        this.p1.setScore(p1);
        this.p2.setScore(p2);

        // Check which player has won the match and decide who is going  to play the next match
        if (p2 < p1) {
            Node parent = this.p1.getParent();
            if(parent != null)
                parent.setPlayer(this.p1.getPlayer());
        } else {
            Node parent = this.p2.getParent();
            if(parent != null)
                parent.setPlayer(this.p2.getPlayer());
        }

    }

}
