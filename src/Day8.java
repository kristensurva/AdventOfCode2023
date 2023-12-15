import UtilityClasses.MapNode;

import java.util.*;
import java.util.stream.Collectors;

public class Day8 {
    static public int solveTask1(List<String> input) {
        String instructions = input.get(0);
        MapNode currentNode = constructNetwork(input.subList(2, input.size())).get("AAA");
        int instructionIndex = 0;
        int count = 0;
        while (!Objects.equals(currentNode.getName(), "ZZZ")) {
            currentNode = instructions.charAt(instructionIndex)=='L' ? currentNode.getLeft() : currentNode.getRight();
            count++;
            instructionIndex++;
            if (instructionIndex>=instructions.length()) {
                instructionIndex=0;
            }
        }
        return count;
    }



    static public long solveTask2(List<String> input) {
        String instructions = input.get(0);
        Map<String, MapNode> nodeMap = constructNetwork(input.subList(2, input.size()));
        // Get starting nodes
        List<MapNode> currentNodes = new ArrayList<>();
        for (String node : nodeMap.keySet()) {
            if (node.charAt(2)=='A') currentNodes.add(nodeMap.get(node));
        }
        // Pathing
        long stepCount = 0;
        int instructionIndex = 0;
        List<StringBuilder> path = new ArrayList<>();
        for (int i = 0, currentNodesSize = currentNodes.size(); i < currentNodesSize; i++) {
            path.add(new StringBuilder());
            MapNode currentNode = currentNodes.get(i);
            // Printing path
            path.set(i, new StringBuilder().append(currentNode.getName()));
        }
        List<Integer> currentlySkippedPaths = new ArrayList<>();
        List<MapNode> currentPatchStartingNodes = new ArrayList<>(currentNodes);
        HashMap<Integer, Long> stepCountToEndForEachPath = new HashMap<>();
        while (true) {
            // If all nodes are skippable
            if (instructionIndex==0 && currentlySkippedPaths.size()==currentNodes.size()) {
                stepCount+=instructions.length();
                instructionIndex+=instructions.length();
            }
            else {
                for (int i = 0; i < currentNodes.size(); i++) {
                    if (!currentlySkippedPaths.contains(i)) { // Ignore skipped nodes
                        MapNode nextNode = instructions.charAt(instructionIndex)=='L' ? currentNodes.get(i).getLeft() : currentNodes.get(i).getRight();
                        currentNodes.set(i, nextNode);
                        path.get(i).append(nextNode.getName());
                    }
                }
                stepCount++;
                instructionIndex++;
            }
            // At the end of instructions, reset
            if (instructionIndex>=instructions.length()) {
                currentlySkippedPaths.clear();
                for (int i = 0; i < currentPatchStartingNodes.size(); i++) { // Update end skip nodes
                    currentPatchStartingNodes.get(i).setPathEndNode(currentNodes.get(i));
                }
                currentPatchStartingNodes = new ArrayList<>(currentNodes); // hope this works might be off somehow
                // Check which node instructions skippable
                for (int i = 0; i < currentNodes.size(); i++) {
                    if (currentNodes.get(i).getPathEndNode()!=null) {
                        currentlySkippedPaths.add(i);
                    }
                }
                instructionIndex=0;
            }
            if (stepCount % instructions.length()==0) {
                // Cache every successful step count seperately, then take the least common multiple
                for (int i = 0; i < currentNodes.size(); i++) {
                    if (currentNodes.get(i).getName().charAt(2)=='Z') {
                        stepCountToEndForEachPath.put(i, stepCount);
                    }
                    if (stepCountToEndForEachPath.size()==currentNodes.size()) {
                        return leastCommonMultiple(new ArrayList<>(stepCountToEndForEachPath.values()));
                    }
                }
            }
        }
    }
    static private Map<String, MapNode> constructNetwork(List<String> input) {
        Map<String, MapNode> network = new HashMap<>();
        //1st pass inputing all nodes in the Map
        for (String line : input) {
            String name = line.substring(0, 3);
            network.put(name, new MapNode(name));
        }

        for (String line : input) {
            String name = line.substring(0, 3);
            String left = line.substring(7,10);
            String right = line.substring(12,15);
            network.get(name).setLeft(network.get(left));
            network.get(name).setRight(network.get(right));
        }
        return network;
    }
    static long leastCommonMultiple(List<Long> numbers) {
        long maxNum = Collections.max(numbers);
        long i = 2;
        while (true) {
            long x = maxNum*i;
            if (numbers.stream().allMatch(num -> x % num == 0 )) {
                return x;
            }
            i++;
        }
    }
}