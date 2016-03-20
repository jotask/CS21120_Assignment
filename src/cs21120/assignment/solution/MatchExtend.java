package cs21120.assignment.solution;

import cs21120.assignment.provided.Match;

public class MatchExtend extends Match {

    public Node p1, p2;

    public MatchExtend(Node player1, Node player2) {
        super(player1.getPlayer(), player2.getPlayer());
        p1 = player1;
        p2 = player2;
    }

}
