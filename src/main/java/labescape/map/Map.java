package labescape.map;

import labescape.model.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Map {

    Random rand = new Random();

    int currentRow, currentColumn;
    private Direction previousDirection;
    private Room elevatorRoom;

    Room[][] board = new Room[13][13];


    public enum Direction{
        NORTH("N", "NORTH"),
        SOUTH("S", "SOUTH"),
        EAST("E", "EAST"),
        WEST("W", "WEST");

        private final String abbreviation;
        private final String fullName;

        Direction(String abbreviation, String fullName) {
            this.abbreviation = abbreviation;
            this.fullName = fullName;
        }

        public String getAbbreviation(){
            return this.abbreviation;
        }

        public String getFullName() {
            return fullName;
        }
    }

    public void move(Direction direction) {
        if (this.getExits().contains(direction)) {

            board[currentRow][currentColumn].setExplored(true);
            board[currentRow][currentColumn].setCurrent(false);


            if (direction == Direction.NORTH) {
                currentRow -= 1;
                previousDirection = Direction.SOUTH;
            } else if (direction == Direction.SOUTH) {
                currentRow += 1;
                previousDirection = Direction.NORTH;
            } else if (direction == Direction.WEST) {
                currentColumn -= 1;
                previousDirection = Direction.EAST;
            } else {
                currentColumn += 1;
                previousDirection = Direction.WEST;
            }
            board[currentRow][currentColumn].setCurrent(true);
            System.out.println("You move " + direction);
            displayBoard();
        } else {
            System.out.println("There's nothing in that direction.");
        }
    }

    public void getStartCords() {
            currentColumn = rand.nextInt(board.length);
            currentRow = 12;
            var current = board[currentRow][currentColumn];
            current.getStartingRoom();
            current.setCurrent(true);
            getElevatorCoords();

    }

    public void getElevatorCoords(){
        int elevatorColumn = rand.nextInt(board.length);
        int elevatorRow = 0;
        var elevatorRoom = board[elevatorRow][elevatorColumn];
        elevatorRoom.setElevator(true);
        this.elevatorRoom = elevatorRoom;

    }


    public List<Direction> getExits(){

        List<Direction> exitList = new ArrayList<>();

        int lowerBound = 0;
        int upperBound = board.length;
        int westTrial = currentColumn - 1;
        int eastTrial = currentColumn + 1;
        int northTrial = currentRow - 1;
        int southTrial = currentRow + 1;


        if ((westTrial >= lowerBound && westTrial < upperBound)
                && !board[currentRow][westTrial].isObstacle()) {

                exitList.add(Direction.WEST);

        }

        if ((southTrial >= lowerBound && southTrial < upperBound)
                && !board[southTrial][currentColumn].isObstacle()) {

                exitList.add(Direction.SOUTH);

            }



        if ((eastTrial >= lowerBound && eastTrial < upperBound)
                && !board[currentRow][eastTrial].isObstacle()) {

            exitList.add(Direction.EAST);
        }


        if ((northTrial >= lowerBound && northTrial < upperBound)
                && !board[northTrial][currentColumn].isObstacle()){

                exitList.add(Direction.NORTH);
        }

        return exitList;
    }



    public void generateBoard() {
        int rand;
        for (int row = 0; row < board.length; row++) {
            for (int column = 0; column < board[row].length; column++) {
                rand = (int) (Math.random() * 3);
                if(rand == 0) {
                    board[row][column] = new Room(true);
                }else {
                    board[row][column] = new Room();
                }
            }
        }

    }
    public void displayBoard() {
        System.out.println();
        System.out.println("MAP");
        for (int row = 0; row < board.length; row++) {
            for (int column = 0; column < board[row].length; column++) {
                if(board[row][column].isCurrent()) {
                    printArrow(previousDirection);
                }else if(board[row][column].isElevator()) {
                    System.out.print(" E ");
                }else if(board[row][column].isExplored()) {
                    System.out.print(" * ");
                }else if(board[row][column].isObstacle()) {
                    System.out.print(" X ");
                }else if(!board[row][column].isObstacle()) {
                    System.out.print(" _ ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    private void printArrow(Direction previousDirection) {

        if (previousDirection == null || previousDirection == Direction.SOUTH) {
            System.out.print(" ▲ "); //unicode U+25B2

        } else if( previousDirection == Direction.WEST){
            System.out.print(" ▶ "); //unicode U+25C0

        } else if( previousDirection == Direction.EAST ){
            System.out.print(" ◀ "); // unicode U+25C0

        } else {
            System.out.print(" ▼ "); // unicode U+25BC
        }

    }

    public Room getCurrentRoom() {
        return board[currentRow][currentColumn];
    }

    public Room getElevatorRoom() {
        return elevatorRoom;
    }
}
