package wanthavers.mad.cs.fau.de.wanthavers_android.chatlist;

import android.support.annotation.NonNull;

import de.fau.cs.mad.wanthavers.common.Chat;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCaseHandler;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetChatList;
import java.util.List;

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

        GetChatList.RequestValues requestValue = new GetChatList.RequestValues();

        mUseCaseHandler.execute(mGetChatList, requestValue,
                new UseCase.UseCaseCallback<GetChatList.ResponseValue>() {
                    @Override
                    public void onSuccess(GetChatList.ResponseValue response) {
                        List<Chat> chatList = response.getChats();
                        // The view may not be able to handle UI updates anymore
                        if (!mChatListView.isActive()) {
                            return;
                        }
                        if (showLoadingUI) {
                            mChatListView.setLoadingIndicator(false);
                        }

                        processChats(chatList);
                    }

                    @Override
                    public void onError() {
                        // The view may not be able to handle UI updates anymore
                        if (!mChatListView.isActive()) {
                            return;
                        }
                        mChatListView.showLoadingChatsError();
                    }
                });

    }



    private void processChats(List<Chat> chatList) {
        if (chatList.isEmpty()) {
            // Show a message indicating there are no tasks for that filter type.
            //TODO add what to do if no desires
        } else {
            // Show the list of tasks
            mChatListView.showChats(chatList);
            // Set the filter label's text.
        }
    }

    @Override
    public void openChatDetails(@NonNull Chat chat) {
        checkNotNull(chat, "Message cannot be null!");
        //TODO implement opening chat details
        // mChatListView.showChatDetailsUi(chat.);
    }

}
