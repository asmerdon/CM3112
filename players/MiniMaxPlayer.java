/*
 * Running the experiment 20 times, my implementation of minimax won 4 times.
 */
package players;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;
import snake.GameState;
import snake.Snake;
import static players.RandomPlayer.rand;

/**
 *
 * @author steven
 */
public class MiniMaxPlayer extends RandomPlayer {

    Map<Position, Integer> positions;

    public MiniMaxPlayer(GameState state, int index, Snake game) {
        super(state, index, game);
    }

    public void doMove() {
        initPositions();

		Node currentPosition = new Node(state.getPlayerX(index).get(0), state.getPlayerY(index).get(0), state.getTargetX(), state.getTargetY());

		Node yMinusOne = new Node(state.getPlayerX(index).get(0), state.getPlayerY(index).get(0)-1, state.getTargetX(), state.getTargetY()); //north
		Node xPlusOne = new Node(state.getPlayerX(index).get(0)+1, state.getPlayerY(index).get(0), state.getTargetX(), state.getTargetY()); // west
		Node yPlusOne = new Node(state.getPlayerX(index).get(0), state.getPlayerY(index).get(0)+1, state.getTargetX(), state.getTargetY()); //south
		Node xMinusOne = new Node(state.getPlayerX(index).get(0)-1, state.getPlayerY(index).get(0), state.getTargetX(), state.getTargetY()); // east
	
		List<Double> moves = new ArrayList<Double>();
		
		double yMinusOneMM = getMinScore(currentPosition, yMinusOne);
		moves.add(yMinusOneMM);
		double xPlusOneMM = getMinScore(currentPosition, xPlusOne);
		moves.add(xPlusOneMM);
		double yPlusOneMM = getMinScore(currentPosition, yPlusOne);
		moves.add(yPlusOneMM);
		double xMinusOneMM = getMinScore(currentPosition, xMinusOne);
		moves.add(xMinusOneMM);
		int chosenY = 0;
		int chosenX = 0;
		
		/*System.out.println("yMinusOne minmax value:");
		System.out.println(yMinusOneMM);
		System.out.println("xPlusOne minmax value:");
		System.out.println(xPlusOneMM);
		System.out.println("yPlusOne minmax value:");
		System.out.println(yPlusOneMM);
		System.out.println("xMinusOne minmax value:");
		System.out.println(xMinusOneMM);*/
		
		if (yMinusOneMM == 1.0) {
			state.setOrientation(1, 1);
		}
		else if (xPlusOneMM == 1.0) {
			state.setOrientation(1, 2);
		}
		else if (yPlusOneMM == 1.0) {
			state.setOrientation(1, 3);
		}
		else if (xMinusOneMM == 1.0) {
			state.setOrientation(1, 4);
		}
		else {
			System.out.println("minimax player doing random move"); //random move done if no direction found.
			doRandomMove();
		}
	}
    	
	private double getMinScore(Node currentNode, Node searchNode) {
		double res = Double.POSITIVE_INFINITY;
		boolean occupied = isOccupied(searchNode.x, searchNode.y, 1); //checks if search node is occupied by snake
		if(occupied == true) { //if it is occupied by snake, minimax returns -1, which means path should not be taken.
			res = -1;
			return res;
		}
		else {
			res = getMaxScore(currentNode, searchNode);
		}
		return res;
	}
	
	private double getMaxScore(Node currentNode, Node searchNode) {
		double res = Double.NEGATIVE_INFINITY; //what is returned
		double currentCompareX = currentNode.x - state.getTargetX(); 
		double searchCompareX = searchNode.x - state.getTargetX();
		double currentCompareY = currentNode.y - state.getTargetY();
		double searchCompareY = searchNode.y - state.getTargetY();

		if((currentCompareX > 0) && (searchCompareX > 0)) {
			if(currentCompareX > searchCompareX) {
				//search is closer
				res = 1;
				return res; 
			}
		}
		else if((currentCompareX < 0) && (searchCompareX < 0))
		{
			if(currentCompareX < searchCompareX) {
				//search is closer but a negative number
				res = 1;
				return res;
			}
		}

		if((currentCompareY > 0) && (searchCompareY > 0)) {
			if(currentCompareY > searchCompareY) {
				//search is closer
				res = 1;
				return res; 
			}
		}
		else if((currentCompareY < 0) && (searchCompareY < 0))
		{
			if(currentCompareY < searchCompareY) {
				//search is closer but a negative number
				res = 1;
				return res;
			}
		}
		res = 0;
		return res;
	}
	

    private void initPositions() {
        positions = new HashMap();
        for (int i = 0; i < state.getNrPlayers(); i++) {
            if (!state.isDead(i)) {
                List<Integer> xList = state.getPlayerX(i);
                List<Integer> yList = state.getPlayerY(i);
                for (int j = 0; j < xList.size(); j++) {
                    positions.put(new Position(xList.get(j), yList.get(j)), xList.size() - j);
                }
            }
        }
    }

    
    private boolean isOccupied(int x, int y, int step) {
        if (x < 0 || x >= state.getWidth() || y < 0 || y >= state.getHeight()) {
			//System.out.println(true);
            return true;
        }
        else if (!state.isOccupied(x, y)) {
			//System.out.println(false);
            return false;
        }
        int cutoff = positions.get(new Position(x, y));
        return step <= cutoff;
    }
}
