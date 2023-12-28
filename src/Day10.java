import UtilityClasses.Point;

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
        Point previousPosition = getStartPosition();
        Point currentPosition = getStartingDirection(previousPosition);
        markedMap[currentPosition.row][currentPosition.col] = '*';
        int steps = 1;
        while (map[currentPosition.row][currentPosition.col]!='S') {
            //printMapWithPosition(currentPosition, markedMap);
            Point temp = currentPosition;
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
        ArrayList<ArrayList<Point>> allConnectedPipeLoops = new ArrayList<>();
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[row].length; col++) {
                char c = map[row][col];
                if (c != '.' && c!=',' && markedMap[row][col]!='#') {
                    ArrayList<Point> pipeLoop = getEnclosedPipeLoop(new Point(row, col));
                    if (pipeLoop.size()>0) {
                        allConnectedPipeLoops.add(pipeLoop);
                        for (Point point : pipeLoop) {
                            markedMap[point.row][point.col] ='#';
                        }
                    }
                }
            }
        }
        ArrayList<Point> mainPipeLoop = null;
        // Convert all the 4 length pipes to ground too
        for (ArrayList<Point> connectedPipeLoop : allConnectedPipeLoops) {
            if (connectedPipeLoop.size()==4){
                for (Point point : connectedPipeLoop) {
                    map[point.row][point.col]='.';
                }
            }
            else {
                mainPipeLoop = connectedPipeLoop;
            }
        }
        printMap(processInputPretty(input));
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
        printMap(map);
        return enclosedTiles;
    }

    static void replaceStartOnMap() {
        String possiblePipes = "|-LJ7F";
        Point startPosition = getStartPosition();
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
        ArrayList<Point> startingOuterPosition = new ArrayList<>();
        //Count all O's and remember position
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[0].length; col++) {
                if (map[row][col]=='O') {
                    startingOuterPosition.add(new Point(row, col));
                }
            }
        }
        for (Point position : startingOuterPosition) {
            recursiveFillOutAllOuterSpots(position);
        }
    }

    static void recursiveFillOutAllOuterSpots(Point position) {
        // Base case: if no direction has a fillable spot, exit branch
        Point up = new Point(position.row -1, position.col);
        Point down = new Point(position.row + 1, position.col);
        Point left = new Point(position.row, position.col - 1);
        Point right = new Point(position.row , position.col + 1);
        if (up.row >= 0 && map[up.row][up.col]=='.') {
            map[up.row][up.col] = 'O';
            recursiveFillOutAllOuterSpots(new Point(up.row, up.col));
        }
        if (down.row < map.length && map[down.row][down.col]=='.') {
            map[down.row][down.col] = 'O';
            recursiveFillOutAllOuterSpots(new Point(down.row, down.col));
        }
        if (left.col >= 0 && map[left.row][left.col]=='.') {
            map[left.row][left.col] = 'O';
            recursiveFillOutAllOuterSpots(new Point(left.row, left.col));
        }
        if (right.col < map[0].length && map[right.row][right.col]=='.') {
            map[right.row][right.col] = 'O';
            recursiveFillOutAllOuterSpots(new Point(right.row, right.col));
        }
    }

    static ArrayList<Point> getEnclosedPipeLoop(Point startingPosition) {
        // Not only does it return a pipe loop if its a loop, it will also clean all non looping pipes, by changing the map!
        ArrayList<Point> connectedPipes = new ArrayList<>();
        connectedPipes.add(startingPosition);
        Point currentPosition = getNextDirection(startingPosition, startingPosition);
        int steps = 1;
        while (!currentPosition.equals(startingPosition)) {
            //printMapWithPosition(currentPosition);
            // If pipe isn't looping, replace all the pipes with ground and return
            if (currentPosition.row==-1) {
                for (Point connectedPipe : connectedPipes) {
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

    static void travelAlongPipeAndMarkOutwardSpots(ArrayList<Point> pipeLoop) {
        // Hardcoding the direction, we should always start with lefthand being towards the outside
        Point toTheLeftEast = new Point(0, -1); // Means we're facing North
        Point toTheLeftNorth = new Point(-1, 0);// Means we're facing West
        Point toTheLeftWest = new Point(0, 1); // Means we're facing South
        Point toTheLeftSouth = new Point(1, 0); // Means we're facing East
        // If we return right, we increment index by 1, if left, subtract.
        ArrayList<Point> rotations = new ArrayList<>();
        rotations.add(toTheLeftEast);
        rotations.add(toTheLeftNorth);
        rotations.add(toTheLeftWest);
        rotations.add(toTheLeftSouth);
        int rotationIndex = 1;
        for (int i = 1; i < pipeLoop.size()-1; i++) {
            Point currentPipe = pipeLoop.get(i);
            Point currentPipeAdjacent = Point.sum(currentPipe, rotations.get(rotationIndex));
            Point previousPipe = pipeLoop.get(i-1);
            Point previousPipeAdjacent = Point.sum(previousPipe, rotations.get(rotationIndex));
            if (currentPipeAdjacent.row >= 0 && currentPipeAdjacent.row < map.length && currentPipeAdjacent.col >= 0 && currentPipeAdjacent.col < map[0].length && map[currentPipeAdjacent.row][currentPipeAdjacent.col]=='.') {
                map[currentPipeAdjacent.row][currentPipeAdjacent.col]='O';
            }
            if (previousPipeAdjacent.row >= 0 && previousPipeAdjacent.row < map.length && previousPipeAdjacent.col >= 0 && previousPipeAdjacent.col < map[0].length && map[previousPipeAdjacent.row][previousPipeAdjacent.col]=='.') {
                map[previousPipeAdjacent.row][previousPipeAdjacent.col]='O';
            }
            // If there's a turn
            if (map[currentPipe.row][currentPipe.col]!='|' && map[currentPipe.row][currentPipe.col]!='-') {
                Point nextPipe = pipeLoop.get(i+1);
                // Bigger than 0 turn right, smaller than 0 turn left
                rotationIndex = ((rotationIndex + crossProductOf3Points(previousPipe, currentPipe, nextPipe))+rotations.size()) % rotations.size();
                currentPipeAdjacent = Point.sum(currentPipe, rotations.get(rotationIndex));
                if (currentPipeAdjacent.row >= 0 && currentPipeAdjacent.row < map.length && currentPipeAdjacent.col >= 0 && currentPipeAdjacent.col < map[0].length && map[currentPipeAdjacent.row][currentPipeAdjacent.col]=='.') {
                    map[currentPipeAdjacent.row][currentPipeAdjacent.col]='O';
                }
            }
        }
    }

    static int crossProductOf3Points(Point A, Point B, Point C) {
        Point AB = new Point(A.row-B.row, B.col-A.col);
        Point BC = new Point(B.row-C.row, C.col-B.col);
        return AB.row*BC.col-AB.col*BC.row;
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

    static Point getStartPosition() {
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map.length; col++) {
                if (map[row][col]=='S') {
                    return new Point(row, col);
                }
            }
        }
        return new Point(-1, -1);
    }

    static Point getStartingDirection(Point position) {
        // This method is greedy and will return the first suitable direction it finds.
        // Checking north
        if (position.row > 0 && northConnectingPipes.indexOf(map[position.row-1][position.col])!=-1) {
            return new Point(position.row-1, position.col);
        }
        // Checking south
        if (position.row < map.length-1 && southConnectingPipes.indexOf(map[position.row+1][position.col])!=1) {
            return new Point(position.row+1, position.col);
        }
        // Checking west
        if (position.col > 0 && westConnectingPipes.indexOf(map[position.row][position.col-1])!=1) {
            return new Point(position.row, position.col-1);
        }
        // Checking east
        if (position.col < map.length-1 && eastConnectingPipes.indexOf(map[position.row][position.col+1])!=1) {
            return new Point(position.row+1, position.col+1);
        }
        return new Point(-1, -1);
    }

    static Point getNextDirection(Point currentPosition, Point lastPosition) {
        // This method is necessary because we need to not go back the way we came.
        // Returning Coordinates with -1,-1 when a suiting pipe is not found
        if (currentPosition.row<0 || currentPosition.col < 0 || currentPosition.row >= map.length || currentPosition.col >= map[0].length) return new Point(-1,-1);
        char currentPipe = map[currentPosition.row][currentPosition.col];
        // Here we check if the lastPosition even connects to the currentPosition (necessary for part 2).
        if (currentPipe=='|') {
            if (lastPosition.row!=currentPosition.row+1 && currentPosition.row+1<map.length) {
                Point nextPipePoint = new Point(currentPosition.row+1, currentPosition.col);
                char nextPipe = map[nextPipePoint.row][nextPipePoint.col];
                if (northConnectingPipes.indexOf(nextPipe)!=-1 || nextPipe=='S') {
                    return nextPipePoint;
                }
            }
            if (lastPosition.row!=currentPosition.row-1 && currentPosition.row-1 >= 0) {
                Point nextPipePoint = new Point(currentPosition.row - 1, currentPosition.col);
                char nextPipe = map[nextPipePoint.row][nextPipePoint.col];
                if (southConnectingPipes.indexOf(nextPipe) != -1 || nextPipe == 'S') {
                    return nextPipePoint;
                }
            }
        }
        if (currentPipe=='-') {
            if (lastPosition.col!=currentPosition.col+1 && currentPosition.col+1 < map[0].length) {
                Point nextPipePoint = new Point(currentPosition.row, currentPosition.col+1);
                char nextPipe = map[nextPipePoint.row][nextPipePoint.col];
                if (westConnectingPipes.indexOf(nextPipe)!=-1 || nextPipe=='S') {
                    return nextPipePoint;
                }
            }
            if (lastPosition.col!=currentPosition.col-1 && currentPosition.col-1 >= 0) {
                Point nextPipePoint = new Point(currentPosition.row, currentPosition.col-1);
                char nextPipe = map[nextPipePoint.row][nextPipePoint.col];
                if (eastConnectingPipes.indexOf(nextPipe)!=-1 || nextPipe=='S') {
                    return nextPipePoint;
                }
            }
        }
        if (currentPipe=='L') {
            if (lastPosition.col!=currentPosition.col+1 && currentPosition.col+1 < map[0].length) {
                Point nextPipePoint = new Point(currentPosition.row, currentPosition.col+1);
                char nextPipe = map[nextPipePoint.row][nextPipePoint.col];
                if (westConnectingPipes.indexOf(nextPipe)!=-1 || nextPipe=='S') {
                    return nextPipePoint;
                }
            }
            if (lastPosition.row!=currentPosition.row-1 && currentPosition.row-1 >= 0) {
                Point nextPipePoint = new Point(currentPosition.row - 1, currentPosition.col);
                char nextPipe = map[nextPipePoint.row][nextPipePoint.col];
                if (southConnectingPipes.indexOf(nextPipe) != -1 || nextPipe == 'S') {
                    return nextPipePoint;
                }
            }
        }
        if (currentPipe=='J') {
            if (lastPosition.col!=currentPosition.col-1 && currentPosition.col-1 >= 0) {
                Point nextPipePoint = new Point(currentPosition.row, currentPosition.col-1);
                char nextPipe = map[nextPipePoint.row][nextPipePoint.col];
                if (eastConnectingPipes.indexOf(nextPipe)!=-1 || nextPipe=='S') {
                    return nextPipePoint;
                }
            }
            if (lastPosition.row!=currentPosition.row-1 && currentPosition.row-1 >= 0) {
                Point nextPipePoint = new Point(currentPosition.row - 1, currentPosition.col);
                char nextPipe = map[nextPipePoint.row][nextPipePoint.col];
                if (southConnectingPipes.indexOf(nextPipe) != -1 || nextPipe == 'S') {
                    return nextPipePoint;
                }
            }
        }
        if (currentPipe=='7') {
            if (lastPosition.col != currentPosition.col - 1 && currentPosition.col - 1 >= 0) {
                Point nextPipePoint = new Point(currentPosition.row, currentPosition.col - 1);
                char nextPipe = map[nextPipePoint.row][nextPipePoint.col];
                if (eastConnectingPipes.indexOf(nextPipe) != -1 || nextPipe == 'S') {
                    return nextPipePoint;
                }
            }
            if (lastPosition.row != currentPosition.row + 1 && currentPosition.row + 1 < map.length) {
                Point nextPipePoint = new Point(currentPosition.row + 1, currentPosition.col);
                char nextPipe = map[nextPipePoint.row][nextPipePoint.col];
                if (northConnectingPipes.indexOf(nextPipe) != -1 || nextPipe == 'S') {
                    return nextPipePoint;
                }
            }
        }
        if (currentPipe=='F') {
            if (lastPosition.col!=currentPosition.col+1 && currentPosition.col+1 < map[0].length) {
                Point nextPipePoint = new Point(currentPosition.row, currentPosition.col+1);
                char nextPipe = map[nextPipePoint.row][nextPipePoint.col];
                if (westConnectingPipes.indexOf(nextPipe)!=-1 || nextPipe=='S') {
                    return nextPipePoint;
                }
            }
            if (lastPosition.row != currentPosition.row + 1 && currentPosition.row + 1 < map.length) {
                Point nextPipePoint = new Point(currentPosition.row + 1, currentPosition.col);
                char nextPipe = map[nextPipePoint.row][nextPipePoint.col];
                if (northConnectingPipes.indexOf(nextPipe) != -1 || nextPipe == 'S') {
                    return nextPipePoint;
                }
            }
        }
        return new Point(-1, -1);
    }

    static void printMap(char[][] map) {
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[row].length; col++) {
                System.out.print(map[row][col]);
            }
            System.out.println();
        }
        System.out.println();
    }
    static void printMapWithPosition(Point position, char[][] map) {
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
}