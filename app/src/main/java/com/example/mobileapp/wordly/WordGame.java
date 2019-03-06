//
// WordGame.java
// Joseph Nguyen
//
package com.example.mobileapp.wordly;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class WordGame {
    public static final String TAG = "WordGame";

    private WordGraph wordGraph;
    private int pathIterator = 0;
    private ArrayList<String> path;

    public WordGame(Context context) {
        try {
            wordGraph = new WordGraph(context.getAssets().open("unix_words.graph"));
        } catch (IOException ioException) {
            Log.d(TAG, "Unable to load word graph.");
        }
    }

    public void newGame(int pathSize) {
        path = new ArrayList<>();
        pathIterator = 0;
        ArrayList<GraphNode<String>> temp = wordGraph.getRandomPath(pathSize);
        for (int i = 0; i < temp.size(); i++) {
            path.add(temp.get(i).getValue());
        }
    }

    public int getPathSize() {
        return path.size();
    }

    public String getCurrentWord() {
        return path.get(pathIterator);
    }

    public String getNextWord() {
        if (pathIterator == path.size() - 1) {
            return null;
        }
        return path.get(pathIterator + 1);
    }

    public boolean checkGuess(String guess) {
        if (isFinished()) {
            return false;
        }

        if (guess.equals(getNextWord())) {
            pathIterator++;
            return true;
        }
        return false;
    }

    public boolean isFinished() {
        return pathIterator == path.size() - 1;
    }

    public ArrayList<String> getCurrentPath() {
        Collections.reverse(path);
        return path;
    }

    public Character getHint() {
        if (pathIterator == path.size() - 1) {
            return null;
        }

        String curr = getCurrentWord();
        String next = getNextWord();

        for (int i = 0; i < curr.length(); i++) {
            if (curr.charAt(i) != next.charAt(i)) {
                return next.charAt(i);
            }
        }

        return null;
    }

    public ArrayList<String> getPath(String aWord, String bWord, int pathLimit) {
        ArrayList<GraphNode<String>> path = wordGraph.getPath(wordGraph.getVertexWithValue(aWord),
                wordGraph.getVertexWithValue(bWord), pathLimit - 1);

        if (path == null) {
            return null;
        }

        ArrayList<String> retVal = new ArrayList<>();
        for (GraphNode<String> node : path) {
            retVal.add(node.getValue());
        }

        return retVal;
    }
}
