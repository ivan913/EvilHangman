package hangman;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class EvilHangmanGame implements IEvilHangmanGame{
    private HashSet<String> dictionary;
    private HashSet<String> currentDictionary;
    private TreeSet<Character> guessedLetters;
    private int wordLength;
    private String blankGroup;
    private String currentGroup;

    @Override
    public void startGame(File dictionary, int wordLength) throws IOException, EmptyDictionaryException {
        Scanner scanner = new Scanner(dictionary);
        this.wordLength = wordLength;
        this.dictionary = new HashSet<String>();
        this.currentDictionary = new HashSet<String>();
        this.guessedLetters = new TreeSet<Character>();

        while(scanner.hasNext()){
            String word = scanner.next();
            if(word.length() == wordLength){
                this.dictionary.add(word);
                this.currentDictionary.add(word);
            }
        }

        if(this.dictionary.size() == 0) throw new EmptyDictionaryException();

        this.blankGroup = makeBlankGroup(wordLength);
        this.currentGroup = this.blankGroup;

        scanner.close();
    }

    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
        char currentGuess = Character.toLowerCase(guess);

        if(this.guessedLetters.contains(currentGuess)) {
            throw new GuessAlreadyMadeException();
        }

        HashMap<String, WordGroup> wordGroups = makeWordGroups(currentGuess);
        WordGroup bestGroup = getBest(wordGroups);
        this.guessedLetters.add(currentGuess);

        this.currentGroup = makeCurrentGroup(bestGroup.getGrouping());
        this.currentDictionary = bestGroup.getSet();

        return bestGroup.getSet();
    }

    public Boolean hasWon(){
        return currentDictionary.size() <= 1;
    }

    @Override
    public SortedSet<Character> getGuessedLetters() {
        return guessedLetters;
    }

    public String getCurrentGroup(){
        return currentGroup;
    }

    public int getLetterCount(Character c){
        int count = 0;

        for(int i = 0; i < this.currentGroup.length(); i += 1){
            if(this.currentGroup.charAt(i) == c){
                count += 1;
            }
        }

        return count;
    }

    private String makeCurrentGroup(String bestGroup){
        StringBuilder newCurrentGroup = new StringBuilder(this.currentGroup);
        int length = newCurrentGroup.toString().length();

        for(int i = 0; i < length; i += 1){
            if(newCurrentGroup.charAt(i) == '-' && bestGroup.charAt(i) != '-'){
                newCurrentGroup.setCharAt(i, bestGroup.charAt(i));
            }
        }

        return newCurrentGroup.toString();
    } //Unsure, needs testing

    private HashMap<String, WordGroup> makeWordGroups(Character c){
        HashSet<String> shortList = new HashSet<String>();
        HashMap<String, WordGroup> groups = new HashMap<String, WordGroup>();

        WordGroup emptyGroup = new WordGroup(this.blankGroup);
        for(String word : currentDictionary){
            if(word.contains(c.toString())){
                shortList.add(word);
            }
            else{
                emptyGroup.addWord(word);
            }
        }

        groups.put(this.blankGroup,emptyGroup);

        for(String word : shortList){
            StringBuilder newGroup = new StringBuilder(this.blankGroup);
            for(int i = 0; i < this.wordLength; i += 1){
                if(word.charAt(i) == c){
                    newGroup.setCharAt(i, c);
                }
            }
            String currentGroup = newGroup.toString();
            if(groups.containsKey(currentGroup)){
                groups.get(currentGroup).addWord(word);
            }
            else{
                WordGroup newWordGroup = new WordGroup(currentGroup);
                newWordGroup.addWord(word);
                groups.put(newWordGroup.getGrouping(), newWordGroup);
            }
        }

        return groups;
    }

    private String makeBlankGroup(int wordLength){
        StringBuilder builder = new StringBuilder();

        for(int i = 0; i < this.wordLength; i += 1){
            builder.append('-');
        }

        return builder.toString();
    }

    private WordGroup getBest(HashMap<String, WordGroup> groups){
        HashMap<String, WordGroup> largestGroups = getLargest(groups);

        if(largestGroups.size() == 1){              //if only 1 left, return it
            for(String key : largestGroups.keySet()){
                return largestGroups.get(key);
            }
        }

        if(largestGroups.containsKey(blankGroup)){  //if more than 1 left, but does not contain letter, return it
            return largestGroups.get(blankGroup);
        }

        HashMap<String, WordGroup> leastFrequentGroups = getLeastFrequent(largestGroups);

        if(leastFrequentGroups.size() == 1){              //if only 1 least frequent group left, return it
            for(String key : leastFrequentGroups.keySet()){
                return leastFrequentGroups.get(key);
            }
        }

        return getRightMost(leastFrequentGroups);         //if more than 1 groups containing letter and is least frequent exists, get right most
    }

    private HashMap<String, WordGroup> getLargest(HashMap<String, WordGroup> groups){
        int largestSize = 0;
        HashMap<String, WordGroup> largestGroups = new HashMap<String, WordGroup>();

        for(String key : groups.keySet()){  //Find largest possible set size
            WordGroup currentGroup = groups.get(key);
            int currentSize = currentGroup.getSize();
            if(currentSize > largestSize){
                largestSize = currentSize;
            }
        }

        for(String key : groups.keySet()){  //Gather largest sets in one HashMap
            WordGroup currentGroup = groups.get(key);
            int size = currentGroup.getSize();
            if(size == largestSize){
                largestGroups.put(key, currentGroup);
            }
        }

        return largestGroups;
    }

    private WordGroup getRightMost(HashMap<String, WordGroup> groups){
        int mostRightness = 0;

        for(String key : groups.keySet()){
            int rightness = groups.get(key).getRightness();
            if(rightness > mostRightness){
                mostRightness = rightness;
            }
        }

        WordGroup rightMost = null;

        for(String key : groups.keySet()){
            WordGroup current = groups.get(key);
            if(current.getRightness() == mostRightness){
                rightMost = current;
            }
        }

        return rightMost;
    }

    private HashMap<String, WordGroup> getLeastFrequent(HashMap<String, WordGroup> groups){
        int lowestFrequency = this.wordLength;

        for(String key : groups.keySet()){
            int currentFrequency = groups.get(key).getFrequency();
            if(currentFrequency < lowestFrequency){
                lowestFrequency = currentFrequency;
            }
        }

        HashMap<String, WordGroup> returnGroups = new HashMap<String, WordGroup>();

        for(String key : groups.keySet()){
            WordGroup currentGroup = groups.get(key);
            if(currentGroup.getFrequency() == lowestFrequency){
                returnGroups.put(key, currentGroup);
            }
        }

        return returnGroups;
    }

}
