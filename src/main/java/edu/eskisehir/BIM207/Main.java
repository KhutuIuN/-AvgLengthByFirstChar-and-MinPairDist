package edu.eskisehir.BIM207;

import java.io.*;
import java.util.*;

public class Main {
    private final String fileName;
    private final int topN;
    private String[] tokens;
    private Map<String, Double> map = new HashMap<>(); // A map to hold pairs with their factors.

    public Main(String fileName, int topN) throws IOException {
        this.fileName = fileName;
        this.topN = topN;

        /** Tokenize operation **/
        File file = new File(getFileName());
        BufferedReader br = new BufferedReader(new FileReader(file));

        String s = br.readLine().replace("  ", " ");
        StringBuilder toplam = new StringBuilder(s);
        while ((s = br.readLine()) != null)
            toplam.append(" ").append(s);

        String[] tokens = toplam.toString().split(" ");

        for (int i = 0; i < tokens.length; i++) {
            tokens[i] = tokens[i].trim();
        }

        for (int i = 0; i < tokens.length; i++) {
            tokens[i] = tokens[i].replaceAll("[,.':-]", "");
            tokens[i] = tokens[i].toLowerCase();
        }

        this.tokens = tokens;

        computeAvgLengthByFirstChar();
        Set pairs = calculateMinPairDist();
        printSet(pairs);
    }

    public String getFileName() {
        return fileName;
    }

    public int getTopN() {
        return topN;
    }

    private void computeAvgLengthByFirstChar() {
        //Fill this function
        ArrayList<Character> chars = new ArrayList<>();
        for (String token : tokens) {
            if (!chars.contains(token.charAt(0)))
                chars.add(token.charAt(0));
        }
        Collections.sort(chars);

        int temp = 0;
        double numofif = 0;

        for (Character aChar : chars) {

            for (String token : tokens) {

                if (token.charAt(0) == aChar) {
                    temp += token.length();
                    numofif++;
                }

            }

            System.out.println(aChar + "   " + temp / numofif);
            temp = 0;
            numofif = 0;
        }

    }

    private Set calculateMinPairDist() {
        //Fill this function


        Set pairs = new HashSet();
        double distance;

        for (int i = 1; i < tokens.length; i++) {
            for (int j = i + 1; j < tokens.length; j++) {
                if (tokens[i].equals(tokens[j])) {
                    continue;
                }
                distance = calculateTotalDistance(tokens[i], tokens[j]);
                String pair = "Pair{t1='" + tokens[i] + "', t2='" + tokens[j] + "', factor= " + distance + "}";
                map.put(pair, distance);
            }
        }

        /** This part exists for adding pairs in the set according to the topN number.(topN biggest pair) **/
        ArrayList<Double> sortedMap = new ArrayList<>(map.values());
        Collections.sort(sortedMap);
        Collections.reverse(sortedMap);

        int counter = 0;
        while (counter < getTopN())
            for (Map.Entry<String, Double> iterator : map.entrySet()) {
                if (iterator.getValue().equals(sortedMap.get(counter))) {
                    pairs.add(iterator.getKey());
                    if (pairs.contains(iterator.getKey()))
                        counter++;
                }
            }

        return pairs;
    }

    /**
     * Sets are unordered so to print the set with decreasing order, use printSet method.
     **/
    public void printSet(Set set) {

        ArrayList<String> arListOfSet = new ArrayList<>(set); //Copy of the Set as ArrayList.

        ArrayList<Double> values = new ArrayList<>(map.values()); //To keep values to know which pair to print.

        Collections.sort(values);
        Collections.reverse(values);

        int counter = 0; // to keep track of how many pair is printed
        for (int i = 0; i < arListOfSet.size(); i++) {
            for (String s : arListOfSet) {
                if (s.contains(values.get(i).toString())) {
                    System.out.println(s);
                    counter++;
                }
                if (counter == getTopN())
                    break;
            }
        }
    }


    public double calculateTotalDistance(String token1, String token2) {
        double numOfToken1 = 0;
        double numOfToken2 = 0;

        /** To find how many time token1 and token2 are occured in the text **/
        for (String token : tokens) {
            {
                if (token.equals(token1))
                    numOfToken1++;
                else if (token.equals(token2))
                    numOfToken2++;
            }
        }

        /** To fin the distance between token1 and token2 **/
        double sum = 0;
        for (int i = 1; i < tokens.length; i++) {
            if (!tokens[i].equals(token1))
                continue;

            for (int j = i; j < tokens.length; j++) {
                if (!tokens[j].equals(token2))
                    continue;

                sum += j - i;
                break;
            }
        }

        return (numOfToken1 * numOfToken2) / (1 + Math.log(sum));
    }

    public static void main(String[] args) throws IOException {
        new Main(args[0], Integer.parseInt(args[1]));


    }


}