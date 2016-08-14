package com.hackathon.inout.middleman;

import java.util.List;

/**
 * Created by Sarthak on 14/08/16.
 */
public class Words {

    public List<String> getWords() {
        return words;
    }

    public void setWords(List<String> words) {
        this.words = words;
    }

    public List<String> words;



    @Override
    public String toString() {
        String s="";
        for(String x:words)
        {
            s=s+x+",";
        }
        return s;
    }
}
