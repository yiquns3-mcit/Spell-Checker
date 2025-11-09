import java.util.Scanner;
import java.io.*;
import java.util.*;

public class SpellChecker {
    // Use this field everytime you need to read user input
    private Scanner inputReader; // DO NOT MODIFY

    public SpellChecker() {
        inputReader = new Scanner(System.in); // DO NOT MODIFY - must be included in this method
        // TODO: Complete the body of this constructor, as necessary.
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

        // (YS) Step 2:

        inputReader.close();  // DO NOT MODIFY - must be the last line of this method!
    }

    // You can of course write other methods as well.

    // (YS) Method: Ask user to enter the valid .txt file name
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

    // (YS) Method: Extract words one by one from the text file

}