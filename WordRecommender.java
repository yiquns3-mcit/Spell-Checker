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
        double leftSimilarity = 0.0;
        double rightSimilarity = 0.0;
        int word1Length = word1.length();
        int word2Length = word2.length();
        int minLength = Math.min(word1.length(), word2.length());
        for (int i = 0; i < minLength; i++) {
            if (word1.charAt(i) == word2.charAt(i)) {
                leftSimilarity++;
            }
        }
        for (int i = 1; i <= minLength; i++) {
            if (word1.charAt(word1Length -i)==word2.charAt(word2Length -i)){
                rightSimilarity++;
            }
        }
        double similarity = (leftSimilarity + rightSimilarity) / 2;
        return similarity;
    }
  
    public ArrayList<String> getWordSuggestions(String word, int tolerance, double commonPercent, int topN) {
      // TODO: change this!
        ArrayList<String> candidate = new ArrayList<String>();
        for(String dicElem :dictionary){
            if(Math.abs(word.length()- dicElem.length())>tolerance){
                continue;
            }
            HashSet<Character> wordSet =new HashSet<>();
            HashSet<Character> dicElemSet =new HashSet<>();
            for(int i=0;i<word.length();i++){
                wordSet.add(word.charAt(i));
            }
            for(int i = 0; i< dicElem.length(); i++){
                dicElemSet.add(dicElem.charAt(i));
            }
            HashSet<Character> unionSet =new HashSet<>();
            HashSet<Character> intersectSet =new HashSet<>();
            unionSet.addAll(wordSet);
            unionSet.addAll(dicElemSet);
            int unionLength = unionSet.size();
            if(unionLength ==0) continue;
            intersectSet.addAll(wordSet);
            intersectSet.retainAll(dicElemSet);
            int intersectionLength = intersectSet.size();
            double common=(double) intersectionLength /(double) unionLength;
            if (common>=commonPercent){
                candidate.add(dicElem);
            }
        }
        ArrayList<String> topnSuggestion =getTopN(candidate,word,Math.min(topN,candidate.size()));
        return topnSuggestion;
    }


    // You can of course write other methods as well.
    private ArrayList<String> getTopN(ArrayList<String> candidate, String target, int N ){

        ArrayList<Double> similarityVector=new ArrayList<>();
        ArrayList<String> topNSuggestion=new ArrayList<>();

        for(int i=0;i<candidate.size();i++){
            String candidateElem =candidate.get(i);
            double similarity=getSimilarity(target, candidateElem);
            similarityVector.add(similarity);

        }
        if(!similarityVector.isEmpty()){
            for(int j=0;j<N && !similarityVector.isEmpty();j++) {
                int highest_similarity_index=0;
                for (int i = 1; i < similarityVector.size(); i++) {
                    if (similarityVector.get(i) > similarityVector.get(highest_similarity_index)) {
                        highest_similarity_index = i;
                    }
                }
                topNSuggestion.add(candidate.get(highest_similarity_index));
                candidate.remove(highest_similarity_index);
                similarityVector.remove(highest_similarity_index);
            }

        }
        return topNSuggestion;
    }
  }