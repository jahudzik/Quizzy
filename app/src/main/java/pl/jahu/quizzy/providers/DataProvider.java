package pl.jahu.quizzy.providers;

import pl.jahu.quizzy.models.Category;
import pl.jahu.quizzy.models.Question;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Quzzy
 * Created by jahudzik on 2014-12-15.
 */
public interface DataProvider {

    public Map<Category, Integer> getCategories();

    public List<Question> getQuestionsByCategories(List<Category> categories);

}
