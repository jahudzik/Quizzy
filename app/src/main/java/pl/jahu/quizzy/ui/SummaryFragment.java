package pl.jahu.quizzy.ui;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import pl.jahu.quizzy.app.R;
import pl.jahu.quizzy.models.Question;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Quizzy
 * Created by jahudzik on 2015-01-08.
 */
public class SummaryFragment extends ListFragment {

    private static final String QUESTIONS_BUNDLE_KEY = "questions";

    private OnFragmentInteractionListener listener;

    private List<Question> questions;

    private Button repeatButton;

    protected static SummaryFragment newInstance(List<Question> questions) {
        SummaryFragment fragment = new SummaryFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(QUESTIONS_BUNDLE_KEY, new ArrayList(questions));
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement SummaryFragment.OnFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.questions = getArguments().getParcelableArrayList(QUESTIONS_BUNDLE_KEY);

        View rootView = inflater.inflate(R.layout.fragment_summary, container, false);

        repeatButton = (Button) rootView.findViewById(R.id.repeatButton);
        repeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onQuizRepeat();
            }
        });

        Collections.sort(questions, new Question.CurrentDifficultValueComparator());
        setListAdapter(new QuestionsListAdapter(inflater.getContext(), questions));
        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
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

    public interface OnFragmentInteractionListener {
        public void onQuizRepeat();
    }

}
