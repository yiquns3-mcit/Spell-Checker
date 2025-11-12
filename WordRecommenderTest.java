import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
public class WordRecommenderTest {
    private  WordRecommender recommender;
    @BeforeEach
    public void setUp() {
        recommender = new WordRecommender("engDictionary.txt");
    }


    @Test
    void testConstructor() {
        assertNotNull(recommender);
    }

    @Test
    public void testGetSimilarity1() {
        String word1="Hello";
        String word2="Hall";
        double actual=recommender.getSimilarity(word1,word2);
        double expected=2.0;
        assertEquals(expected,actual);
    }

    @Test
    public void testGetSimilarity2() {
        String word1="Hello";
        String word2="Hello";
        double actual=recommender.getSimilarity(word1,word2);
        double expected=(double)word1.length();
        assertEquals(expected,actual);
    }

    @Test
    public void testGetSimilarity3() {
        String word1="Hello";
        String word2="";
        double actual=recommender.getSimilarity(word1,word2);
        double expected=0.0;
        assertEquals(expected,actual);
    }

    @Test
    public void testGetWordSuggestionsTolerance(){
        String target="talo";
        ArrayList<Integer> actual=new ArrayList<>();
        ArrayList<String> ActualSuggestion= recommender.getWordSuggestions(target,2,0.9,5);
        for(String SuggestionItem:ActualSuggestion){
            int toleranceItem=Math.abs(target.length()-SuggestionItem.length());
            actual.add(toleranceItem);
        }
        for(int item:actual){
            assertTrue(item<=2);
        }
    }

    @Test
    public void testGetWordSuggestionsCommon(){
        String target="talo";
        ArrayList<Double> actual=new ArrayList<>();
        ArrayList<String> ActualSuggestion= recommender.getWordSuggestions(target,2,0.9,5);
        for(String SuggestionItem:ActualSuggestion){
            HashSet<Character>targetSet=new HashSet<>();
            HashSet<Character>recommendationSet=new HashSet<>();
            HashSet<Character>unionSet=new HashSet<>();
            HashSet<Character>intersectSet=new HashSet<>();
            for(int i=0;i<target.length();i++){
                targetSet.add(target.charAt(i));
            }
            for(int i=0;i<SuggestionItem.length();i++){
                recommendationSet.add(SuggestionItem.charAt(i));
            }
            unionSet.addAll(targetSet);
            unionSet.addAll(recommendationSet);
            intersectSet.addAll(targetSet);
            intersectSet.retainAll(recommendationSet);
            double common=(double)intersectSet.size()/ (double)unionSet.size();
            actual.add(common);
        }
        for(double item:actual){
            assertTrue(item>=0.9);
        }
    }

    @Test
    public void testGetWordSuggestionsN1(){
        String target="talo";
        int expected=5;
        ArrayList<String> ActualSuggestion= recommender.getWordSuggestions(target,2,0.9,5);
        assertTrue(ActualSuggestion.size()<expected);

        }


    @Test
    void testGetTopNThroughPublic() {
        ArrayList<String> actual = recommender.getWordSuggestions("app", 2,0.9,3);
        assertNotNull(actual);
        int expected=3;
        assertEquals(expected, actual.size());
    }
}

