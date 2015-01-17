package pl.jahu.quizzy;

import dagger.Module;
import dagger.Provides;
import pl.jahu.quizzy.providers.AssetsBasedDatabase;
import pl.jahu.quizzy.providers.QuizzyDatabase;
import pl.jahu.quizzy.ui.QuizActivity;

import javax.inject.Singleton;

/**
 * Quizzy
 * Created by jahudzik on 2014-12-18.
 */
@Module(
        injects = QuizActivity.class
)
class AppModule {

    private final QuizzyApplication application;

    public AppModule(QuizzyApplication application) {
        this.application = application;
    }


    @Provides
    @Singleton
    @SuppressWarnings("unused")
    QuizzyDatabase provideDatabase() {
        return new AssetsBasedDatabase(application);
    }

}
