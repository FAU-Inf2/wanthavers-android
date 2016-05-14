package wanthavers.mad.cs.fau.de.wanthavers_android.chatdetail;

import android.support.annotation.NonNull;

import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCaseHandler;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetMessageList;
import static com.google.common.base.Preconditions.checkNotNull;

public class ChatDetailPresenter implements ChatDetailContract.Presenter {


    private final ChatDetailContract.View mChatListView;
    private final GetMessageList mGetMessageList;
    private boolean mFirstLoad = true;
    private final UseCaseHandler mUseCaseHandler;


    public ChatDetailPresenter(@NonNull UseCaseHandler useCaseHandler, @NonNull ChatDetailContract.View chatListView,
                             @NonNull GetMessageList getMessageList){

        mUseCaseHandler = checkNotNull(useCaseHandler, "usecaseHandle cannot be null");
        mChatListView = checkNotNull(chatListView, "desirelist view cannot be null!");
        mGetMessageList = checkNotNull(getMessageList);

        mChatListView.setPresenter(this);
    }


    @Override
    public void start() {loadMessages(false);}

    @Override
    public void loadMessages(boolean forceUpdate) {
        loadMessages(forceUpdate || mFirstLoad, true);
        mFirstLoad = false;
    }


    private void loadMessages(boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) {
            mChatListView.setLoadingIndicator(true);
        }
        /*
        GetDesireList.RequestValues requestValue = new GetDesireList.RequestValues();

        mUseCaseHandler.execute(mGetDesireList, requestValue,
                new UseCase.UseCaseCallback<GetDesireList.ResponseValue>() {
                    @Override
                    public void onSuccess(GetDesireList.ResponseValue response) {
                        List<Desire> desires = response.getDesires();
                        // The view may not be able to handle UI updates anymore
                        if (!mChatListView.isActive()) {
                            return;
                        }
                        if (showLoadingUI) {
                            mChatListView.setLoadingIndicator(false);
                        }

                        processDesires(desires);
                    }

                    @Override
                    public void onError() {
                        // The view may not be able to handle UI updates anymore
                        if (!mChatListView.isActive()) {
                            return;
                        }
                        mChatListView.showLoadingDesiresError();
                    }
                });
                */
    }

}
