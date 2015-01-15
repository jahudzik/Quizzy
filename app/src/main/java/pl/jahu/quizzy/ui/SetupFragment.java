package pl.jahu.quizzy.ui;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import pl.jahu.quizzy.app.R;
import pl.jahu.quizzy.utils.Constants;

import java.util.*;

/**
 * A placeholder fragment containing a simple view.
 */
public class SetupFragment extends ListFragment implements SeekBar.OnSeekBarChangeListener {

    private static final int[] LEVEL_DESCRIPTIONS = {R.string.diff_level_desc_all, R.string.diff_level_desc_75, R.string.diff_level_desc_50, R.string.diff_level_desc_25};
    private static final int[] LEVEL_COLORS = {Color.BLACK, Color.GREEN, Color.rgb(255, 179, 0), Color.RED};
    private static final int MARK_COLOR = Color.rgb(81, 171, 240);

    private OnFragmentInteractionListener listener;

    private Map<String, Integer[]> categoriesSizes;
    private final Set<String> chosenCategories;
    private int actualLevel = Constants.DIFFICULTY_LEVEL_ALL;

    private TextView levelInfoLabel;
    private Button startButton;

    public SetupFragment() {
        chosenCategories = new HashSet<>();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement SetupFragment.OnFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_setup, container, false);

        if (savedInstanceState != null) {
            // handling configuration changes
            actualLevel = savedInstanceState.getInt(QuizActivity.DIFF_LEVEL_BUNDLE_KEY);
            chosenCategories.addAll(savedInstanceState.getStringArrayList(QuizActivity.CHOSEN_CATEGORIES_BUNDLE_KEY));
        }

        categoriesSizes = (HashMap) getArguments().getSerializable(QuizActivity.CATEGORIES_BUNDLE_KEY);
        if (categoriesSizes != null) {
            // fill categories list
            List<String> categoriesNames = new ArrayList<>(categoriesSizes.keySet());
            Collections.sort(categoriesNames);
            setListAdapter(new CategoryListAdapter(inflater.getContext(), categoriesNames));
        }

        levelInfoLabel = (TextView) rootView.findViewById(R.id.difficultInfoLabel);
        startButton = (Button) rootView.findViewById(R.id.startQuizButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onStartQuizButtonClicked(chosenCategories, actualLevel);
            }
        });

        SeekBar difficultBar = (SeekBar) rootView.findViewById(R.id.difficultBar);
        difficultBar.setOnSeekBarChangeListener(this);

        updateStartButton();
        updateLevelInfoLabel();
        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(QuizActivity.DIFF_LEVEL_BUNDLE_KEY, actualLevel);
        outState.putStringArrayList(QuizActivity.CHOSEN_CATEGORIES_BUNDLE_KEY, new ArrayList<>(chosenCategories));
    }

    private void updateDifficulty() {
        ListView listView = getListView();
        for (int i = 0; i < listView.getChildCount(); i++) {
            View view = listView.getChildAt(i);
            TextView nameLabel = (TextView) view.findViewById(R.id.categoryNameLabel);
            TextView sizeLabel = (TextView) view.findViewById(R.id.categorySizeLabel);
            sizeLabel.setText(String.valueOf((int) categoriesSizes.get(nameLabel.getText().toString())[actualLevel]));
        }
        updateStartButton();
        updateLevelInfoLabel();
    }

    private void updateLevelInfoLabel() {
        levelInfoLabel.setText(LEVEL_DESCRIPTIONS[actualLevel]);
        levelInfoLabel.setTextColor(LEVEL_COLORS[actualLevel]);
    }

    private void updateStartButton() {
        int totalQuestionsNumber = 0;
        for (String chosenCategory : chosenCategories) {
            totalQuestionsNumber += categoriesSizes.get(chosenCategory)[actualLevel];
        }

        if (totalQuestionsNumber > 0) {
            startButton.setText(String.valueOf(getResources().getText(R.string.start_button_label)).replace("#", String.valueOf(totalQuestionsNumber)));
            startButton.setEnabled(true);
        } else {
            startButton.setText(getResources().getText(R.string.start_button_empty_label));
            startButton.setEnabled(false);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        actualLevel = progress;
        updateDifficulty();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }


    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }


    private class CategoryListAdapter extends ArrayAdapter<String> implements View.OnTouchListener {

        public CategoryListAdapter(Context context, List<String> items) {
            super(context, R.layout.category_row, R.id.categoryNameLabel, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = super.getView(position, convertView, parent);
            TextView nameLabel = (TextView)row.findViewById(R.id.categoryNameLabel);
            TextView sizeLabel = (TextView)row.findViewById(R.id.categorySizeLabel);
            CheckBox categoryChosenCheckBox = (CheckBox) row.findViewById(R.id.categoryChosenCheckBox);
            String category = getItem(position);
            int questionsCount = categoriesSizes.get(category)[actualLevel];
            nameLabel.setText(category);
            sizeLabel.setText(String.valueOf(questionsCount));
            if (chosenCategories.contains(category)) {
                nameLabel.setTextColor(MARK_COLOR);
                sizeLabel.setTextColor(MARK_COLOR);
                categoryChosenCheckBox.setChecked(true);
            }
            row.setOnTouchListener(this);
            return row;
        }

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            CheckBox categoryChosenCheckBox = (CheckBox) view.findViewById(R.id.categoryChosenCheckBox);
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    TextView nameLabel = (TextView) view.findViewById(R.id.categoryNameLabel);
                    TextView sizeLabel = (TextView) view.findViewById(R.id.categorySizeLabel);

                    boolean newCheckedState = !categoryChosenCheckBox.isChecked();
                    categoryChosenCheckBox.setChecked(newCheckedState);

                    String categoryName = nameLabel.getText().toString();
                    if (newCheckedState) {
                        chosenCategories.add(categoryName);
                    } else {
                        chosenCategories.remove(categoryName);
                    }

                    nameLabel.setTextColor((newCheckedState) ? MARK_COLOR : Color.BLACK);
                    sizeLabel.setTextColor((newCheckedState) ? MARK_COLOR : Color.BLACK);
                    updateStartButton();
                    break;
            }
            return true;
        }

    }

    public interface OnFragmentInteractionListener {
        public void onStartQuizButtonClicked(Set<String> chosenCategories, int chosenLevel);
    }

}
