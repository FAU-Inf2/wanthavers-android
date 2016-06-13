package wanthavers.mad.cs.fau.de.wanthavers_android.desirelist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.DesireFilter;
import de.fau.cs.mad.wanthavers.common.User;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCaseHandler;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.WantHaversApplication;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetAvgRatingForUser;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetDesireList;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetUser;
import wanthavers.mad.cs.fau.de.wanthavers_android.rest.RestClient;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.SharedPreferencesHelper;

import static com.google.common.base.Preconditions.checkNotNull;

public class DesireListPresenter implements DesireListContract.Presenter {

    private final DesireListContract.View mDesireListView;
    private final GetDesireList mGetDesireList;
    private boolean mFirstLoad = true;
    private final UseCaseHandler mUseCaseHandler;
    private final GetAvgRatingForUser mGetAvgRatingForUser;
    private final GetUser mGetUser;
    private List<DesireItemViewModel> mDesireModels = new ArrayList<>();
    private int counter = 0;
    private DesireListType mDesireListType;
    private long mLoggedInUser;
    private Context mAppContext;
    private List<Desire> mDesireListAll = new ArrayList<>();
    private List<Desire> mDesireListMy = new ArrayList<>();
    private List<Desire> mDesireListTrans = new ArrayList<>();
    private static final int DESIRE_LOAD_LIMIT  = 2;

    public DesireListPresenter(@NonNull UseCaseHandler useCaseHandler, @NonNull DesireListContract.View desireListView,
                               @NonNull GetDesireList getDesireList, @NonNull GetAvgRatingForUser getAvgRatingForUser, @NonNull GetUser getUser,
                               @NonNull Context context){

        mUseCaseHandler = checkNotNull(useCaseHandler, "usecaseHandle cannot be null");
        mDesireListView = checkNotNull(desireListView, "desirelist view cannot be null!");
        mGetDesireList = checkNotNull(getDesireList);
        mGetAvgRatingForUser = checkNotNull(getAvgRatingForUser);
        mGetUser = checkNotNull(getUser);
        mAppContext = checkNotNull(context, "context cannot be null");
        mDesireListView.setPresenter(this);
    }

    @Override
    public void start(){loadDesires(false, false);}


    public void loadDesires(boolean forceUpdate, boolean loadOlderDesires){

        loadDesiresAccToType(forceUpdate || mFirstLoad, true, loadOlderDesires);

        mFirstLoad = false;
    }

    @Override
    public void createNewDesire() {
        //TODO add create new desire Logic here
        mDesireListView.showNewDesire();
    }

    @Override
    public void createLogout() {
        //TODO add create new desire Logic here

        //destroy user settings in shared preferences



        mDesireListView.showLogout();
    }

    @Override
    public void openSettings() {
        mDesireListView.showSettings();
    }

    @Override
    public void openMyDesires(){ mDesireListView.showMyDesires();}

    @Override
    public void openMyTransactions(){ mDesireListView.showMyTransactions();}

    @Override
    public void openAllDesires(){ mDesireListView.showAllDesires();}

    @Override
    public void openDesireDetails(@NonNull Desire desire) {
        checkNotNull(desire, "desire cannot be null!");
        mDesireListView.showDesireDetailsUi(desire.getId());
    }


    public void setDesireListType(DesireListType desireListType){
        mDesireListType = desireListType;
    }

    private void loadDesiresAccToType(boolean forceUpdate, final boolean showLoadingUI,
                                      final boolean loadOlderDesires) {
        if (showLoadingUI) {
            mDesireListView.setLoadingIndicator(true);
        }

        GetDesireList.RequestValues requestValue = new GetDesireList.RequestValues(mDesireListType, mAppContext);
        requestValue.setUserId(mLoggedInUser);
        setCurFilterForDesireList(loadOlderDesires);


        mUseCaseHandler.execute(mGetDesireList, requestValue,
                new UseCase.UseCaseCallback<GetDesireList.ResponseValue>() {
                    @Override
                    public void onSuccess(GetDesireList.ResponseValue response) {
                        List<Desire> desires = setDesireListAccToType();

                        if(loadOlderDesires){
                            desires.addAll(response.getDesires());   //TODO JuG: check if desireListIsUpdated

                        }else{
                            desires = response.getDesires();
                        }

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

    public void setUser(long userId){
        mLoggedInUser = userId;
    }

    public void getUser(long userId){

        GetUser.RequestValues requestValue = new GetUser.RequestValues(userId);

        mUseCaseHandler.execute(mGetUser, requestValue,
                new UseCase.UseCaseCallback<GetUser.ResponseValue>() {
                    @Override
                    public void onSuccess(GetUser.ResponseValue response) {
                        mDesireListView.setUser(response.getUser());
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



    /*  TODO (TASK_JUG1) keep as discussion base and for future reference
    public void loadRatingsForDesires(final List<DesireItemViewModel> desireList) {

        Desire desire = desireList.get(counter).getDesire();

        GetAvgRatingForUser.RequestValues requestValue = new GetAvgRatingForUser.RequestValues(desire.getCreator().getId());

        mUseCaseHandler.execute(mGetAvgRatingForUser, requestValue,
                new UseCase.UseCaseCallback<GetAvgRatingForUser.ResponseValue>() {
                    @Override
                    public void onSuccess(GetAvgRatingForUser.ResponseValue response) {
                        Rating rating = response.getRating();
                        // The view may not be able to handle UI updates anymore


                        desireList.get(counter).setRating(rating);
                        if(counter < desireList.size()-1){
                            counter++;
                            loadRatingsForDesires(desireList);
                        }else{
                            //loadChatsWithUsers(chatList,userList);
                            counter = 0;
                            mDesireListView.showDesires(desireList);
                        }

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
    */

    private void processDesires(List<Desire> desires) {
        if (desires.isEmpty()) {
            // Show a message indicating there are no tasks for that filter type.
            //TODO add what to do if no desires
        } else {
            // Show the list of tasks
            Collections.reverse(desires);   //TODO JuG - might need to go as nico changed sort of desirelist on server

            List<DesireItemViewModel> desireModels = new ArrayList<>();

            for(Desire desire: desires){
                desireModels.add(new DesireItemViewModel(desire));
            }

            /* TODO (TASK_JUG1) keep as discussion base and for future reference
            counter = 0;
            loadRatingsForDesires(desireModels);
            */
            mDesireListView.showDesires(desireModels);
        }
    }


    public void openChat(User user){
        //TODO: open chat here;
        checkNotNull(user, "user cannot be null!");
        mDesireListView.showChatList(user.getId());
    }

    @Override
    public void openFilterSettings() {
        mDesireListView.showFilterSettings();
    }


    private List<Desire> setDesireListAccToType() {
        List<Desire> desires = null;

        switch (mDesireListType) {
            case ALL_DESIRES:
                desires = mDesireListAll;
                break;
            case MY_DESIRES:
                desires = mDesireListMy;
                break;
            case MY_TRANSACTIONS:
                desires = mDesireListTrans;
                break;
            default:
                desires = mDesireListAll;
        }

        return desires;
    }

    private void setCurFilterForDesireList(boolean loadOlderDesires){

        DesireFilter curDesireFilter = WantHaversApplication.getCurDesireFilter(mAppContext);

        if(curDesireFilter == null){
            curDesireFilter = new DesireFilter();
        }

        curDesireFilter.setLimit(DESIRE_LOAD_LIMIT);

        Date lastCreationTime = new Date();

        List<Desire> desires;

        switch (mDesireListType) {
            case ALL_DESIRES:
                desires = mDesireListAll;
                break;
            case MY_DESIRES:
                desires = mDesireListMy;
                break;
            case MY_TRANSACTIONS:
                desires = mDesireListTrans;
                break;
            default:
                desires = mDesireListAll;
        }

        if(loadOlderDesires == true) {
            lastCreationTime = desires.get(desires.size() - 1).getCreation_time();
        }

        curDesireFilter.setLastCreationTime(lastCreationTime);
        WantHaversApplication.setCurDesireFilter(curDesireFilter);
    }


}
