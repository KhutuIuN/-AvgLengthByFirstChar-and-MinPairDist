package edu.eskisehir.BIM207;

import java.io.*;
import java.util.*;

public class Main {
    private final String fileName;
    private final int topN;
    public String[] tokens;
    Map<String, Double> map = new HashMap<>();

    public Main(String fileName, int topN) throws IOException {
        //Complete this constructor
        this.fileName = fileName;
        this.topN = topN;

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


        // A map to hold pairs with their factors.

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

        /** This part exist for adding pairs in the set according to the topN number. **/
        ArrayList<Double> sortedMap = new ArrayList<>(map.values());
        Collections.sort(sortedMap);
        Collections.reverse(sortedMap);

        int counter = 0;
        while (counter < getTopN())
            for (Map.Entry<String, Double> iterator : map.entrySet()) {
                if (iterator.getValue().equals(sortedMap.get(counter))) {
                    pairs.add(iterator.getKey());
                    counter++;
                }
            }

        return pairs;
    }

    /** Sets are unordered so to print the set with decreasing order, use printSet method. **/
    public void printSet(Set set) {

        ArrayList<String> arListOfSet = new ArrayList<>(set); //Copy of the Set as ArrayList.

        ArrayList<String> indexes = new ArrayList<>(); //To keep indexes to know which pair to print.


        for (int i = 0; i < getTopN(); i++) {
            String temp = (arListOfSet.get(i));
            String[] split = temp.split("="); //
            indexes.add(split[3]);
        }

        Collections.sort(indexes);
        Collections.reverse(indexes);

        for (int i = 0; i < getTopN(); i++) {
            for (int j = 0; j < getTopN(); j++) {
                String temp = arListOfSet.get(j);
                if (temp.contains(indexes.get(i))) {
                    System.out.println(arListOfSet.get(j));
                    arListOfSet.remove(arListOfSet.get(j));
                    arListOfSet.add(j, "-1");
                }
            }
        }
    }


    public double calculateTotalDistance(String token1, String token2) {
        double numOfToken1 = 0;
        double numOfToken2 = 0;

        for (String token : tokens) {
            {
                if (token.equals(token1))
                    numOfToken1++;
                else if (token.equals(token2))
                    numOfToken2++;
            }
        }

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