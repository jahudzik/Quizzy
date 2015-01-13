package pl.jahu.quizzy.ui;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import pl.jahu.quizzy.app.R;
import pl.jahu.quizzy.models.Question;

import java.util.Collections;
import java.util.List;

/**
 * Quizzy
 * Created by jahudzik on 2015-01-08.
 */
public class SummaryFragment extends ListFragment {

    private List<Question> questions;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            this.questions = getArguments().getParcelableArrayList(QuizActivity.QUESTIONS_BUNDLE_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.questions = getArguments().getParcelableArrayList(QuizActivity.QUESTIONS_BUNDLE_KEY);

        View rootView = inflater.inflate(R.layout.fragment_summary, container, false);
        Collections.sort(questions, new Question.CurrentDifficultValueComparator());
        setListAdapter(new QuestionsListAdapter(inflater.getContext(), questions));
        return rootView;
    }

    private class QuestionsListAdapter extends ArrayAdapter<Question> {

        public QuestionsListAdapter(Context context, List<Question> items) {
            super(context, R.layout.question_stats_row, R.id.questionLabel, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = super.getView(position, convertView, parent);
            TextView questionLabel = (TextView) row.findViewById(R.id.questionLabel);
            TextView statsLabel = (TextView) row.findViewById(R.id.questionStatsLabel);
            Question question = questions.get(position);
            questionLabel.setText(question.getQuestion());
            statsLabel.setText(question.getQuizDifficultValue() + "% (" + question.getQuizCorrectAnswers() + "/" + question.getQuizAnswers() + ")");
            return row;
        }
    }

}
