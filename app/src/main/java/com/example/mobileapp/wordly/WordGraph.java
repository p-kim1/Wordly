//
// WordGraph.java
// Joseph Nguyen
//
package com.example.mobileapp.wordly;

import android.support.v4.util.Pair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;

class WordGraph extends Graph<String> {
    public WordGraph(InputStream inputStream) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            // read the file line by line
            String line = reader.readLine();
            while (line != null && line.length() > 0) {
                // replace symbols
                line = line.replaceAll("[\\[:,\\]]", "");

                // split strings on whitespace; first word is vertex of focus, other words are edges
                String[] words = line.split("\\s+");

                GraphNode<String> newWord = new GraphNode<>(words[0]);
                for (int i = 1; i < words.length; i++) {
                    GraphNode<String> curr = getVertexWithValue(words[i]);

                    // if the vertex doesnt exist
                    if (curr == null) {
                        curr = new GraphNode<>(words[i]);
                        valueMap.put(curr.getValue(), curr);
                        vertices.add(curr);
                    }

                    // add two way edge
                    newWord.addNeighbor(curr);
                    curr.addNeighbor(newWord);
                }
                valueMap.put(newWord.getValue(), newWord);
                line = reader.readLine();
            }
        } catch (IOException ioException) {
        }
    }

    public WordGraph(File file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file.toString()));

            // read the words
            String word = reader.readLine();
            while (word != null) {
                // construct word node
                word = word.replaceAll("[-+'.^:,]","");
                GraphNode<String> newWord = new GraphNode<>(word.toLowerCase());

                // add appropriate edges using the edge function
                int currentGraphSize = vertices.size();
                for (int i = 0; i < currentGraphSize; i++) {
                    GraphNode<String> currentWord = vertices.get(i);

                    if (onlyOneChange(currentWord.getValue(), newWord.getValue())) {
                        currentWord.addNeighbor(newWord);
                        newWord.addNeighbor(currentWord);
                    }
                }

                // add new word to overall graph
                valueMap.put(newWord.getValue(), newWord);
                vertices.add(newWord);

                // next word
                word = reader.readLine();
            }

            // clean up
            reader.close();

        } catch (FileNotFoundException fileNotFoundException) {
            // TODO: do something
        } catch (IOException ioException) {
            // TODO: do something
        }

        prune();
    }

    private int numberOfDifferences(String aWord, String bWord) {
        if (aWord == null || bWord == null) {
            return -1;
        }

        if (aWord.length() != bWord.length()) {
            return -1;
        }

        int changeCount = 0;
        for (int i = 0; i < aWord.length(); i++) {
            char aChar = aWord.charAt(i);
            char bChar = bWord.charAt(i);

            if (aChar != bChar)  {
                changeCount++;
            }
        }

        return changeCount;
    }

    // edge function for WordGraph
    private boolean onlyOneChange(String aWord, String bWord) {
        return numberOfDifferences(aWord, bWord) == 1;
    }

}
