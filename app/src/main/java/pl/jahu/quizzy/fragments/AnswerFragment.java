package pl.jahu.quizzy.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import pl.jahu.quizzy.app.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class AnswerFragment extends Fragment {

    public static final String TOUCH_TO_SEE_MESSAGE = "Touch to see the answer";

    private OnFragmentInteractionListener listener;
    private TextView questionTextView;
    private Button correctAnswerButton;
    private Button wrongAnswerButton;
    private String answer;

    public AnswerFragment() {
    }

    public void setAnswer(String answer) {
        this.answer = answer;
        questionTextView.setText(TOUCH_TO_SEE_MESSAGE);
        correctAnswerButton.setEnabled(false);
        wrongAnswerButton.setEnabled(false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_question, container, false);
        questionTextView = (TextView) rootView.findViewById(R.id.answerTextView);
        questionTextView.setText(TOUCH_TO_SEE_MESSAGE);
        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN && questionTextView.getText().equals(TOUCH_TO_SEE_MESSAGE)) {
                    questionTextView.setText(answer);
                    correctAnswerButton.setEnabled(true);
                    wrongAnswerButton.setEnabled(true);
                }
                return true;
            }
        });

        correctAnswerButton = (Button) rootView.findViewById(R.id.correctAnswerButton);
        correctAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCorrectAnswer();
            }
        });

        wrongAnswerButton = (Button) rootView.findViewById(R.id.wrongAnswerButton);
        wrongAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onWrongAnswer();
            }
        });
        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface OnFragmentInteractionListener {
        public void onCorrectAnswer();
        public void onWrongAnswer();
    }

}
