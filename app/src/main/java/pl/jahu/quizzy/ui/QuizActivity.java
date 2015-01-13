package pl.jahu.quizzy.ui;

import android.os.Bundle;
import pl.jahu.quizzy.app.R;
import pl.jahu.quizzy.models.Question;
import pl.jahu.quizzy.providers.QuizzyDatabase;
import pl.jahu.quizzy.utils.Constants;

import javax.inject.Inject;
import java.util.*;

public class QuizActivity extends BaseActivity implements SetupFragment.OnFragmentInteractionListener, QuizFragment.OnFragmentInteractionListener {

    public static final String CATEGORIES_BUNDLE_KEY = "categories";
    public static final String QUESTIONS_BUNDLE_KEY = "questions";

    @Inject
    QuizzyDatabase quizzyDatabase;

    private List<Question> questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        questions = quizzyDatabase.selectAllQuestions();
        HashMap<String, Integer[]> categoriesSizes = getCategoriesInfo(questions);
        SetupFragment setupFragment = new SetupFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(CATEGORIES_BUNDLE_KEY, categoriesSizes);
        setupFragment.setArguments(bundle);

        setContentView(R.layout.activity_quiz);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, setupFragment)
                    .commit();
        }
    }

    private HashMap<String, Integer[]> getCategoriesInfo(List<Question> questions) {
        HashMap<String, Integer[]> result = new HashMap<>();
        for (Question question : questions) {
            String category = question.getCategory();
            if (!result.containsKey(category)) {
                result.put(category, new Integer[]{0, 0, 0, 0});
            }
            Integer[] sizes = result.get(category);
            sizes[Constants.DIFFICULTY_LEVEL_ALL]++;
            int difficultValue = question.getOverallDifficultValue();
            if (difficultValue < 75) {
                sizes[Constants.DIFFICULTY_LEVEL_BELOW_75]++;
            }
            if (difficultValue < 50) {
                sizes[Constants.DIFFICULTY_LEVEL_BELOW_50]++;
            }
            if (difficultValue < 25) {
                sizes[Constants.DIFFICULTY_LEVEL_BELOW_25]++;
            }
            result.put(category, sizes);
        }


        return result;
    }

    @Override
    public void onStartQuizButtonClicked(Set<String> chosenCategories, int chosenLevel) {
        ArrayList<Question> chosenQuestions = new ArrayList<>();
        for (Question question : questions) {
            if (chosenCategories.contains(question.getCategory()) && question.matchesLevel(chosenLevel)) {
                chosenQuestions.add(question);
            }
        }
        QuizFragment quizFragment = new QuizFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(QUESTIONS_BUNDLE_KEY, chosenQuestions);
        quizFragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .replace(R.id.container, quizFragment)
                .commit();
    }

    @Override
    public void onQuizFinished(List<Question> questions) {
        quizzyDatabase.updateQuestionStats(questions);

        SummaryFragment summaryFragment = new SummaryFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(QUESTIONS_BUNDLE_KEY, new ArrayList(questions));
        summaryFragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .replace(R.id.container, summaryFragment)
                .commit();
    }

}
