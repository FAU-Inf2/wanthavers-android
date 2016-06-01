package wanthavers.mad.cs.fau.de.wanthavers_android.settings;

import android.support.annotation.NonNull;
import static com.google.common.base.Preconditions.checkNotNull;

import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCaseHandler;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetUser;

public class SettingsPresenter implements SettingsContract.Presenter {

    private final SettingsContract.View mSettingsView;
    private final UseCaseHandler mUseCaseHandler;
    private final GetUser mGetUser;

    public SettingsPresenter(@NonNull UseCaseHandler useCaseHandler, @NonNull SettingsContract.View settingsView, @NonNull GetUser getUser) {
        mUseCaseHandler = checkNotNull(useCaseHandler, "useCaseHandler cannot be null");
        mSettingsView = checkNotNull(settingsView, "settings view cannont be null");
        mGetUser = checkNotNull(getUser);

        mSettingsView.setPresenter(this);
    }

    public void getUser(long userId){

        GetUser.RequestValues requestValue = new GetUser.RequestValues(userId);

        mUseCaseHandler.execute(mGetUser, requestValue,
                new UseCase.UseCaseCallback<GetUser.ResponseValue>() {
                    @Override
                    public void onSuccess(GetUser.ResponseValue response) {
                       // mSettingsView.setUser(response.getUser());
                    }

                    @Override
                    public void onError() {
                        // The view may not be able to handle UI updates anymore
                        /*if (!mSettingsView.isActive()) {
                            return;
                        }
                        mSettingsView.showLoadingDesiresError();*/
                    }
                });
    }

    @Override
    public void start() {
        //TODO
    }

}