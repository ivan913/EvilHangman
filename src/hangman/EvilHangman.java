package hangman;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.TreeSet;

public class EvilHangman {

    public static void main(String[] args) {
        String dictionary = args[0];
        int wordLength = Integer.parseInt(args[1]);
        int guesses = Integer.parseInt(args[2]);

        EvilHangmanGame game = new EvilHangmanGame();

        try{
            game.startGame(new File(dictionary), wordLength);
        }catch(hangman.EmptyDictionaryException | IOException e){
            System.out.println("EmptyDictionaryException thrown");
        }

        Scanner input = new Scanner(System.in);

        while(!game.hasWon() && guesses > 0){
            System.out.println("You have " + guesses + " guesses left");

            System.out.print("Used letters ");
            TreeSet<Character> usedLetters = (TreeSet<Character>) game.getGuessedLetters();
            for(Character c : usedLetters){
                System.out.print(" " + c);
            }
            System.out.print('\n');

            String currentGroup = game.getCurrentGroup();
            System.out.println("Word: " + currentGroup);

            System.out.print("Enter guess: ");
            String guessString = input.next();
            if(isValidInput(guessString));      //input validation
            char guess = guessString.charAt(0);
            HashSet set = null;
            try{
                set = (HashSet<String>) game.makeGuess(guess);
            }catch(hangman.GuessAlreadyMadeException e){
                System.out.println("GuessAlreadyMadeException thrown");
            }

            String newGroup = game.getCurrentGroup();
            if(newGroup.equals(currentGroup)){
                System.out.println("Sorry, there are no " + guess + "'s");
                guesses = guesses - 1;
            }else{
                System.out.println("Yes, there are "
                                    + game.getLetterCount(guess) + " "
                                    + guess + "'s");
            }
        }

        System.out.println("Great, you did it");
    }

    private static boolean isValidInput(String guessString){
        if(guessString.length() > 1) return false;

        Character val = guessString.charAt(0);
        val = Character.toLowerCase(val);

        if(val < 97 || val > 122) return false;

        return true;
    }

}
