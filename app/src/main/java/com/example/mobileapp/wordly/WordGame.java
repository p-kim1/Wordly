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
    private ArrayList<GraphNode<String>> path;

    public WordGame(Context context, int pathSize) {
        this.context = context;
        try {
            wordGraph = new WordGraph(context.getAssets().open("unix_words.graph"));
        } catch (IOException ioException) {
            Log.d(TAG, "Unable to load word graph.");
        }
        this.pathSize = pathSize;
    }

    public void newGame() {
        pathIterator = 0;
        path = wordGraph.getRandomPath(pathSize);
    }

    public String getCurrentWord() {
        int index = Math.min(pathIterator, path.size() - 1);
        return path.get(index).getValue();
    }

    public boolean checkGuess(String guess) {
        if (guess.equals(path.get(pathIterator).getValue())) {
            pathIterator++;
            return true;
        }
        return false;
    }

    public boolean isFinished() {
        return pathIterator == path.size();
    }
}
