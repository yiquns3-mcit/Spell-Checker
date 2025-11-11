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
  
    public double getSimilarity(String word1, String word2) {
      // TODO: change this!
        double left_similarity=0.0;
        double right_similarity=0.0;
        int word1_length=word1.length();
        int word2_length=word2.length();
        int min_length=Math.min(word1.length(),word2.length());
        int max_length=Math.max(word1.length(),word2.length());
        int length_diff=max_length-min_length;
        for (int i=0;i<min_length;i++){
            if(word1.charAt(i)==word2.charAt(i)){
                left_similarity++;
            }
        }
        for (int i=max_length-1;i>=min_length;i--){
            if(word1.length()>word2_length){
                if(word1.charAt(i)==word2.charAt(i-length_diff)){
                    right_similarity++;

                }
            }
            else{
                if(word2.charAt(i)==word1.charAt(i-length_diff)){
                    right_similarity++;
            }
        }
            double similarity=(left_similarity+right_similarity)/2;
        return similarity;
    }
  
    public ArrayList<String> getWordSuggestions(String word, int tolerance, double commonPercent, int topN) {
      // TODO: change this!
        ArrayList<String> candidate = new ArrayList<String>();
        for(String dic_elem:dictionary){
            if(Math.abs(word.length()-dic_elem.length())>tolerance){
                continue;
            }
            HashSet<Character> word_set1=new HashSet<>();
            HashSet<Character> word_set2=new HashSet<>();
            HashSet<Character> dic_elem_set=new HashSet<>();
            for(int i=0;i<word.length();i++){
                word_set1.add(word.charAt(i));
                word_set2.add(word.charAt(i));
            }
            for(int i=0;i<dic_elem.length();i++){
                dic_elem_set.add(dic_elem.charAt(i));
            }
            word_set1.addAll(dic_elem_set);
            int union_length=word_set1.size();
            word_set2.retainAll(dic_elem_set);
            int intersection_length=word_set2.size();
            double common=(double)intersection_length/(double)union_length;
            if (common>commonPercent){
                candidate.add(dic_elem);
            }
        }


        return null;
    }
  
    // You can of course write other methods as well.
  }