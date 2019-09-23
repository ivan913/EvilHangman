package hangman;

import java.util.HashSet;

public class WordGroup {
    private HashSet<String> words;
    private String grouping;

    public WordGroup(String grouping){
        this.grouping = grouping;
        words = new HashSet<String>();
    }

    public WordGroup(String grouping, HashSet<String> words){
        this.grouping = grouping;
        this.words = words;
    }

    public int getSize(){
        return words.size();
    }

    public void addWord(String word){
        words.add(word);
    }

    public String getGrouping(){
        return grouping;
    }

    public HashSet<String> getSet(){
        return words;
    }


}
