package hangman;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedSet;

public class EvilHangmanGame implements IEvilHangmanGame{
    HashSet<String> dictionary;

    @Override
    public void startGame(File dictionary, int wordLength) throws IOException, EmptyDictionaryException {
        Scanner scanner = new Scanner(dictionary);

        this.dictionary = new HashSet<String>();

        while(scanner.hasNext()){
            this.dictionary.add(scanner.next());
        }

    }

    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
        return null;
    }

    @Override
    public SortedSet<Character> getGuessedLetters() {
        return null;
    }
}
