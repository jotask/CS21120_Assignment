package cs21120.assignment.solution;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class BTSingleElimjov2 implements IManager{

    @Override
    public void setPlayers(ArrayList<String> players) {

    }

    @Override
    public boolean hasNextMatch() {
        return false;
    }

    @Override
    public Match nextMatch() throws NoNextMatchException {
        return null;
    }

    @Override
    public void setMatchScore(int p1, int p2) {

    }

    @Override
    public String getPosition(int n) {
        return null;
    }

    @Override
    public IBinaryTree getCompetitionTree() {
        return null;
    }

    public static void main(String[] args) {
        String file = "";
        CompetitionManager cm = new CompetitionManager(new BTSingleElimjov2());
        try {
            cm.runCompetition(file);
        } catch (FileNotFoundException e) {
            System.err.println("FILE NOT FOUND");
            e.printStackTrace();
        }
    }



}
