package hangman;

import java.util.HashSet;

public class WordGroup {
    private HashSet<String> words;
    private String grouping;
    private int rightness;
    private int frequency;

    public WordGroup(String grouping){
        this.grouping = grouping;
        this.words = new HashSet<String>();
        this.rightness = makeRight();
        this.frequency = makeFrequency();
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

    private int makeRight(){
        int rightness = 0;

        for(int i = 0; i < grouping.length(); i += 1){
            if (grouping.charAt(i) != '-'){
                int val = i+1;
                rightness += val*(10*val);
            }
        }
        return rightness;
    }

    public int getRightness(){
        return this.rightness;
    }

    private int makeFrequency(){
        int count = 0;
        for(int i = 0; i < grouping.length(); i += 1){
            if (grouping.charAt(i) != '-') count +=1;
        }
        return count;
    }

    public int getFrequency(){
        return frequency;
    }




}
