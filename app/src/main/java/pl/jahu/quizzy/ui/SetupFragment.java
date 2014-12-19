package pl.jahu.quizzy.ui;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 */
public class SetupFragment extends ListFragment implements SeekBar.OnSeekBarChangeListener {

    private static final int MARK_COLOR = Color.rgb(81, 171, 240);
    private static final int LEVEL_100_COLOR = Color.BLACK;
    private static final int LEVEL_75_COLOR = Color.GREEN;
    private static final int LEVEL_50_COLOR = Color.rgb(255, 179, 0);
    private static final int LEVEL_25_COLOR = Color.RED;


    private Map<String, Integer[]> categoriesSizes;
    private List<String> categoriesNames;
    private int actualLevel = Constants.DIFFICULTY_LEVEL_ALL;
    private int totalQuestionsNumber = 0;

    private TextView totalLabel;
    private TextView levelInfoLabel;
    private Button startButton;

    public SetupFragment() {
        categoriesNames = new ArrayList<>();
    }

    public void setCategoriesSizes(Map<String, Integer[]> categoriesSizes) {
        this.categoriesSizes = categoriesSizes;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_setup, container, false);
        totalLabel = (TextView) rootView.findViewById(R.id.totalCountLabel);
        levelInfoLabel = (TextView) rootView.findViewById(R.id.difficultInfoLabel);
        startButton = (Button) rootView.findViewById(R.id.startQuizButton);

        categoriesNames.addAll(categoriesSizes.keySet());
        Collections.sort(categoriesNames);
        setListAdapter(new CategoryListAdapter(inflater.getContext(), categoriesNames));

        SeekBar difficultBar = (SeekBar) rootView.findViewById(R.id.difficultBar);
        difficultBar.setOnSeekBarChangeListener(this);

        updateLevelInfoLabel(getResources().getString(R.string.diff_level_desc_all), LEVEL_100_COLOR);
        return rootView;
    }

    private void updateDifficulty() {
        switch (actualLevel) {
            case Constants.DIFFICULTY_LEVEL_ALL:
                updateLevelInfoLabel(getResources().getString(R.string.diff_level_desc_all), LEVEL_100_COLOR);
                break;
            case Constants.DIFFICULTY_LEVEL_BELOW_75:
                updateLevelInfoLabel(getResources().getString(R.string.diff_level_desc_75), LEVEL_75_COLOR);
                break;
            case Constants.DIFFICULTY_LEVEL_BELOW_50:
                updateLevelInfoLabel(getResources().getString(R.string.diff_level_desc_50), LEVEL_50_COLOR);
                break;
            case Constants.DIFFICULTY_LEVEL_BELOW_25:
                updateLevelInfoLabel(getResources().getString(R.string.diff_level_desc_25), LEVEL_25_COLOR);
                break;
        }

        ListView listView = getListView();
        totalQuestionsNumber = 0;
        for (int i = 0; i < listView.getChildCount(); i++) {
            View rowView = listView.getChildAt(i);
            TextView sizeLabel = (TextView) rowView.findViewById(R.id.categorySizeLabel);
            CheckBox chosenCheckBox = (CheckBox) rowView.findViewById(R.id.categoryChosenCheckBox);
            int questionsCount = categoriesSizes.get(categoriesNames.get(i))[actualLevel];
            sizeLabel.setText(String.valueOf(questionsCount));
            if (chosenCheckBox.isChecked()) {
                totalQuestionsNumber += questionsCount;
            }
        }
        totalLabel.setText(String.valueOf(totalQuestionsNumber));
    }

    private void updateLevelInfoLabel(String message, int color) {
        levelInfoLabel.setText(message);
        levelInfoLabel.setTextColor(color);
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
            String category = getItem(position);
            nameLabel.setText(category);
            sizeLabel.setText(String.valueOf(categoriesSizes.get(category)[Constants.DIFFICULTY_LEVEL_ALL]));
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

                    boolean isChecked = !categoryChosenCheckBox.isChecked();
                    categoryChosenCheckBox.setChecked(isChecked);

                    String categoryName = nameLabel.getText().toString();
                    int questionsCount = categoriesSizes.get(categoryName)[actualLevel];
                    totalQuestionsNumber = (isChecked) ? totalQuestionsNumber + questionsCount : totalQuestionsNumber - questionsCount;

                    nameLabel.setTextColor((isChecked) ? MARK_COLOR : Color.BLACK);
                    sizeLabel.setTextColor((isChecked) ? MARK_COLOR : Color.BLACK);
                    totalLabel.setText(String.valueOf(totalQuestionsNumber));
                    startButton.setEnabled(totalQuestionsNumber > 0);
                    break;
            }
            return true;
        }
    }

}
