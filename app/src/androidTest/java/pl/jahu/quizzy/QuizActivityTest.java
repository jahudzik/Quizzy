package pl.jahu.quizzy;

import android.app.Activity;
import android.app.Fragment;
import android.widget.TextView;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import pl.jahu.quizzy.app.R;
import pl.jahu.quizzy.ui.AnswerFragment;
import pl.jahu.quizzy.ui.QuizActivity;
import pl.jahu.quizzy.ui.StatsFragment;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Quizzy
 * Created by jahudzik on 2014-12-16.
 */
@Config(manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class QuizActivityTest {

    @Test
    public void onCreate_test() {
        Activity activity = Robolectric.buildActivity(QuizActivity.class).create().get();

        Fragment statsFragment = activity.getFragmentManager().findFragmentById(R.id.statsPlaceholder);
        Fragment answerFragment = activity.getFragmentManager().findFragmentById(R.id.answerPlaceholder);
        TextView questionTextView = (TextView) activity.findViewById(R.id.questionTextView);

        assertTrue(statsFragment instanceof StatsFragment);
        assertTrue(answerFragment instanceof AnswerFragment);
        assertNotNull(questionTextView);
    }

}
