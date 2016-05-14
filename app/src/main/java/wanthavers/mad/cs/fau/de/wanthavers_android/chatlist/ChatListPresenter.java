package wanthavers.mad.cs.fau.de.wanthavers_android.chatlist;

import android.support.annotation.NonNull;

import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCaseHandler;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetChatList;
import static com.google.common.base.Preconditions.checkNotNull;


public class ChatListPresenter implements ChatListContract.Presenter {


    private final ChatListContract.View mChatListView;
    private final GetChatList mGetChatList;
    private boolean mFirstLoad = true;
    private final UseCaseHandler mUseCaseHandler;


    public ChatListPresenter(@NonNull UseCaseHandler useCaseHandler, @NonNull ChatListContract.View chatListView,
                               @NonNull GetChatList getChatList){

        mUseCaseHandler = checkNotNull(useCaseHandler, "usecaseHandle cannot be null");
        mChatListView = checkNotNull(chatListView, "desirelist view cannot be null!");
        mGetChatList = checkNotNull(getChatList);

        mChatListView.setPresenter(this);
    }


    @Override
    public void start() {loadChats(false);}

    @Override
    public void loadChats(boolean forceUpdate) {
        loadChats(forceUpdate || mFirstLoad, true);
        mFirstLoad = false;
    }


    private void loadChats(boolean forceUpdate, final boolean showLoadingUI) {
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

    @Override
    public void openChatDetails(@NonNull Chat chat) {
        checkNotNull(chat, "desire cannot be null!");
        mChatListView.showChatDetailsUi(chat.getID());
    }

}
