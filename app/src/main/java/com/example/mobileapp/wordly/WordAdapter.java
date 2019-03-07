package com.example.mobileapp.wordly;

import android.support.constraint.ConstraintLayout;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.WordViewHolder> {

     ArrayList<String> theListOfWords;

    public static class WordViewHolder extends RecyclerView.ViewHolder{
        public TextView textView;
        public WordViewHolder(TextView v){
            super(v);
            textView = v;
        }
    }

    public WordAdapter(ArrayList<String> myListOfWords){
        theListOfWords = myListOfWords;
    }

    @Override
    public WordAdapter.WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        TextView v = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.my_text_view, parent, false);
        WordViewHolder vh = new WordViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(WordViewHolder holder, int position){
        holder.textView.setText((theListOfWords.get(position)));
    }

    @Override
    public int getItemCount(){
        return theListOfWords.size();
    }

}
