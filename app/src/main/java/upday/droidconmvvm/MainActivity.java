package upday.droidconmvvm;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.disposables.CompositeDisposable;
import upday.droidconmvvm.model.Language;

public class MainActivity extends AppCompatActivity {

    @NonNull
    private CompositeDisposable compositeDisposable;

    @NonNull
    private MainViewModel viewModel;

    @Nullable
    private TextView greetingView;

    @Nullable
    private Spinner languagesSpinner;

    @Nullable
    private LanguageSpinnerAdapter languageSpinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = getViewModel();
        setupViews();
    }

    private void setupViews() {
        greetingView = (TextView) findViewById(R.id.greeting);

        languagesSpinner = (Spinner) findViewById(R.id.languages);
        assert languagesSpinner != null;
        languagesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parent, final View view,
                                       final int position, final long id) {
                itemSelected(position);
            }

            @Override
            public void onNothingSelected(final AdapterView<?> parent) {
                //nothing to do here
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        bind();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unBind();
    }

    private void bind() {
        compositeDisposable = new CompositeDisposable();

        compositeDisposable.add(viewModel.getGreeting()
                                    .subscribeOn(Schedulers.computation())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(this::setGreeting));

        compositeDisposable.add(viewModel.getSupportedLanguages()
                                    .subscribeOn(Schedulers.computation())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(this::setLanguages));
    }

    private void unBind() {
        compositeDisposable.clear();
    }

    private void setGreeting(@NonNull final String greeting) {
        assert greetingView != null;

        greetingView.setText(greeting);
    }

    private void setLanguages(@NonNull final List<Language> languages) {
        assert languagesSpinner != null;

        languageSpinnerAdapter = new LanguageSpinnerAdapter(this,
                                                             R.layout.language_item,
                                                             languages);
        languagesSpinner.setAdapter(languageSpinnerAdapter);
    }

    @NonNull
    private MainViewModel getViewModel() {
        return ((DroidconApplication) getApplication()).getViewModel();
    }

    private void itemSelected(final int position) {
        assert languageSpinnerAdapter != null;

        Language languageSelected = languageSpinnerAdapter.getItem(position);
        viewModel.languageSelected(languageSelected);
    }
}
