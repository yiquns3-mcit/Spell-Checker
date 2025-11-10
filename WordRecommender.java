import java.util.*;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
public class WordRecommender {
    private ArrayList<String> dictionary;
    public WordRecommender(String dictionaryFile) {    
      // TODO: change this!
        this.dictionary=new ArrayList<String>();
        try (BufferedReader reader=new BufferedReader(new FileReader(dictionaryFile))){
            String line;
            while((line=reader.readLine())!=null){
                this.dictionary.add(line.trim().toLowerCase());
            }
        }catch(IOException e){
            System.err.println("Error Loading dictionary "+e.getMessage());
        }
    }
    private
  
    public double getSimilarity(String word1, String word2) {
      // TODO: change this!
      return 0.0;
    }
  
    public ArrayList<String> getWordSuggestions(String word, int tolerance, double commonPercent, int topN) {
      // TODO: change this!
        ArrayList<String> candidate = new ArrayList<String>();

        return null;
    }
  
    // You can of course write other methods as well.
  }