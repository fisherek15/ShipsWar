import java.util.*;

public class Board {

    //<<Size of ship, Available ships>>
    private Map<Integer, Integer> availableShips;
    private String[][] board = new String[10][10];
    private String[][] opponentBoard = new String[10][10];
    private int sunkenShips;

    public Board() {
        this.availableShips = Map.of(
                1,4,
                2,3,
                3,2,
                4,1
        );
        this.board = resetArray(this.board);
        this.opponentBoard = resetArray(this.opponentBoard);
        this.sunkenShips = 0;
    }

    private String[][] resetArray(String[][] array){
        Arrays.stream(array)
                .forEach(a -> Arrays.fill(a, "~"));
        return  array;
    }

    public String processCommand(String command){
        return null;
    }

    private boolean setOrRemoveShip(Point point1, Point point2, Boolean addShip) {

        int shipSize = (int) Math.sqrt(Math.pow(point1.getX() - point2.getX(), 2) + Math.pow(point1.getY() - point2.getY(), 2));
        int x1 = point1.getX();
        int y1 = point1.getY();
        int x2 = point2.getX();
        int y2 = point2.getY();
        String signToCheck;
        String signToSet;

        if(addShip){
            signToCheck = "~";
            signToSet = "X";
        } else {
            signToCheck = "X";
            signToSet = "~";
        }

        if (isShipAvailable(shipSize)) {
            if (x1 == x2 && y1 == y2) {
                if(!board[y1][x1].equals(signToCheck)){
                    return false;
                }
                board[y1][x1] = signToSet;
            } else if (x1 == x2) {
                if (y1 < y2) {
                    for (int i = y1; i <= y2; i++) {
                        if(!board[i][x1].equals(signToCheck)){
                            return false;
                        }
                    }
                    for (int i = y1; i <= y2; i++) {
                        board[i][x1] = signToSet;
                    }
                } else {
                    for (int i = y2; i <= y1; i++) {
                        if(!board[i][x1].equals(signToCheck)){
                            return false;
                        }
                    }
                    for (int i = y2; i <= y1; i++) {
                        board[i][x1] = signToSet;
                    }
                }
            } else if (y1 == y2){
                if (x1 < x2) {
                    for (int i = x1; i <= x2; i++) {
                        if(!board[y1][i].equals(signToCheck)){
                            return false;
                        }
                    }
                    for (int i = x1; i <= x2; i++) {
                        board[y1][i] = signToSet;
                    }
                } else {
                    for (int i = x2; i <= x1; i++) {
                        if(!board[y1][i].equals(signToCheck)){
                            return false;
                        }
                    }
                    for (int i = x2; i <= x1; i++) {
                        board[y1][i] = signToSet;
                    }
                }
            } else return false;
            this.availableShips.replace(shipSize, this.availableShips.get(shipSize) - 1);
            return true;
        }
        return false;
    }

    private boolean areAllShipOnPlace(){
        if(this.availableShips.values().stream().mapToInt(i->i).sum() == 0){
            return true;
        }
        return false;
    }

    private boolean isShipAvailable(int size){
        if(availableShips.get(size) > 0){
            return true;
        }
        return false;
    }

    private boolean checkShotAndSetField(Point shot){
        int x = shot.getX();
        int y = shot.getY();
        if(board[y][x].equals("X")) {
            board[y][x] = "#";
            return true;
        } else {
            board[y][x] = "-";
            return false;
        }
    }

    private Point makePoint(String point){
        return new Point(point);
    }

}
