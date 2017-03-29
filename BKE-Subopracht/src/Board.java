

import java.util.ArrayList;

/**
 * Created by vincent on 28-3-2017.
 */
public class Board {

    private ArrayList<ArrayList<Cell>> board;


    public Board(){
        board = new ArrayList<ArrayList<Cell>>();
        board.add(new ArrayList<>());
        board.add(new ArrayList<>());
        board.add(new ArrayList<>());

    }

    private void fillArrays(){
        for(ArrayList<Cell> array : board){
            for(int i = 0; i< 3;i++){
                array.add(new Cell());
            }
        }
    }

    private void count(){
        int count = 0;
        for(int i = 0; i< board.size();i++){
            for(int j = 0; j < board.size();j++){
                count += 1;
                //System.out.println(j);
            }
        }
        System.out.println(count);
    }

    public void setPosition(int xPos, int yPos,Player newPlayer){
        Cell cell = board.get(xPos).get(yPos);
        cell.setPlayer(newPlayer);
    }

    public String getTokenFromPosition(int xPos, int yPos){
        return getPlayerAtPosition(xPos,yPos).getSymbol();
    }

    public void test(){
        fillArrays();
        count();

    }

    public Player getPlayerAtPosition(int xPos, int yPos) {
        return board.get(xPos).get(yPos).getPlayer();

    }

}