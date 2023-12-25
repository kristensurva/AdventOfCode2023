import UtilityClasses.Coordinates;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Day10 {
    static char[][] map = null;
    static String northConnectingPipes = "|LJ";
    static String southConnectingPipes = "|7F";
    static String westConnectingPipes =  "-J7";
    static String eastConnectingPipes =  "-LF";
    public static int solveTask1(List<String> input) {
        map = processInput(input);
        char[][] markedMap = processInput(input);
        //printMapWithPosition(getStartPosition());
        Coordinates previousPosition = getStartPosition();
        Coordinates currentPosition = getStartingDirection(previousPosition);
        markedMap[currentPosition.row][currentPosition.col] = '*';
        int steps = 1;
        while (map[currentPosition.row][currentPosition.col]!='S') {
            //printMapWithPosition(currentPosition, markedMap);
            Coordinates temp = currentPosition;
            currentPosition = getNextDirection(currentPosition, previousPosition);
            previousPosition = temp;
            steps++;
            markedMap[currentPosition.row][currentPosition.col] = '*';
        }
        printMapWithPosition(currentPosition, markedMap);
        return steps/2;
    }

    public static int solveTask2(List<String> input) {
        map = processInput(input);
        replaceStartOnMap();
        char[][] markedMap = processInputPretty(input);
        ArrayList<ArrayList<Coordinates>> allConnectedPipeLoops = new ArrayList<>();
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[row].length; col++) {
                char c = map[row][col];
                if (c != '.' && c!=',' && markedMap[row][col]!='#') {
                    ArrayList<Coordinates> pipeLoop = getEnclosedPipeLoop(new Coordinates(row, col));
                    if (pipeLoop.size()>0) {
                        allConnectedPipeLoops.add(pipeLoop);
                        for (Coordinates coordinates : pipeLoop) {
                            markedMap[coordinates.row][coordinates.col] ='#';
                        }
                    }
                }
            }
        }
        ArrayList<Coordinates> mainPipeLoop = null;
        // Convert all the 4 length pipes to ground too
        for (ArrayList<Coordinates> connectedPipeLoop : allConnectedPipeLoops) {
            if (connectedPipeLoop.size()==4){
                for (Coordinates coordinates : connectedPipeLoop) {
                    map[coordinates.row][coordinates.col]='.';
                }
            }
            else {
                mainPipeLoop = connectedPipeLoop;
            }
        }
        printMapWithPosition(new Coordinates(-1,-1), processInputPretty(input));
        travelAlongPipeAndMarkOutwardSpots(mainPipeLoop);
        fillOutAllOuterSpots();
        int enclosedTiles = 0;
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[row].length; col++) {
                if (map[row][col]=='.') {
                    enclosedTiles++;
                    map[row][col]='I';
                }
            }
        }
        printMapWithPosition(new Coordinates(-1,-1), map);
        return enclosedTiles;
    }

    static void replaceStartOnMap() {
        String possiblePipes = "|-LJ7F";
        Coordinates startPosition = getStartPosition();
        ArrayList<String> suitableConnectionTypes = new ArrayList<>();
        if (southConnectingPipes.indexOf(map[startPosition.row-1][startPosition.col])!=1) {
            suitableConnectionTypes.add(southConnectingPipes);
        }
        if (northConnectingPipes.indexOf(map[startPosition.row+1][startPosition.col])!=1) {
            suitableConnectionTypes.add(northConnectingPipes);
        }
        if (westConnectingPipes.indexOf(map[startPosition.row][startPosition.col-1])!=1) {
            suitableConnectionTypes.add(westConnectingPipes);
        }
        if (eastConnectingPipes.indexOf(map[startPosition.row][startPosition.col+1])!=1) {
            suitableConnectionTypes.add(eastConnectingPipes);
        }
        for (char pipeType : possiblePipes.toCharArray()) {
            int connectionCount = 0;
            for (String suitableConnectionType : suitableConnectionTypes) {
                if (suitableConnectionType.indexOf(pipeType)!=1) connectionCount++;
            }
            if (connectionCount==2) {
                map[startPosition.row][startPosition.col] = pipeType;
                return;
            }
        }

    }

    static void fillOutAllOuterSpots() {
        ArrayList<Coordinates> startingOuterPosition = new ArrayList<>();
        //Count all O's and remember position
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[0].length; col++) {
                if (map[row][col]=='O') {
                    startingOuterPosition.add(new Coordinates(row, col));
                }
            }
        }
        for (Coordinates position : startingOuterPosition) {
            recursiveFillOutAllOuterSpots(position);
        }
    }

    static void recursiveFillOutAllOuterSpots(Coordinates position) {
        // Base case: if no direction has a fillable spot, exit branch
        Coordinates up = new Coordinates(position.row -1, position.col);
        Coordinates down = new Coordinates(position.row + 1, position.col);
        Coordinates left = new Coordinates(position.row, position.col - 1);
        Coordinates right = new Coordinates(position.row , position.col + 1);
        if (up.row >= 0 && map[up.row][up.col]=='.') {
            map[up.row][up.col] = 'O';
            recursiveFillOutAllOuterSpots(new Coordinates(up.row, up.col));
        }
        if (down.row < map.length && map[down.row][down.col]=='.') {
            map[down.row][down.col] = 'O';
            recursiveFillOutAllOuterSpots(new Coordinates(down.row, down.col));
        }
        if (left.col >= 0 && map[left.row][left.col]=='.') {
            map[left.row][left.col] = 'O';
            recursiveFillOutAllOuterSpots(new Coordinates(left.row, left.col));
        }
        if (right.col < map[0].length && map[right.row][right.col]=='.') {
            map[right.row][right.col] = 'O';
            recursiveFillOutAllOuterSpots(new Coordinates(right.row, right.col));
        }
    }

    static ArrayList<Coordinates> getEnclosedPipeLoop(Coordinates startingPosition) {
        // Not only does it return a pipe loop if its a loop, it will also clean all non looping pipes, by changing the map!
        ArrayList<Coordinates> connectedPipes = new ArrayList<>();
        connectedPipes.add(startingPosition);
        Coordinates currentPosition = getNextDirection(startingPosition, startingPosition);
        int steps = 1;
        while (!currentPosition.equals(startingPosition)) {
            //printMapWithPosition(currentPosition);
            // If pipe isn't looping, replace all the pipes with ground and return
            if (currentPosition.row==-1) {
                for (Coordinates connectedPipe : connectedPipes) {
                    map[connectedPipe.row][connectedPipe.col] = '.';
                }
                return new ArrayList<>();
            }
            connectedPipes.add(currentPosition);
            currentPosition = getNextDirection(currentPosition, connectedPipes.get(steps-1));
            steps++;
        }
        return connectedPipes;
    }

    static void travelAlongPipeAndMarkOutwardSpots(ArrayList<Coordinates> pipeLoop) {
        // Hardcoding the direction, we should always start with lefthand being towards the outside
        Coordinates toTheLeftEast = new Coordinates(0, -1); // Means we're facing North
        Coordinates toTheLeftNorth = new Coordinates(-1, 0);// Means we're facing West
        Coordinates toTheLeftWest = new Coordinates(0, 1); // Means we're facing South
        Coordinates toTheLeftSouth = new Coordinates(1, 0); // Means we're facing East
        // If we return right, we increment index by 1, if left, subtract.
        ArrayList<Coordinates> rotations = new ArrayList<>();
        rotations.add(toTheLeftEast);
        rotations.add(toTheLeftNorth);
        rotations.add(toTheLeftWest);
        rotations.add(toTheLeftSouth);
        int rotationIndex = 1;
        for (int i = 1; i < pipeLoop.size()-1; i++) {
            Coordinates currentPipe = pipeLoop.get(i);
            Coordinates currentPipeAdjacent = Coordinates.sum(currentPipe, rotations.get(rotationIndex));
            Coordinates previousPipe = pipeLoop.get(i-1);
            Coordinates previousPipeAdjacent = Coordinates.sum(previousPipe, rotations.get(rotationIndex));
            if (currentPipeAdjacent.row >= 0 && currentPipeAdjacent.row < map.length && currentPipeAdjacent.col >= 0 && currentPipeAdjacent.col < map[0].length && map[currentPipeAdjacent.row][currentPipeAdjacent.col]=='.') {
                map[currentPipeAdjacent.row][currentPipeAdjacent.col]='O';
            }
            if (previousPipeAdjacent.row >= 0 && previousPipeAdjacent.row < map.length && previousPipeAdjacent.col >= 0 && previousPipeAdjacent.col < map[0].length && map[previousPipeAdjacent.row][previousPipeAdjacent.col]=='.') {
                map[previousPipeAdjacent.row][previousPipeAdjacent.col]='O';
            }
            // If there's a turn
            if (map[currentPipe.row][currentPipe.col]!='|' && map[currentPipe.row][currentPipe.col]!='-') {
                Coordinates nextPipe = pipeLoop.get(i+1);
                // Bigger than 0 turn right, smaller than 0 turn left
                rotationIndex = ((rotationIndex + crossProductOf3Points(previousPipe, currentPipe, nextPipe))+rotations.size()) % rotations.size();
                currentPipeAdjacent = Coordinates.sum(currentPipe, rotations.get(rotationIndex));
                if (currentPipeAdjacent.row >= 0 && currentPipeAdjacent.row < map.length && currentPipeAdjacent.col >= 0 && currentPipeAdjacent.col < map[0].length && map[currentPipeAdjacent.row][currentPipeAdjacent.col]=='.') {
                    map[currentPipeAdjacent.row][currentPipeAdjacent.col]='O';
                }
            }
        }
    }

    static int crossProductOf3Points(Coordinates A, Coordinates B, Coordinates C) {
        Coordinates AB = new Coordinates(A.row-B.row, B.col-A.col);
        Coordinates BC = new Coordinates(B.row-C.row, C.col-B.col);
        return AB.row*BC.col-AB.col*BC.row;
    }


    static void printMapWithPosition(Coordinates position, char[][] map) {
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[row].length; col++) {
                if (position.row==row && position.col==col) {
                    System.out.print('*');
                }
                else System.out.print(map[row][col]);
            }
            System.out.println();
        }
        System.out.println();
    }

    static char[][] processInput(List<String> input) {
        char[][] map = new char[input.size()][input.get(0).length()];
        for (int row = 0; row < input.size(); row++) {
            for (int col = 0; col < input.get(0).length(); col++) {
                map[row][col] = input.get(row).charAt(col);
            }
        }
        return map;
    }

    static char[][] processInputPretty(List<String> input) {
        char[][] map = new char[input.size()][input.get(0).length()];
        for (int row = 0; row < input.size(); row++) {
            for (int col = 0; col < input.get(row).length(); col++) {
                char c;
                if (input.get(row).charAt(col)=='|') {
                    c = '│';
                }
                else if (input.get(row).charAt(col)=='-') {
                    c = '─';
                }
                else if (input.get(row).charAt(col)=='L') {
                    c = '└';
                }
                else if (input.get(row).charAt(col)=='J') {
                    c = '┘';
                }
                else if (input.get(row).charAt(col)=='7') {
                    c = '┐';
                }
                else if (input.get(row).charAt(col)=='F') {
                    c = '┌';
                }
                else {
                    c = input.get(row).charAt(col);
                }
                map[row][col] = c;
            }
        }
        return map;
    }


    static Coordinates getStartPosition() {
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map.length; col++) {
                if (map[row][col]=='S') {
                    return new Coordinates(row, col);
                }
            }
        }
        return new Coordinates(-1, -1);
    }

    static Coordinates getStartingDirection(Coordinates position) {
        // This method is greedy and will return the first suitable direction it finds.
        // Checking north
        if (position.row > 0 && northConnectingPipes.indexOf(map[position.row-1][position.col])!=-1) {
            return new Coordinates(position.row-1, position.col);
        }
        // Checking south
        if (position.row < map.length-1 && southConnectingPipes.indexOf(map[position.row+1][position.col])!=1) {
            return new Coordinates(position.row+1, position.col);
        }
        // Checking west
        if (position.col > 0 && westConnectingPipes.indexOf(map[position.row][position.col-1])!=1) {
            return new Coordinates(position.row, position.col-1);
        }
        // Checking east
        if (position.col < map.length-1 && eastConnectingPipes.indexOf(map[position.row][position.col+1])!=1) {
            return new Coordinates(position.row+1, position.col+1);
        }
        return new Coordinates(-1, -1);
    }

    static Coordinates getNextDirection(Coordinates currentPosition, Coordinates lastPosition) {
        // This method is necessary because we need to not go back the way we came.
        // Returning Coordinates with -1,-1 when a suiting pipe is not found
        if (currentPosition.row<0 || currentPosition.col < 0 || currentPosition.row >= map.length || currentPosition.col >= map[0].length) return new Coordinates(-1,-1);
        char currentPipe = map[currentPosition.row][currentPosition.col];
        // Here we check if the lastPosition even connects to the currentPosition (necessary for part 2).
        if (currentPipe=='|') {
            if (lastPosition.row!=currentPosition.row+1 && currentPosition.row+1<map.length) {
                Coordinates nextPipeCoordinates = new Coordinates(currentPosition.row+1, currentPosition.col);
                char nextPipe = map[nextPipeCoordinates.row][nextPipeCoordinates.col];
                if (northConnectingPipes.indexOf(nextPipe)!=-1 || nextPipe=='S') {
                    return nextPipeCoordinates;
                }
            }
            if (lastPosition.row!=currentPosition.row-1 && currentPosition.row-1 >= 0) {
                Coordinates nextPipeCoordinates = new Coordinates(currentPosition.row - 1, currentPosition.col);
                char nextPipe = map[nextPipeCoordinates.row][nextPipeCoordinates.col];
                if (southConnectingPipes.indexOf(nextPipe) != -1 || nextPipe == 'S') {
                    return nextPipeCoordinates;
                }
            }
        }
        if (currentPipe=='-') {
            if (lastPosition.col!=currentPosition.col+1 && currentPosition.col+1 < map[0].length) {
                Coordinates nextPipeCoordinates = new Coordinates(currentPosition.row, currentPosition.col+1);
                char nextPipe = map[nextPipeCoordinates.row][nextPipeCoordinates.col];
                if (westConnectingPipes.indexOf(nextPipe)!=-1 || nextPipe=='S') {
                    return nextPipeCoordinates;
                }
            }
            if (lastPosition.col!=currentPosition.col-1 && currentPosition.col-1 >= 0) {
                Coordinates nextPipeCoordinates = new Coordinates(currentPosition.row, currentPosition.col-1);
                char nextPipe = map[nextPipeCoordinates.row][nextPipeCoordinates.col];
                if (eastConnectingPipes.indexOf(nextPipe)!=-1 || nextPipe=='S') {
                    return nextPipeCoordinates;
                }
            }
        }
        if (currentPipe=='L') {
            if (lastPosition.col!=currentPosition.col+1 && currentPosition.col+1 < map[0].length) {
                Coordinates nextPipeCoordinates = new Coordinates(currentPosition.row, currentPosition.col+1);
                char nextPipe = map[nextPipeCoordinates.row][nextPipeCoordinates.col];
                if (westConnectingPipes.indexOf(nextPipe)!=-1 || nextPipe=='S') {
                    return nextPipeCoordinates;
                }
            }
            if (lastPosition.row!=currentPosition.row-1 && currentPosition.row-1 >= 0) {
                Coordinates nextPipeCoordinates = new Coordinates(currentPosition.row - 1, currentPosition.col);
                char nextPipe = map[nextPipeCoordinates.row][nextPipeCoordinates.col];
                if (southConnectingPipes.indexOf(nextPipe) != -1 || nextPipe == 'S') {
                    return nextPipeCoordinates;
                }
            }
        }
        if (currentPipe=='J') {
            if (lastPosition.col!=currentPosition.col-1 && currentPosition.col-1 >= 0) {
                Coordinates nextPipeCoordinates = new Coordinates(currentPosition.row, currentPosition.col-1);
                char nextPipe = map[nextPipeCoordinates.row][nextPipeCoordinates.col];
                if (eastConnectingPipes.indexOf(nextPipe)!=-1 || nextPipe=='S') {
                    return nextPipeCoordinates;
                }
            }
            if (lastPosition.row!=currentPosition.row-1 && currentPosition.row-1 >= 0) {
                Coordinates nextPipeCoordinates = new Coordinates(currentPosition.row - 1, currentPosition.col);
                char nextPipe = map[nextPipeCoordinates.row][nextPipeCoordinates.col];
                if (southConnectingPipes.indexOf(nextPipe) != -1 || nextPipe == 'S') {
                    return nextPipeCoordinates;
                }
            }
        }
        if (currentPipe=='7') {
            if (lastPosition.col != currentPosition.col - 1 && currentPosition.col - 1 >= 0) {
                Coordinates nextPipeCoordinates = new Coordinates(currentPosition.row, currentPosition.col - 1);
                char nextPipe = map[nextPipeCoordinates.row][nextPipeCoordinates.col];
                if (eastConnectingPipes.indexOf(nextPipe) != -1 || nextPipe == 'S') {
                    return nextPipeCoordinates;
                }
            }
            if (lastPosition.row != currentPosition.row + 1 && currentPosition.row + 1 < map.length) {
                Coordinates nextPipeCoordinates = new Coordinates(currentPosition.row + 1, currentPosition.col);
                char nextPipe = map[nextPipeCoordinates.row][nextPipeCoordinates.col];
                if (northConnectingPipes.indexOf(nextPipe) != -1 || nextPipe == 'S') {
                    return nextPipeCoordinates;
                }
            }
        }
        if (currentPipe=='F') {
            if (lastPosition.col!=currentPosition.col+1 && currentPosition.col+1 < map[0].length) {
                Coordinates nextPipeCoordinates = new Coordinates(currentPosition.row, currentPosition.col+1);
                char nextPipe = map[nextPipeCoordinates.row][nextPipeCoordinates.col];
                if (westConnectingPipes.indexOf(nextPipe)!=-1 || nextPipe=='S') {
                    return nextPipeCoordinates;
                }
            }
            if (lastPosition.row != currentPosition.row + 1 && currentPosition.row + 1 < map.length) {
                Coordinates nextPipeCoordinates = new Coordinates(currentPosition.row + 1, currentPosition.col);
                char nextPipe = map[nextPipeCoordinates.row][nextPipeCoordinates.col];
                if (northConnectingPipes.indexOf(nextPipe) != -1 || nextPipe == 'S') {
                    return nextPipeCoordinates;
                }
            }
        }
        return new Coordinates(-1, -1);
    }
}