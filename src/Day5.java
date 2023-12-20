import UtilityClasses.Mapping;

import java.util.*;


public class Day5 {
    public static Long solveTask1(List<String> input) {
        //System.out.println(Character.isLetter(' '));
        List<List<String>> maps = processInput(input);
        List<Long> sourceNumbers = new ArrayList<>(Arrays.stream(maps.get(0).get(0).split(" ")).map((e -> Long.parseLong(e))).toList());
        //System.out.println(sourceNumbers);
        for (int i = 1; i < maps.size(); i++) { // Sets of mappings
            List<Long> destinationRangeStartArray = new ArrayList<>();
            List<Long> sourceRangeStartArray = new ArrayList<>();
            List<Long> rangeLengthArray = new ArrayList<>();
            for (int j = 0; j < sourceNumbers.size(); j++) { // Each sourcenumber we are tracking and changing till the end
                List<String> mapping = maps.get(i);
                for (int k = 0; k < maps.get(i).size(); k++) { // Mappings in one set
                    Long destinationRangeStart, sourceRangeStart, rangeLength;
                    String numbers = mapping.get(k);
                    if (k>=destinationRangeStartArray.size()) {
                        List<Long> destinationNumbers = Arrays.stream(numbers.split(" ")).map((e -> Long.parseLong(e))).toList();
                        destinationRangeStartArray.add(destinationNumbers.get(0));
                        sourceRangeStartArray.add(destinationNumbers.get(1));
                        rangeLengthArray.add(destinationNumbers.get(2));
                    }
                    destinationRangeStart = destinationRangeStartArray.get(k);
                    sourceRangeStart = sourceRangeStartArray.get(k);
                    rangeLength = rangeLengthArray.get(k);
                    if (sourceRangeStart <= sourceNumbers.get(j) && sourceNumbers.get(j) < sourceRangeStart + rangeLength) {
                        Long newSourceNumber = destinationRangeStart + (sourceNumbers.get(j) - sourceRangeStart);
                        sourceNumbers.set(j, newSourceNumber);
                        break;
                    }
                }
            }
            //System.out.println(sourceNumbers);
        }
        return Collections.min(sourceNumbers);
    }

    public static long solveTask2(List<String> input) {
        // Idea, go backwards, prioritize the ones mappings that make numbers smaller, then from the next take the ranges that put it in the right range to go smaller in the last one.
        // We sort the mappings themselves, and check them in order, going upwards.
        ArrayList<Mapping> mappings = processMappings(input);
        Collections.sort(mappings);
        ArrayList<ArrayList<Mapping>> mappingsByDepth = new ArrayList<>();
        initialValues = processInitialValues(input.get(0));
        // Creating the mappingsByDepth reference.
        for (int i = 0; i <= 6; i++) {
            mappingsByDepth.add(new ArrayList<>());
        }
        for (Mapping mapping : mappings) {
            mappingsByDepth.get(mapping.depthLevel).add(mapping);
        }
        // Mapping out the paths
        for (Mapping mapping : mappings) {
            getNextMappingRec(mapping, mappingsByDepth);
        }
        // Processing the paths to find the lowest number
        System.out.println(mappings);
        long smallestAnswer = Long.MAX_VALUE;
        for (Mapping mapping : mappings) {
            long answer = recFindLowestValue(mapping, new StringBuilder(), new ArrayList<>());
            if (answer<smallestAnswer) {
                smallestAnswer = answer;
            }
        }
        System.out.println("Collected " + answerCount + " answers, the first one being correct.");
        return smallestAnswer;
    }

    static ArrayList<Mapping> initialValues = new ArrayList<>();
    static int answerCount = 0;
    static long recFindLowestValue(Mapping mapping, StringBuilder pathString, ArrayList<Mapping> path) {
        long lowestAnswer = Long.MAX_VALUE;
        pathString.append("[" + mapping.destinationStartRange+ " " + mapping.sourceStartRange + " "+ mapping.rangeLength +  "] -> ");
        path.add(mapping);
        // Base: We are at the last depth, we check for the initial values now.
        if (mapping.depthLevel==0 || mapping.lowerMappings.size()==0) {
            //System.out.println(pathString);
            for (Mapping initialValue: initialValues) {
                if (checkIfRangesOverlap(mapping, initialValue)) {
                    long startingValue = initialValue.destinationStartRange;
                    answerCount++;
                    lowestAnswer= traversePath(startingValue, initialValue.rangeLength, path);
                }
            }
            return lowestAnswer;
        }
        // For all the inner mappings.
        for (Mapping suitableMapping : mapping.lowerMappings) {
            long answer = recFindLowestValue(suitableMapping, new StringBuilder(pathString), new ArrayList<>(path));
            if (answer<lowestAnswer) {
                lowestAnswer = answer;
            }
        }
        return lowestAnswer;
    }
    static long traversePath(Long startingValue, Long startingValueRange, ArrayList<Mapping> path) {
        Collections.reverse(path);
        for (int i = 0; i < path.size(); i++) {
            Mapping mapping = path.get(i);
            if (!(mapping.sourceStartRange<=startingValue) || !(startingValue < mapping.sourceStartRange + mapping.rangeLength)) {
                if (startingValue < mapping.sourceStartRange && mapping.sourceStartRange<startingValue+startingValueRange) {
                    // So here we skip a bunch of numbers till we know we will reach the next number.
                    startingValueRange-=mapping.sourceStartRange-startingValue;
                    startingValue=mapping.sourceStartRange;
                    i--;
                    continue;
                }
                else {
                    Collections.reverse(path);
                    return Long.MAX_VALUE;
                }
            }
            startingValue = mapping.destinationStartRange + (startingValue - mapping.sourceStartRange);
            if (startingValueRange> mapping.rangeLength) { // ALL ALONG this was the last thing that was missing.
                startingValueRange = mapping.rangeLength;
            }
            for (Mapping higherMapping : mapping.higherMappings) {
                if (higherMapping.sourceStartRange<=startingValue && startingValue < higherMapping.sourceStartRange + higherMapping.rangeLength && ((!path.contains(higherMapping) && i+1<path.size() && higherMapping.depthLevel<path.get(i+1).depthLevel) || i==path.size()-1)) { // Why we check only till next depth leve, is because the value is going to chance, and is irrelevant to the ones past the next one.
                    Collections.reverse(path);
                    return Long.MAX_VALUE;
                }
            }
        }
        System.out.println("Answer: " + startingValue + " counter: " + answerCount);
        Collections.reverse(path);
        return startingValue;
    }

    static boolean checkIfRangesOverlap(Mapping current, Mapping previous) {
        // Subtract 1 since our ranges look like this [x, y)
        long end1 = current.sourceStartRange+ current.rangeLength-1;
        long end2 = previous.destinationStartRange + previous.rangeLength-1;
        return end1 >= previous.destinationStartRange && current.sourceStartRange <= end2;
    }


    static void getNextMappingRec(Mapping mapping, ArrayList<ArrayList<Mapping>> mappingsByDepth) {
        // Base case
        if (mapping.depthLevel==0 || !mapping.lowerMappings.isEmpty()) {
            return;
        }
        //System.out.println("Checking: " + mapping);
        for (int i = mapping.depthLevel-1; i >= 0; i--) {
            for (Mapping candidateMapping : mappingsByDepth.get(i)) {
                if (checkIfRangesOverlap(mapping, candidateMapping)) {
                    mapping.lowerMappings.add(candidateMapping);
                    candidateMapping.higherMappings.add(mapping);
                    //System.out.println("Adding: " + candidateMapping + " to: " + mapping);
                    getNextMappingRec(candidateMapping, mappingsByDepth);
                }
            }
        }
    }

    static ArrayList<Mapping> processInitialValues(String string) {
        ArrayList<Mapping> initialValues = new ArrayList<>();
        String[] temp = string.split(":")[1].trim().split(" ");
        for (int i = 0; i < temp.length; i+=2) {
            Mapping mapping = new Mapping(Long.parseLong(temp[i]), -1, Long.parseLong(temp[i+1]), -1);
            initialValues.add(mapping);
        }
        Collections.sort(initialValues);
        return initialValues;
    }
    static ArrayList<Mapping> processMappings(List<String> input) {
        ArrayList<Mapping> mappings = new ArrayList<>();
        //System.out.println(numbers);
        int depth=-1;
        for (String line : input) {
            // New map
            //System.out.println(line);
            if (line.length()==0) {
                depth++;
            }
            else if (Character.isDigit(line.charAt(0))) {
                List<Long> destinationNumbers = Arrays.stream(line.split(" ")).map((e -> Long.parseLong(e))).toList();
                long destinationStartRange = destinationNumbers.get(0);
                long sourceStartRange = destinationNumbers.get(1);
                long rangeLength = destinationNumbers.get(2);
                mappings.add(new Mapping(destinationStartRange, sourceStartRange, rangeLength, depth));
            }
        }
        return mappings;
    }


    static List<List<String>> processInput(List<String> input) {
        List<List<String>> maps = new ArrayList<>();
        List<String> numbers = new ArrayList<>();
        numbers.add(input.get(0).split(": ")[1]);
        //System.out.println(numbers);
        for (String line : input) {
            // New map
            //System.out.println(line);
            if (line.length()==0) {
                maps.add(numbers);
                numbers = new ArrayList<>();
            }
            else if (Character.isDigit(line.charAt(0))) {
                numbers.add(line);
            }
        }
        maps.add(numbers);

        return maps;
    }
}
