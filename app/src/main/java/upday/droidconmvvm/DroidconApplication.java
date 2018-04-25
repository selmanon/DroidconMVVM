package upday.droidconmvvm;

import android.app.Application;
import android.support.annotation.NonNull;

import upday.droidconmvvm.datamodel.DataModel;
import upday.droidconmvvm.datamodel.IDataModel;
import upday.droidconmvvm.schedulers.ISchedulerProvider;
import upday.droidconmvvm.schedulers.SchedulerProvider;

public class DroidconApplication extends Application {

    @NonNull
    private final IDataModel dataModel;

    public DroidconApplication() {
        dataModel = new DataModel();
    }

    @NonNull
    public IDataModel getDataModel() {
        return dataModel;
    }

    @NonNull
    public ISchedulerProvider getSchedulerProvider() {
        return SchedulerProvider.getInstance();
    }

    @NonNull
    public MainViewModel getViewModel() {
        return new MainViewModel(getDataModel(), getSchedulerProvider());
    }

}
