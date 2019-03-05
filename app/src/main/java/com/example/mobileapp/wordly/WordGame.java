//
// WordGame.java
// Joseph Nguyen
//
package com.example.mobileapp.wordly;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

public class WordGame {
    // android stuff
    public static final String TAG = "WordGame";
    private Context context;

    private int pathSize;
    private WordGraph wordGraph;
    private int pathIterator = 0;
    private ArrayList<String> path;

    public WordGame(Context context, int pathSize) {
        this.context = context;
        try {
            wordGraph = new WordGraph(context.getAssets().open("unix_words.graph"));
        } catch (IOException ioException) {
            Log.d(TAG, "Unable to load word graph.");
        }
        this.pathSize = pathSize;
        newGame();
    }

    public void newGame() {
        path = new ArrayList<>();
        pathIterator = 0;
        ArrayList<GraphNode<String>> temp = wordGraph.getRandomPath(pathSize);
        for (int i = 0; i < temp.size(); i++) {
            path.add(temp.get(i).getValue());
        }
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

    public ArrayList<String> getPath(String aWord, String bWord, int depthLimit) {
        ArrayList<GraphNode<String>> path = wordGraph.getPath(wordGraph.getVertexWithValue(aWord),
                wordGraph.getVertexWithValue(bWord), depthLimit);

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
