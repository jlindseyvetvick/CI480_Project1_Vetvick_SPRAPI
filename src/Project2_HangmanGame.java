import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Project2_HangmanGame {

	public static void main(String[] args) throws FileNotFoundException {
		// initiate game play by asking the user for their name
		System.out.println("Welcome to the Hangman Game!");
		System.out.println("Type your name below: ");
		Scanner scan = new Scanner(System.in);
		String playerName = scan.nextLine();

		// ask user for game file
		System.out.println("Enter the name of the text file (ie: Colors) from which you'd like to play a game. ");
		String fileName = new String(scan.nextLine());

		// test validity of fileName
		while (!fileName.equals("Answers") && !fileName.equals("Answers1") && !fileName.equals("Colors")
				&& !fileName.equals("Adjectives")) {
			System.out.println("The file name you have entered is invaild.");
			System.out.println(
					"Please enter the name of a valid text file (ie: Colors) from which you'd like to play a game. ");
			fileName = scan.nextLine();
		}

		// run method to read file and create list array
		String[] wordList = fileListToArray(fileName);

		// shuffle order of words in array
		shuffle(wordList);

		// create and initialize variables for player score, number of games, and
		// number of incorrect guesses
		double playerScore = 0;
		double numberOfGames = 0;

		// create and initialize variable for while loop to play again
		String keepPlaying = "yes";

		// initiate "while playing loop"
		while (keepPlaying.equals("yes")) {
			System.out.println("");
			System.out.println("Let's Play Hangman, " + playerName + "!");

			for (int i = 0; i <= wordList.length; i++) {
				// test if maximum number of puzzles have been solved in selected file
				if (i == (wordList.length)) {
					System.out.println(playerName
							+ ", you have worked through all of the puzzles from the selected file. Restart the program to play again.");
					keepPlaying = "no";
					break;
				}
				
				String currentPuzzle = wordList[i];

				playerScore = playerScore + playHangman(currentPuzzle);
				numberOfGames++;

				double percentCorrect = (playerScore / numberOfGames) * 100;

				// prior to looping again, ask user if they would like to
				// play another game
				System.out.println("");
				System.out.println(playerName + ", you have solved " + playerScore + " of " + numberOfGames
						+ " puzzles. A success rate of: " + percentCorrect + "%.");
				System.out.println("Would you like to play again? YES or NO ");
				keepPlaying = scan.nextLine();
				keepPlaying = keepPlaying.toLowerCase();

				// break while loop if user chooses not to continue game play
				if (keepPlaying.equals("no")) {
					break;
				}
			}

		}
		// end game System.out.println("");
		System.out.println("End of game. ");
	}

	// method to read text file & create array of words included in the text file
	public static String[] fileListToArray(String textFileName) throws FileNotFoundException {
		File userFile = new File(textFileName);
		Scanner fileScanner = new Scanner(userFile);

		int listLength = fileScanner.nextInt();
		fileScanner.nextLine();

		String[] listArray = new String[listLength];

		for (int i = 0; i < listLength; i++) {
			String currentLine = fileScanner.nextLine();
			listArray[i] = currentLine;
		}
		fileScanner.close();
		return listArray;
	}

	// method to shuffle words in array
	public static void shuffle(String[] array) {
		Random rand = new Random();
		for (int i = array.length - 1; i > 0; --i) {
			int j = rand.nextInt(i + 1);
			String temp = array[i];
			array[i] = array[j];
			array[j] = temp;
		}
	}

	// method to create array of letters for selected word
	public static char[] currentPuzzleLettersToArray(String selectedWord) throws FileNotFoundException {
		char[] letterArray = selectedWord.toCharArray();
		return letterArray;
	}

	// method to create array of blanks for chosen word
	public static char[] arrayOfBlanks(char[] lettersArray) throws FileNotFoundException {
		char[] blankArray = new char[lettersArray.length];

		for (int i = 0; i < lettersArray.length; i++) {
			blankArray[i] = '_';
		}

		return blankArray;
	}

	// method to collect user input as a guess
	public static char askUserGuess() {
		Scanner s = new Scanner(System.in);
		System.out.println("Please enter a letter to make a guess: ");
		String userInput = s.nextLine();
		userInput = userInput.toLowerCase();
		char userGuess = userInput.charAt(0);
		return userGuess;
	}

	// method to check user input against letters in current puzzle, update the
	// game array if guess is correct
	public static boolean isGuessCorrect(char userGuess, char[] currentPuzzle, char[] updatedArray) {
		int correctGuess = 0;
		char currentChar = userGuess;

		for (int i = 0; i < currentPuzzle.length; i++) {
			if (currentChar == currentPuzzle[i]) {
				updatedArray[i] = currentChar;
				correctGuess++;
			}
		}
		if (correctGuess > 0) {
			return true;

		} else {
			return false;
		}

	}

	// method to compare two char arrays for user win or loss
	public static boolean puzzleSolvedTest(char[] currentPuzzle, char[] updatedArray) {
		if (Arrays.equals(currentPuzzle, updatedArray)) {
			return true;
		}
		return false;
	}

	// method to play a round of hangman
	public static int playHangman(String currentPuzzle) throws FileNotFoundException {
		char[] lettersArray = currentPuzzleLettersToArray(currentPuzzle);
		char[] updatedArray = (arrayOfBlanks(lettersArray));
		char userGuess;
		int wrongGuessNum = 0;

		printHangman(wrongGuessNum);

		System.out.println("");

		System.out.println("Puzzle: " + Arrays.toString(updatedArray));

		while (wrongGuessNum < 6) {

			// run method to collect user guess & set return value to userGuess
			userGuess = askUserGuess();

			boolean guessResult = isGuessCorrect(userGuess, lettersArray, updatedArray);

			if (guessResult == true) {
				System.out.println("");
				System.out.println("Way to go! A '" + userGuess + "' has been found in the puzzle.");
				System.out.println("Puzzle: " + Arrays.toString(updatedArray));
				System.out.println("");
			}

			else if (guessResult == false) {
				System.out.println("");
				System.out.println("Oh, man! A '" + userGuess + "' was not found in the puzzle. ");
				System.out.println("Puzzle: " + Arrays.toString(updatedArray));
				System.out.println("");
				wrongGuessNum++;
				printHangman(wrongGuessNum);

			}

			if (puzzleSolvedTest(lettersArray, updatedArray) == true) {
				return 1;
			}
		}

		return 0;

	}

	// method to print hangman graphic, written by Max and Luc
	public static void printHangman(int numIncorrectGuesses) {
		// Print the first line
		System.out.println(" ___________");
		// Print the second line
		System.out.println(" |/ |");
		// Print the third line
		System.out.print(" |");
		if (numIncorrectGuesses >= 1) {
			System.out.println(" (_)");
		} else {
			System.out.println("");
		}
		// Print the fourth line
		System.out.print(" |");
		if (numIncorrectGuesses >= 4) {
			// The body and both arms
			System.out.println(" \\|/");
		} else if (numIncorrectGuesses >= 3) {
			// The body and one arm
			System.out.println(" \\|");
		} else if (numIncorrectGuesses >= 2) {
			// The body
			System.out.println("  |");
		} else {
			System.out.println("");
		}
		// Print the fifth line
		System.out.print(" |");
		if (numIncorrectGuesses >= 2) {
			// The body
			System.out.println("  |");
		} else {
			System.out.println("");
		}
		// Print the sixth line
		System.out.print(" |");
		if (numIncorrectGuesses >= 6) {
			// Both legs
			System.out.println(" / \\");
		} else if (numIncorrectGuesses >= 5) {
			// One leg
			System.out.println(" /");
		} else {
			System.out.println("");
		}
		// Print the seventh line
		System.out.println(" |");
		// Print the eighth line
		System.out.println(" |___");
	}

}
