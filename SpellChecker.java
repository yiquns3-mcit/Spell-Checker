import java.util.Scanner;
import java.io.*;
import java.util.*;

public class SpellChecker {
    // Use this field everytime you need to read user input
    private Scanner inputReader; // DO NOT MODIFY
    private ArrayList<String> engDictionary;
    private ArrayList<String> wordSuggestion;
    private ArrayList<String> inputWordBank;
    private ArrayList<String> outputWordBank;
    private ArrayList<Integer> lineBreakIndex;
    private WordRecommender recommender;

    public SpellChecker() {
        inputReader = new Scanner(System.in); // DO NOT MODIFY - must be included in this method
        // TODO: Complete the body of this constructor, as necessary.
        this.engDictionary = new ArrayList<String>();
        this.wordSuggestion = new ArrayList<String>();
        this.inputWordBank = new ArrayList<String>();
        this.outputWordBank = new ArrayList<String>();
        this.lineBreakIndex = new ArrayList<Integer>();
        this.recommender = null;
    }

    public void start() {
        // TODO: Complete the body of this method, as necessary.

        // (YS) Step 1: Enter the dictionary file and the text file
        String prompt = "Please enter the name of a file to use as a dictionary.";
        String dict = getValidFile(prompt);
        System.out.printf("Using the dictionary at '%s'.%n", dict);

        prompt = "Please enter the name of a file to be spell checked.";
        String text = getValidFile(prompt);
        String outputFile = text.substring(0, text.length() - 4) + "_chk.txt";
        System.out.printf("Spell checking for '%s' will be output in '%s'.%n", text, outputFile);

        // (YS) Step 2: Update inputWordBank and engDictionary (fill in with given .txt files) and WordRecommender
        storeWordInArrayList(dict,engDictionary);
        storeWordInArrayList(text,inputWordBank);
        storeLineIndex(text, lineBreakIndex);
        recommender = new WordRecommender(dict);

        // (YS) Step 3: Pick each word from the inputWordBank and store the updated result into outputWordBank (LOOP)
        // looping
        for (String word: inputWordBank){
            // Step 3.1: Check if misspelled (yes: continue; no: write down and pick next word)
            if (engDictionary.contains(word)) {
                // (not misspell) directly put it into output word bank
                outputWordBank.add(word);
            } else {
                // (misspell) use another word follow by further instruction
                // Step 3.2: try to get word suggestions and store them
                getSuggestion(word);
                // Step 3.3: check if there is any suggestion
                boolean hasSuggestion = (wordSuggestion.size() != 0);
                // Step 3.4: modify the misspelled word based on different cases (hasSuggestion = True/False)
                // the following method will lead user to choose a word based on different cases
                // and store the final result in the outputWordBank
                handleMisspell(word, hasSuggestion);
            }
        }
        // (YS) Step 4: Use the outputWordBank to form the output .txt file
        createOutputFile(outputWordBank, outputFile);

        inputReader.close();  // DO NOT MODIFY - must be the last line of this method!
    }

    // You can of course write other methods as well.

    // (YS) Single Method: Ask user to enter the valid .txt file name
    private String getValidFile(String prompt) {
        while (true) {
            System.out.println(prompt);
            System.out.print(">> ");
            String fileName = inputReader.next();
            try (FileInputStream inputFile = new FileInputStream(fileName)) {
                return fileName;
            } catch (IOException e) {
                System.out.println("There was an error in opening that file.");
            }
        }
    }

    // (YS) Single Method: Read the file and store each word into target wordBank
    private void storeWordInArrayList(String fileName, ArrayList<String> wordBank){
        try {
            FileInputStream inputFile = new FileInputStream(fileName);
            Scanner fileReader= new Scanner(inputFile);
            while (fileReader.hasNext()) {
                String word = fileReader.next();
                wordBank.add(word);
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    // (YS) Single Method: store each \n inside the input .txt file
    private void storeLineIndex(String fileName, ArrayList<Integer> lineBreakIndex){
        try {
            FileInputStream inputFile = new FileInputStream(fileName);
            Scanner fileReader = new Scanner(inputFile);
            int index = 0;
            while (fileReader.hasNextLine()) {
                String line = fileReader.nextLine();
                // if line is empty (record the last word index again)
                if (line.isEmpty()) {
                    lineBreakIndex.add(index - 1);
                    continue; // move to next line
                }
                // if line is not empty
                String[] words = line.split("\\s+"); // split the line into words by " " (any number of space)
                index += words.length; // find the index of the last word in this line
                lineBreakIndex.add(index - 1); // store the index
            }
            fileReader.close();
            inputFile.close();
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    // (YS) Single Method: update the wordSuggestion ArrayList
    private void getSuggestion(String word) {
        // parameters setup for getWordSuggestions method
        int tolerance = 2;
        double commonPercent = 0.5;
        int topN = 4;
        wordSuggestion = recommender.getWordSuggestions(word, tolerance, commonPercent, topN);
    }

    // (YS) Main Method: Enter "r"/"a"/"t" (options) to handle the misspelled word based on different cases
    private void handleMisspell(String misspelledWord, boolean hasSuggestion){
        // text prompt setup
        System.out.printf(Util.MISSPELL_NOTIFICATION, misspelledWord);
        if (hasSuggestion){
            System.out.println("The following suggestions are available:");
            // print out each suggestion
            for (int i = 0; i < wordSuggestion.size(); i++){
                String word = wordSuggestion.get(i);
                System.out.printf("%d. '%s'%n", i+1, word);
            }
            // print out prompt
            System.out.println("Press 'r' to replace, 'a' to accept, and 't' to enter a replacement manually.");
        } else {
            // print out prompt
            System.out.println("There are no suggestions in our dictionary for this word.");
            System.out.println("Press 'a' to accept, or press 't' to enter a replacement manually.");
        }
        // repeat until user gives the valid input
        while (true) {
            try {
                // record user option selection
                System.out.print(">> ");
                String choice = inputReader.next();
                // different modification method for different option selection
                if (choice.equals("r") && hasSuggestion) {
                    // option "r": user choose one word suggestion
                    System.out.println("Your word will be replaced with the suggestion you choose.");
                    String r = wordReplace();
                    outputWordBank.add(r);
                    return;
                } else if (choice.equals("a")) {
                    // option "a": user accept the misspelled word
                    outputWordBank.add(misspelledWord);
                    return;
                } else if (choice.equals("t")) {
                    // option "t": user write down the alternative word by himself
                    String t = wordCustom();
                    outputWordBank.add(t);
                    return;
                } else {
                    // user type the wrong option
                    System.out.println("Please choose one of the valid options.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Please choose one of the valid options.");
            }
        }
    }

    // (YS) Sub Method: user enter "r" to replace the word based on word suggestion
    private String wordReplace(){
        System.out.println("Enter the number corresponding to the word that you want to use for replacement.");
        while (true) {
            try {
                System.out.print(">> ");
                int num = inputReader.nextInt();
                if (num > wordSuggestion.size() || num < 1) {
                    System.out.println("Please enter a valid number.");
                } else {
                    String newWord = wordSuggestion.get(num-1);
                    return newWord;
                }
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid number.");
                inputReader.nextLine(); // prevent infinite loop due to last input
            }
        }
    }

    // (YS) Sub Method: user enter "t" to enter a replacement manually
    private String wordCustom(){
        System.out.println("Please type the word that will be used as the replacement in the output file.");
        while (true) {
            try {
                System.out.print(">> ");
                String custom = inputReader.next();
                return custom;
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid word.");
            }
        }
    }

    // (YS) Single Method: Use all the updated words to form a new .txt file
    private void createOutputFile(ArrayList<String> words, String fileName){
        try (FileOutputStream outputFile = new FileOutputStream(fileName)){
            // write empty line at beginning (if exists)
            for (int index : lineBreakIndex) {
                if (index == -1){
                    outputFile.write("\n".getBytes()); // write \n
                }
            }
            // write into file word by word
            for (int i = 0; i < words.size(); i++) {
                outputFile.write(words.get(i).getBytes());
                // check if there is any line break after this word
                int breakCount = 0;
                for (int index : lineBreakIndex) {
                    if (index == i){
                        breakCount++;
                    }
                }
                // the last word don't need to add \n or ' '
                if (i != words.size()-1) {
                    if (breakCount > 0) {
                        for (int j = 0; j < breakCount; j++) {
                            outputFile.write("\n".getBytes()); // write \n
                        }
                    } else {
                        outputFile.write(' '); // no break then print ' ' as usual
                    }
                }
            }
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

}