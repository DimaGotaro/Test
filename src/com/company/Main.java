package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        String inputFile = "C:\\Users\\dima\\IdeaProjects\\Test\\src\\com\\company\\input.txt";
        String outputFile = "C:\\Users\\dima\\IdeaProjects\\Test\\src\\com\\company\\output.txt";

        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFile))) {

            List<String> allArray = bufferedReader.lines().toList();
            long i = Long.parseLong(allArray.get(0));

            List<String> firstArray = new ArrayList<>(allArray.stream()
                    .skip(1L)
                    .limit(i)
                    .toList());

            List<String> secondArray = allArray.stream()
                    .skip(2L + i)
                    .toList();

            for (String s : comparison(firstArray, secondArray)) {
                bufferedWriter.write(s);
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> comparison(List<String> firstArray, List<? extends String> secondArray) {

        List<List<Integer>> listSecond = new ArrayList<>(secondArray.size());

        for (String lineSecondArray : secondArray) {
            String lineSecondArrayLower = replaceInLineAndLower(lineSecondArray);
            List<Integer> listFirst = new ArrayList<>(firstArray.size());

            for (String lineFirstArray : firstArray) {
                String lineFirstArrayLower = replaceInLineAndLower(lineFirstArray);
                int count = 0;

                for (char cF : lineSecondArrayLower.toCharArray()) {
                    if (lineFirstArrayLower.contains(String.valueOf(cF))) {
                        count++;
                        lineFirstArrayLower =
                                lineFirstArrayLower.replaceFirst(String.valueOf(cF), "");
                    }
                }
                listFirst.add(count);
            }
            listSecond.add(listFirst);
        }

        for (int i = 0; i < firstArray.size(); i++) {
            String lineFirstArray = firstArray.get(i);
            if (lineFirstArray.contains(":")) {
                firstArray.set(i, lineFirstArray.replaceAll(":", ""));
            }
        }

        int indexSecond = 0;
        for (List<Integer> listFirst : listSecond) {
            Integer countMaxFirst = listFirst.stream().max(Integer::compareTo).orElse(null);
            int indexFirst = listFirst.indexOf(countMaxFirst);
            String resultGood = new String(new StringBuilder(
                    firstArray.get(indexFirst)).append(" : ").append(secondArray.get(indexSecond)));
            firstArray.set(indexFirst, resultGood);
            indexSecond++;
        }

        for (int i = 0; i < firstArray.size(); i++) {
            String lineFirstArray = firstArray.get(i);
            if (!lineFirstArray.contains(":")) {
                firstArray.set(i, lineFirstArray + " : ?");
            }
        }

        return firstArray;
    }

    public static String replaceInLineAndLower(String line) {
        return line
                .replaceAll("[()\\[\\]<>]", " ")
                .replaceAll("[,!.?]", " ")
                .replaceAll("\\s*(\\s)\\s*", "")
                .toLowerCase();
    }
}
