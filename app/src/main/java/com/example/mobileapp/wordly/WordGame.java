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
    static final String TAG = "WordGame";

    // Singleton object.
    private static WordGame wordGame;

    private WordGraph wordGraph;
    private int pathIterator = 0;
    private ArrayList<String> path;

    // Default constructor to instantiate.
    private WordGame(Context context) {
        try {
            wordGraph = new WordGraph(context.getAssets().open("unix_words.graph"));
        } catch (IOException ioException) {
            Log.d(TAG, "Unable to load word graph.");
        }
    }

    // Returns the single instance of WordGame.
    static WordGame getInstance(Context context) {
        if (wordGame == null) {
            wordGame = new WordGame(context);
        }
        return wordGame;
    }

    // Initializes a new game with the specified path size.
    void newGame(int pathSize) {
        path = new ArrayList<>();
        pathIterator = 0;
        ArrayList<GraphNode<String>> temp = wordGraph.getRandomPath(pathSize);
        for (int i = 0; i < temp.size(); i++) {
            path.add(temp.get(i).getValue());
        }
    }

    // Returns the word that the user is currently trying to guess.
    String getCurrentWord() {
        if (path == null || path.size() == 0) {
            return null;
        }
        return path.get(pathIterator);
    }

    // Returns the current path size.
    Integer getPathSize() {
        if (path == null) {
            return null;
        }
        return path.size();
    }

    // Returns the end word.
    String getTargetWord() {
        if (path == null || path.size() == 0) {
            return null;
        }
        return path.get(path.size() - 1);
    }

    // Returns the word that is next in the path.
    String getNextWord() {
        if (path == null || path.size() == 0 || pathIterator == path.size() - 1) {
            return null;
        }
        return path.get(pathIterator + 1);
    }

    // Checks the user's guess and returns a boolean accordingly.
    // Updates the internal path iterator to advance to the next word for use by getCurrentWord().
    boolean checkGuess(String guess) {
        if (isFinished()) {
            return false;
        }

        if (guess.equals(getNextWord())) {
            pathIterator++;
            return true;
        }
        return false;
    }

    // Returns true if the path does not exist or if the iterator is one word from the end.
    // The reason for this is that the target word should already be revealed.
    public boolean isFinished() {
        return path == null || pathIterator == path.size() - 2;
    }

    public Character getHint() {
        String curr = getCurrentWord();
        String next = getNextWord();

        // There was no word found because there is no path or the game is over.
        if (curr == null || next == null) {
            return null;
        }

        // Find the difference and return it as the hint.
        for (int i = 0; i < curr.length(); i++) {
            if (curr.charAt(i) != next.charAt(i)) {
                return next.charAt(i);
            }
        }

        // Some unexpected behavior occurred and we don't know what hint to provide.
        return null;
    }

    // Allows the user to supply words with a valid path within some limit.
    public void setPath(String aWord, String bWord, int pathLimit) {
        ArrayList<GraphNode<String>> temp = wordGraph.getPath(wordGraph.getVertexWithValue(aWord),
                wordGraph.getVertexWithValue(bWord), pathLimit - 1);

        // Only update the current path if there was a valid path found.
        if (temp != null) {
            path = new ArrayList<>();
            for (GraphNode<String> node : temp) {
                path.add(node.getValue());
            }
        }
    }

    // Moves the iterator back to the beginning.
    void resetGame() {
        pathIterator = 0;
    }
}
