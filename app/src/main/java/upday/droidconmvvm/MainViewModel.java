package upday.droidconmvvm;

import android.support.annotation.NonNull;

import com.quanturium.bouquet.annotations.RxLogger;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import upday.droidconmvvm.datamodel.IDataModel;
import upday.droidconmvvm.model.Language;
import upday.droidconmvvm.schedulers.ISchedulerProvider;

/**
 * View model for the main activity.
 */
public class MainViewModel {

    @NonNull
    private final IDataModel dataModel;

    @NonNull
    private final BehaviorSubject<Language> selectedLanguage = BehaviorSubject.create();

    @NonNull
    private final ISchedulerProvider schedulerProvider;

    public MainViewModel(@NonNull final IDataModel dataModel,
                         @NonNull final ISchedulerProvider schedulerProvider) {
        this.dataModel = dataModel;
        this.schedulerProvider = schedulerProvider;
    }

    @RxLogger
    @NonNull
    public Observable<String> getGreeting() {
        return selectedLanguage
                .observeOn(schedulerProvider.computation())
                .map(Language::getCode)
                .flatMap(dataModel::getGreetingByLanguageCode);
    }

    @RxLogger
    @NonNull
    public Observable<List<Language>> getSupportedLanguages() {
        return dataModel.getSupportedLanguages();
    }

    public void languageSelected(@NonNull final Language language) {
        selectedLanguage.onNext(language);
    }

}
