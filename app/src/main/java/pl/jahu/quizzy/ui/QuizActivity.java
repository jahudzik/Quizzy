package pl.jahu.quizzy.ui;

import android.os.Bundle;
import pl.jahu.quizzy.app.R;
import pl.jahu.quizzy.models.Question;
import pl.jahu.quizzy.providers.QuizzyDatabase;
import pl.jahu.quizzy.utils.Constants;

import javax.inject.Inject;
import java.util.*;

public class QuizActivity extends BaseActivity implements SetupFragment.OnFragmentInteractionListener,
                                                            QuizFragment.OnFragmentInteractionListener,
                                                            SummaryFragment.OnFragmentInteractionListener {

    @Inject
    QuizzyDatabase quizzyDatabase;

    private List<Question> questions;
    private Set<String> chosenCategories;
    private int chosenLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        questions = quizzyDatabase.selectAllQuestions();
        chosenCategories = new HashSet<>();
        SetupFragment setupFragment = SetupFragment.newInstance(getCategoriesSizes(questions));

        setContentView(R.layout.activity_quiz);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, setupFragment)
                    .commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SetupFragment.DIFF_LEVEL_BUNDLE_KEY, chosenLevel);
        outState.putStringArrayList(SetupFragment.CHOSEN_CATEGORIES_BUNDLE_KEY, new ArrayList<>(chosenCategories));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            chosenLevel = savedInstanceState.getInt(SetupFragment.DIFF_LEVEL_BUNDLE_KEY);
            chosenCategories = new HashSet<>(savedInstanceState.getStringArrayList(SetupFragment.CHOSEN_CATEGORIES_BUNDLE_KEY));
        }
    }

    private Map<String, Integer[]> getCategoriesSizes(List<Question> questions) {
        Map<String, Integer[]> result = new HashMap<>();
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
        this.chosenCategories = chosenCategories;
        this.chosenLevel = chosenLevel;

        List<Question> chosenQuestions = new ArrayList<>();
        for (Question question : questions) {
            if (chosenCategories.contains(question.getCategory()) && question.matchesLevel(chosenLevel)) {
                chosenQuestions.add(question);
            }
        }
        QuizFragment quizFragment = QuizFragment.newInstance(chosenQuestions);

        getFragmentManager().beginTransaction()
                .replace(R.id.container, quizFragment)
                .commit();
    }

    @Override
    public void onQuizFinished(List<Question> questions) {
        quizzyDatabase.updateQuestionStats(questions);
        SummaryFragment summaryFragment = SummaryFragment.newInstance(questions);

        getFragmentManager().beginTransaction()
                .replace(R.id.container, summaryFragment)
                .commit();
    }

    @Override
    public void onQuizRepeat() {
        // need to recreate fragment, as it could have been GC and categories stats has changed during last quiz
        questions = quizzyDatabase.selectAllQuestions();
        SetupFragment setupFragment = SetupFragment.newInstance(getCategoriesSizes(questions), chosenCategories, chosenLevel);
        getFragmentManager().beginTransaction()
                .replace(R.id.container, setupFragment)
                .commit();

    }
}
