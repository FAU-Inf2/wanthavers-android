package wanthavers.mad.cs.fau.de.wanthavers_android.desirelist;

import android.app.Activity;
import android.support.annotation.NonNull;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.User;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCaseHandler;
import wanthavers.mad.cs.fau.de.wanthavers_android.desiredetail.DesireDetailContract;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetDesireList;

import static com.google.common.base.Preconditions.checkNotNull;

public class DesireListPresenter implements DesireListContract.Presenter {

    private final DesireListContract.View mDesireListView;
    private final GetDesireList mGetDesireList;
    private boolean mFirstLoad = true;
    private final UseCaseHandler mUseCaseHandler;


    public DesireListPresenter(@NonNull UseCaseHandler useCaseHandler, @NonNull DesireListContract.View desireListView,
                               @NonNull GetDesireList getDesireList){

        mUseCaseHandler = checkNotNull(useCaseHandler, "usecaseHandle cannot be null");
        mDesireListView = checkNotNull(desireListView, "desirelist view cannot be null!");
        mGetDesireList = checkNotNull(getDesireList);

        mDesireListView.setPresenter(this);
    }

    @Override
    public void start(){loadDesires(false);}


    public void loadDesires(boolean forceUpdate){
        loadDesires(forceUpdate || mFirstLoad, true);
        mFirstLoad = false;
    }

    @Override
    public void createNewDesire() {
        //TODO add create new desire Logic here
        mDesireListView.showNewDesire();
    }

    @Override
    public void openDesireDetails(@NonNull Desire desire) {
        checkNotNull(desire, "desire cannot be null!");
        mDesireListView.showDesireDetailsUi(desire.getID());
    }


    private void loadDesires(boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) {
            mDesireListView.setLoadingIndicator(true);
        }

        GetDesireList.RequestValues requestValue = new GetDesireList.RequestValues();

        mUseCaseHandler.execute(mGetDesireList, requestValue,
                new UseCase.UseCaseCallback<GetDesireList.ResponseValue>() {
                    @Override
                    public void onSuccess(GetDesireList.ResponseValue response) {
                        List<Desire> desires = response.getDesires();
                        // The view may not be able to handle UI updates anymore
                        if (!mDesireListView.isActive()) {
                            return;
                        }
                        if (showLoadingUI) {
                            mDesireListView.setLoadingIndicator(false);
                        }

                        processDesires(desires);
                    }

                    @Override
                    public void onError() {
                        // The view may not be able to handle UI updates anymore
                        if (!mDesireListView.isActive()) {
                            return;
                        }
                        mDesireListView.showLoadingDesiresError();
                    }
                });
    }


    private void processDesires(List<Desire> desires) {
        if (desires.isEmpty()) {
            // Show a message indicating there are no tasks for that filter type.
            //TODO add what to do if no desires
        } else {
            // Show the list of tasks
            mDesireListView.showDesires(desires);
            // Set the filter label's text.
        }
    }


    public void openChat(User user){
        //TODO: open chat here;
        checkNotNull(user, "user cannot be null!");
        mDesireListView.showChatList(user.getID());
    }

}
