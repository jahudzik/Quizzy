package pl.jahu.quizzy;

import dagger.Module;
import dagger.Provides;
import pl.jahu.quizzy.providers.QuizzyDatabase;
import pl.jahu.quizzy.providers.QuizzyTestDatabase;
import pl.jahu.quizzy.ui.SetupActivity;

import javax.inject.Singleton;

/**
 * Quizzy
 * Created by jahudzik on 2014-12-18.
 */
@Module(
        injects = SetupActivity.class
)
public class AppModule {

    private final QuizzyApplication application;

    public AppModule(QuizzyApplication application) {
        this.application = application;
    }


    @Provides
    @Singleton
    @SuppressWarnings("unused")
    QuizzyDatabase provideDatabase() {
        return new QuizzyTestDatabase(application);
    }

}
