import java.util.*;

public class Day7 {
    static List<Character> cards = List.of('-','2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'A');
    static List<Character> cardsWithJoker = List.of('-','J', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'Q', 'K', 'A');
    enum HandType { HIGH_CARD , ONE_PAIR, TWO_PAIR, THREE_OF_A_KIND, FULL_HOUSE, FOUR_OF_A_KIND, FIVE_OF_A_KIND }
    static List<HandType> handTypes = List.of(HandType.HIGH_CARD, HandType.ONE_PAIR, HandType.TWO_PAIR, HandType.THREE_OF_A_KIND, HandType.FULL_HOUSE, HandType.FOUR_OF_A_KIND, HandType.FIVE_OF_A_KIND);

    static long solveTask1(List<String> input) {
        HashMap<String, Integer> handBets = new HashMap<>();
        HashMap<String, Long> handScores = new HashMap<>();
        for (String s : input) {
            String hand = s.split(" ")[0];
            int bet = Integer.parseInt(s.split(" ")[1]);
            handBets.put(hand, bet);
            handScores.put(hand, getHandValue(hand));
        }
        List<Map.Entry<String, Long>> handsOrderedByScore = new ArrayList<>(handScores.entrySet());
        // Sort the list based on values using a custom comparator
        handsOrderedByScore.sort(Map.Entry.comparingByValue());
        long scoreSum = 0;
        int i = 1;
        for (Map.Entry<String, Long> entry : handsOrderedByScore) {
            System.out.println(entry.getKey() + " " + entry.getValue() + " " + handBets.get(entry.getKey())*i);
            scoreSum+=handBets.get(entry.getKey())*i;
            //System.out.println(scoreSum);
            i++;
        }
        return scoreSum;
    }

    static long solveTask2(List<String> input) {
        HashMap<String, Integer> handBets = new HashMap<>();
        HashMap<String, Long> handScores = new HashMap<>();
        for (String s : input) {
            String hand = s.split(" ")[0];
            int bet = Integer.parseInt(s.split(" ")[1]);
            handBets.put(hand, bet);
            handScores.put(hand, getHandValueWithJoker(hand));
        }
        List<Map.Entry<String, Long>> handsOrderedByScore = new ArrayList<>(handScores.entrySet());

        // Sort the list based on values using a custom comparator
        handsOrderedByScore.sort(Map.Entry.comparingByValue());
        long scoreSum = 0;
        int i = 1;
        for (Map.Entry<String, Long> entry : handsOrderedByScore) {
            System.out.println(entry.getKey() + " " + entry.getValue() + " " + handBets.get(entry.getKey())*i);
            scoreSum+=handBets.get(entry.getKey())*i;
            i++;
        }
        return scoreSum;
    }

    static long getHandValue(String hand) {
        Map<Character, Integer> handMap = new HashMap<>();
        // Count cards
        long offset = -10000000000L;
        for (char card : hand.toCharArray()) {
            if (handMap.containsKey(card)) {
                handMap.put(card, handMap.get(card) + 1);
            }
            else {
                handMap.put(card, 1);
            }
        }
        // Five of a kind
        if (handMap.size()==1) {
            return (long) Math.pow(10,12) + getIndividualValues(hand) + offset;
        }
        if (handMap.size()==2) {
            // Four of a kind
            if (handMap.containsValue(1) && handMap.containsValue(4)) {
                return (long) Math.pow(10,11) + getIndividualValues(hand) + offset;
            }
            // Full house
            return (long) (Math.pow(10,10) + getIndividualValues(hand)) + offset;
        }
        if (handMap.size()==3) {
            // Three of a kind
            if (handMap.containsValue(3)) {
                return (long) Math.pow(10,9) + getIndividualValues(hand) + offset;
            }
            // Two pair
            if (Collections.frequency(handMap.values(), 2)==2) {
                return (long) (Math.pow(10, 8) + getIndividualValues(hand)) + offset;
            }
        }
        // One pair
        if (handMap.size()==4) {
            return (long) Math.pow(10,7) + getIndividualValues(hand) + offset;
        }
        // High card
        return getIndividualValues(hand) + offset;
    }

    static long getHandValueWithJoker(String hand) {
        Map<Character, Integer> handMap = new HashMap<>();
        HandType handType = HandType.HIGH_CARD;
        int jokerCount = 0;
        long offset = -10000000000L;
        // Count cards
        for (char card : hand.toCharArray()) {
            if (card=='J') {
                jokerCount++;
            }
            else if (handMap.containsKey(card)) {
                handMap.put(card, handMap.get(card) + 1);
            }
            else {
                handMap.put(card, 1);
            }
        }
        if (handMap.containsValue(5)) {
            handType = HandType.FIVE_OF_A_KIND;
        }
        else if (handMap.containsValue(4)) {
            handType = HandType.FOUR_OF_A_KIND;
        }
        else if (handMap.containsValue(3) && handMap.containsValue(2)) {
            handType = HandType.FULL_HOUSE;
        }
        else if (handMap.containsValue(3)) {
                handType = HandType.THREE_OF_A_KIND;
        }
        else if (Collections.frequency(handMap.values(), 2)==2) {
            handType = HandType.TWO_PAIR;
        }
        else if (handMap.containsValue(2)) {
            handType = HandType.ONE_PAIR;
        }
        // Process jokers
        //System.out.println(hand);
        if (jokerCount>0) {
            HandType upgradedHandType = handType;
            for (int i = 0; i < Math.min(jokerCount, 4); i++) {
                if (upgradedHandType==HandType.TWO_PAIR) {
                    upgradedHandType=HandType.FULL_HOUSE;
                }
                else if (upgradedHandType==HandType.THREE_OF_A_KIND) {
                    upgradedHandType=HandType.FOUR_OF_A_KIND;
                }
                else if (upgradedHandType==HandType.ONE_PAIR) {
                    upgradedHandType=HandType.THREE_OF_A_KIND;
                }
                else {
                    upgradedHandType=handTypes.get(handTypes.indexOf(upgradedHandType)+1);
                }
            }
            if (upgradedHandType==HandType.HIGH_CARD) {
                return getIndividualValuesWithJoker(hand) + offset;
            }
            System.out.println("Upgraded " + hand + " from " + handType + " to " + upgradedHandType);
            handType = upgradedHandType;
        }
        return (long)(Math.pow(10, 6 + handTypes.indexOf(handType)) + getIndividualValuesWithJoker(hand) + offset);
    }
    
    static int getIndividualValues(String hand) {
        hand = new StringBuilder(hand).reverse().toString();
        int valueSum = 0;
        for (int i = 0; i<hand.length() ; i++) {
            valueSum+= cards.indexOf(hand.charAt(i))*Math.pow(14, i+1);
        }
        return valueSum;
    }

    static int getIndividualValuesWithJoker(String hand) {
        hand = new StringBuilder(hand).reverse().toString();
        int valueSum = 0;
        for (int i = 0; i<hand.length() ; i++) {
            valueSum+= cardsWithJoker.indexOf(hand.charAt(i))*Math.pow(14, i+1);
        }
        return valueSum;
    }
}