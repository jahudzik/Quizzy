package pl.jahu.quizzy.providers;

import pl.jahu.quizzy.models.Category;
import pl.jahu.quizzy.models.Question;

import java.util.*;

/**
 * Quizzy
 * Created by jahudzik on 2014-12-16.
 */
public class SampleDataProvider implements DataProvider {

    private static Map<Category, List<Question>> QUESTIONS = new HashMap<>();

    private static Category ANDROID_CODENAMES_CATEGORY = new Category("Android codenames");
    private static Category CAPITAL_CITIES_CATEGORY = new Category("Capital cities");
    private static Category SPANISH_WORDS_CATEGORY = new Category("German words");

    static {
        List<Question> categoryQuestions = new ArrayList<>();
        categoryQuestions.add(new Question("Ice Cream Sandwitch", "4.0\nAPI 14-15", ANDROID_CODENAMES_CATEGORY));
        categoryQuestions.add(new Question("Jelly Bean", "4.1-4.3\nAPI 16-18", ANDROID_CODENAMES_CATEGORY));
        categoryQuestions.add(new Question("KitKat", "4.4\nAPI 19", ANDROID_CODENAMES_CATEGORY));
        categoryQuestions.add(new Question("Lollipop", "5.0\nAPI 20-21", ANDROID_CODENAMES_CATEGORY));
        QUESTIONS.put(ANDROID_CODENAMES_CATEGORY, categoryQuestions);

        categoryQuestions = new ArrayList<>();
        categoryQuestions.add(new Question("Albania", "Tirana", CAPITAL_CITIES_CATEGORY));
        categoryQuestions.add(new Question("Montenegro", "Podgorica", CAPITAL_CITIES_CATEGORY));
        categoryQuestions.add(new Question("Estonia", "Tallinn", CAPITAL_CITIES_CATEGORY));
        categoryQuestions.add(new Question("Kosovo", "Pristina", CAPITAL_CITIES_CATEGORY));
        categoryQuestions.add(new Question("Switzerland", "Bern", CAPITAL_CITIES_CATEGORY));
        QUESTIONS.put(CAPITAL_CITIES_CATEGORY, categoryQuestions);

        categoryQuestions = new ArrayList<>();
        categoryQuestions.add(new Question("dog", "el perro", SPANISH_WORDS_CATEGORY));
        categoryQuestions.add(new Question("computer", "el ordenador", SPANISH_WORDS_CATEGORY));
        categoryQuestions.add(new Question("trousers", "los pantalones", SPANISH_WORDS_CATEGORY));
        categoryQuestions.add(new Question("airplane", "el aeroplano", SPANISH_WORDS_CATEGORY));
        categoryQuestions.add(new Question("sandwich", "el bocadillo", SPANISH_WORDS_CATEGORY));
        QUESTIONS.put(SPANISH_WORDS_CATEGORY, categoryQuestions);
    }

    @Override
    public Map<Category, Integer> getCategories() {
        Map<Category, Integer> result = new HashMap<>();
        for (Category category : QUESTIONS.keySet()) {
            result.put(category, QUESTIONS.get(category).size());
        }
        return result;
    }

    @Override
    public List<Question> getQuestionsByCategories(List<Category> categories) {
        List<Question> result = new ArrayList<>();
        for (Category category : categories) {
            result.addAll(QUESTIONS.get(category));
        }
        return result;
    }

}
