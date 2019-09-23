package hangman;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class EvilHangmanGame implements IEvilHangmanGame{
    HashSet<String> dictionary;
    HashSet<String> currentDictionary;
    TreeSet<Character> guessedLetters;
    int wordLength;
    String blankGroup;

    @Override
    public void startGame(File dictionary, int wordLength) throws IOException, EmptyDictionaryException {
        Scanner scanner = new Scanner(dictionary);
        this.wordLength = wordLength;
        this.dictionary = new HashSet<String>();
        this.guessedLetters = new TreeSet<Character>();

        while(scanner.hasNext()){
            String word = scanner.next();
            if(word.length() == wordLength){
                this.dictionary.add(word);
                this.currentDictionary.add(word);
            }
        }

        blankGroup = makeBlankGroup();

        scanner.close();
    }

    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
        HashMap<String, WordGroup> wordGroups = makeWordGroups(guess);
        WordGroup largestGroup = getLargest(wordGroups);
        this.guessedLetters.add(guess);

        return largestGroup.getSet();
    }

    @Override
    public SortedSet<Character> getGuessedLetters() {
        return guessedLetters;
    }

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
                groups.put(word, newWordGroup);
            }
        }

        return groups;
    }

    private String makeBlankGroup(){
        StringBuilder builder = new StringBuilder();

        for(int i = 0; i < this.wordLength; i += 1){
            builder.append('-');
        }

        return builder.toString();
    }

    private WordGroup getLargest(HashMap<String, WordGroup> groups){
        int largestSize = 0;
        WordGroup largestGroup = null;

        for(String key : groups.keySet()){
            WordGroup currentGroup = groups.get(key);
            int currentSize = currentGroup.getSize();
            if(currentSize > largestSize){
                largestGroup = currentGroup;
                largestSize = currentSize;
            }
        }

        //TODO: Implement Evil Algorithm 3(b)

        return largestGroup;
    }
}
