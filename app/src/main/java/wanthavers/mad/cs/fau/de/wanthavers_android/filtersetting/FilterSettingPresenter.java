package wanthavers.mad.cs.fau.de.wanthavers_android.filtersetting;

import android.support.annotation.NonNull;
import static com.google.common.base.Preconditions.checkNotNull;

import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCaseHandler;

public class FilterSettingPresenter implements FilterSettingContract.Presenter {

    private final FilterSettingContract.View mFilterSettingView;
    private final UseCaseHandler mUseCaseHandler;

    public FilterSettingPresenter(@NonNull UseCaseHandler useCaseHandler, @NonNull FilterSettingContract.View filterSettingView) {
        mUseCaseHandler = checkNotNull(useCaseHandler, "useCaseHandler cannot be null");
        mFilterSettingView = checkNotNull(filterSettingView, "filtersetting view cannot be null");

        mFilterSettingView.setPresenter(this);
    }
    @Override
    public void start() {
        //TODO: getCategories();
    }

}
