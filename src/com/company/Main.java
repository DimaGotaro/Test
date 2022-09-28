package com.company;

import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        String inputFile = "C:\\Users\\dima\\IdeaProjects\\Test\\src\\com\\company\\input.txt";
        String inputFile1 = "C:\\Users\\dima\\IdeaProjects\\Test\\src\\com\\company\\input1.txt";
        String inputFile2 = "C:\\Users\\dima\\IdeaProjects\\Test\\src\\com\\company\\input2.txt";
        String inputFile3 = "C:\\Users\\dima\\IdeaProjects\\Test\\src\\com\\company\\input3.txt";
        String outputFile = "C:\\Users\\dima\\IdeaProjects\\Test\\src\\com\\company\\output.txt";
        String outputFile1 = "C:\\Users\\dima\\IdeaProjects\\Test\\src\\com\\company\\output1.txt";
        String outputFile2 = "C:\\Users\\dima\\IdeaProjects\\Test\\src\\com\\company\\output2.txt";
        String outputFile3 = "C:\\Users\\dima\\IdeaProjects\\Test\\src\\com\\company\\output3.txt";

        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFile));
            BufferedReader bufferedReader1 = new BufferedReader(new FileReader(inputFile1));
            BufferedReader bufferedReader2 = new BufferedReader(new FileReader(inputFile2));
            BufferedReader bufferedReader3 = new BufferedReader(new FileReader(inputFile3));
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFile));
            BufferedWriter bufferedWriter1 = new BufferedWriter(new FileWriter(outputFile1));
            BufferedWriter bufferedWriter2 = new BufferedWriter(new FileWriter(outputFile2));
            BufferedWriter bufferedWriter3 = new BufferedWriter(new FileWriter(outputFile3))) {

            bufferedReaderWriter(bufferedReader, bufferedWriter);
            bufferedReaderWriter(bufferedReader1, bufferedWriter1);
            bufferedReaderWriter(bufferedReader2, bufferedWriter2);
            bufferedReaderWriter(bufferedReader3, bufferedWriter3);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void bufferedReaderWriter(BufferedReader bufferedReader,
                                            BufferedWriter bufferedWriter) throws IOException {
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
    }

    public static List<String> comparison(List<String> firstArray,
                                          List<String> secondArray) {

        TreeMap<String, Integer> treeMapCount = new TreeMap<>((String o1, String o2) -> {
            double s1 = Double.parseDouble(o1.split(" ")[0]);
            double s2 = Double.parseDouble(o2.split(" ")[0]);
            if (s1 < s2) {
                return -1;
            }
            else {
                return 1;
            }
        });

        for (int indexSecond = 0; indexSecond < secondArray.size(); indexSecond++) {
            String lineSecondArrayLower = replaceInLineAndLower(secondArray.get(indexSecond));
            double lengthLineSecond = lineSecondArrayLower.length();

            for (int indexFirst = 0; indexFirst < firstArray.size(); indexFirst++) {
                StringBuilder lineFirstArrayLower = new StringBuilder(
                        replaceInLineAndLower(firstArray.get(indexFirst)));
                double lengthLineFirst = lineFirstArrayLower.length();
                int count = 0;

                for (char cF : lineSecondArrayLower.toCharArray()) {
                    String sF = String.valueOf(cF);
                    int indexChar = lineFirstArrayLower.indexOf(sF);

                    if (indexChar != -1) {
                        count++;
                        lineFirstArrayLower.replace(indexChar, indexChar + 1, "");
                    }
                }
                double absolutCount = count / lengthLineFirst + count / lengthLineSecond;

                treeMapCount.put(new String(new StringBuilder(String.valueOf(absolutCount))
                        .append(" ")
                        .append(indexSecond)), indexFirst);
            }
        }

        HashMap<Integer, Integer> indexMap = new HashMap<>();
        while (indexMap.size() < firstArray.size() &&
                indexMap.size() < secondArray.size()) {
            Map.Entry<String, Integer> maxEntry = treeMapCount.lastEntry();
            int indexSecondMax = Integer.parseInt(maxEntry.getKey().split(" ")[1]);
            Integer indexFirstMax = maxEntry.getValue();

            while (indexMap.containsKey(indexSecondMax) || indexMap.containsValue(indexFirstMax)) {
                treeMapCount.pollLastEntry();

                maxEntry = treeMapCount.lastEntry();
                indexSecondMax = Integer.parseInt(maxEntry.getKey().split(" ")[1]);
                indexFirstMax = maxEntry.getValue();
            }

            firstArray.set(indexFirstMax, new String(new StringBuilder(firstArray.get(indexFirstMax))
                    .append(" : ")
                    .append(secondArray.get(indexSecondMax))));
            indexMap.put(indexSecondMax, indexFirstMax);
            treeMapCount.pollLastEntry();
        }

        if (firstArray.size() < secondArray.size()) {
            for (int i = 0; i < secondArray.size(); i++) {
                if (!indexMap.containsKey(i)) {
                    firstArray.add(secondArray.get(i) + " : ?");
                }
            }
        }
        else if (firstArray.size() > secondArray.size()) {
            for (int i = 0; i < firstArray.size(); i++) {
                if (!indexMap.containsValue(i)) {
                    firstArray.set(i, firstArray.get(i) + " : ?");
                }
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