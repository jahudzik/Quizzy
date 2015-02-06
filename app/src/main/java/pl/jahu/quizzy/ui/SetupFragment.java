package pl.jahu.quizzy.ui;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
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

    public static final String DIFF_LEVEL_BUNDLE_KEY = "diffLevel";
    public static final String CHOSEN_CATEGORIES_BUNDLE_KEY = "categoriesChosen";
    private static final String CATEGORIES_BUNDLE_KEY = "categories";

    private static final int[] LEVEL_DESCRIPTIONS = {R.string.diff_level_desc_all, R.string.diff_level_desc_75, R.string.diff_level_desc_50, R.string.diff_level_desc_25};
    private static final int[] LEVEL_COLORS = {Color.BLACK, Color.GREEN, Color.rgb(255, 179, 0), Color.RED};
    private static final int MARK_COLOR = Color.rgb(81, 171, 240);

    private OnFragmentInteractionListener listener;

    private Map<String, Integer[]> categoriesSizes;
    private final Set<String> chosenCategories;
    private int actualLevel = Constants.DIFFICULTY_LEVEL_ALL;

    private TextView levelInfoLabel;
    private Button startButton;

    protected static SetupFragment newInstance(Map<String, Integer[]> categoriesSizes) {
        SetupFragment fragment = new SetupFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(CATEGORIES_BUNDLE_KEY, new HashMap<>(categoriesSizes));
        fragment.setArguments(bundle);
        return fragment;
    }

    protected static SetupFragment newInstance(Map<String, Integer[]> categoriesSizes, Set<String> chosenCategories, int chosenLevel) {
        SetupFragment fragment = new SetupFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(CATEGORIES_BUNDLE_KEY, new HashMap<>(categoriesSizes));
        bundle.putInt(DIFF_LEVEL_BUNDLE_KEY, chosenLevel);
        bundle.putStringArrayList(CHOSEN_CATEGORIES_BUNDLE_KEY, new ArrayList<>(chosenCategories));
        fragment.setArguments(bundle);
        return fragment;
    }

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
            actualLevel = savedInstanceState.getInt(DIFF_LEVEL_BUNDLE_KEY);
            chosenCategories.addAll(savedInstanceState.getStringArrayList(CHOSEN_CATEGORIES_BUNDLE_KEY));
        } else {
            if (getArguments().containsKey(DIFF_LEVEL_BUNDLE_KEY) && getArguments().containsKey(CHOSEN_CATEGORIES_BUNDLE_KEY)) {
                // fragment may be initialized with already chosen difficulty level and chosen categories
                actualLevel = getArguments().getInt(DIFF_LEVEL_BUNDLE_KEY);
                chosenCategories.addAll(getArguments().getStringArrayList(CHOSEN_CATEGORIES_BUNDLE_KEY));
            }
        }

        categoriesSizes = (Map) getArguments().getSerializable(CATEGORIES_BUNDLE_KEY);
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
        difficultBar.setProgress(actualLevel);
        difficultBar.setOnSeekBarChangeListener(this);

        updateLayout();
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
        outState.putInt(DIFF_LEVEL_BUNDLE_KEY, actualLevel);
        outState.putStringArrayList(CHOSEN_CATEGORIES_BUNDLE_KEY, new ArrayList<>(chosenCategories));
    }

    private void updateLayout() {
        // count total number of questions chosen
        int totalQuestionsNumber = 0;
        for (String chosenCategory : chosenCategories) {
            totalQuestionsNumber += categoriesSizes.get(chosenCategory)[actualLevel];
        }

        // update start button label and enable/disable it
        if (totalQuestionsNumber > 0) {
            startButton.setText(String.valueOf(getResources().getText(R.string.start_button_label)).replace("#", String.valueOf(totalQuestionsNumber)));
            startButton.setEnabled(true);
        } else {
            startButton.setText(getResources().getText(R.string.start_button_empty_label));
            startButton.setEnabled(false);
        }

        // update level info label
        levelInfoLabel.setText(LEVEL_DESCRIPTIONS[actualLevel]);
        levelInfoLabel.setTextColor(LEVEL_COLORS[actualLevel]);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        actualLevel = progress;

        ListView listView = getListView();
        for (int i = 0; i < listView.getChildCount(); i++) {
            // update questions number for each category
            View view = listView.getChildAt(i);
            TextView nameLabel = (TextView) view.findViewById(R.id.categoryNameLabel);
            TextView sizeLabel = (TextView) view.findViewById(R.id.categorySizeLabel);
            sizeLabel.setText(String.valueOf((int) categoriesSizes.get(nameLabel.getText().toString())[actualLevel]));
        }
        updateLayout();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }


    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onListItemClick(ListView l, View view, int position, long id) {
        CheckBox checkbox = (CheckBox) view.findViewById(R.id.categoryChosenCheckBox);
        TextView nameLabel = (TextView) view.findViewById(R.id.categoryNameLabel);
        TextView sizeLabel = (TextView) view.findViewById(R.id.categorySizeLabel);

        boolean newCheckedState = !checkbox.isChecked();
        checkbox.setChecked(newCheckedState);

        String categoryName = nameLabel.getText().toString();
        if (newCheckedState) {
            chosenCategories.add(categoryName);
        } else {
            chosenCategories.remove(categoryName);
        }

        nameLabel.setTextColor((newCheckedState) ? MARK_COLOR : Color.BLACK);
        sizeLabel.setTextColor((newCheckedState) ? MARK_COLOR : Color.BLACK);
        updateLayout();
    }

    private class CategoryListAdapter extends ArrayAdapter<String> {

        public CategoryListAdapter(Context context, List<String> items) {
            super(context, R.layout.row_category, R.id.categoryNameLabel, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = super.getView(position, convertView, parent);
            TextView nameLabel = (TextView)row.findViewById(R.id.categoryNameLabel);
            TextView sizeLabel = (TextView)row.findViewById(R.id.categorySizeLabel);
            CheckBox checkbox = (CheckBox) row.findViewById(R.id.categoryChosenCheckBox);

            String category = getItem(position);
            boolean checked = chosenCategories.contains(category);
            nameLabel.setText(category);
            nameLabel.setTextColor(checked ? MARK_COLOR : Color.BLACK);
            sizeLabel.setText(String.valueOf((int) categoriesSizes.get(category)[actualLevel]));
            sizeLabel.setTextColor(checked ? MARK_COLOR : Color.BLACK);
            checkbox.setChecked(checked);
            return row;
        }

    }

    public interface OnFragmentInteractionListener {
        public void onStartQuizButtonClicked(Set<String> chosenCategories, int chosenLevel);
    }

}
