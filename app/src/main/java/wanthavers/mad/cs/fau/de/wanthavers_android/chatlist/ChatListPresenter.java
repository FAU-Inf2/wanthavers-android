package wanthavers.mad.cs.fau.de.wanthavers_android.chatlist;

import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import de.fau.cs.mad.wanthavers.common.Chat;
import de.fau.cs.mad.wanthavers.common.Desire;
import de.fau.cs.mad.wanthavers.common.Media;
import de.fau.cs.mad.wanthavers.common.User;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCaseHandler;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetChatList;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetDesire;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;


public class ChatListPresenter implements ChatListContract.Presenter {


    private final ChatListContract.View mChatListView;

    private boolean mFirstLoad = true;
    private final UseCaseHandler mUseCaseHandler;
    private long mLoggedInUserId;

    private final GetChatList mGetChatList;


    public ChatListPresenter(@NonNull UseCaseHandler useCaseHandler, @NonNull ChatListContract.View chatListView,
                             @NonNull GetChatList getChatList, @NonNull long loggedInUserId){

        mUseCaseHandler = checkNotNull(useCaseHandler, "usecaseHandle cannot be null");
        mChatListView = checkNotNull(chatListView, "chatlist view cannot be null!");
        mGetChatList = checkNotNull(getChatList);
        mLoggedInUserId = loggedInUserId;
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

                        chatList = deleteChatsWithMissingUsers(chatList);

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

            List<ChatItemViewModel> chatModels = new ArrayList<>();

            for(Chat chat : chatList){
                chatModels.add(new ChatItemViewModel(chat, mLoggedInUserId));
            }

            mChatListView.showChats(chatModels);

        }
    }


    private List<Chat> deleteChatsWithMissingUsers(List<Chat> chatList) {

        for(int i = 0; i <chatList.size(); i++) {

            Chat curChat = chatList.get(i);

            if(curChat.getUserObject1() == null || curChat.getUserObject2() == null) {
                chatList.remove(i);
            }
        }

        return chatList;

    }

    @Override
    public void openChatDetails(@NonNull Chat chat) {
        checkNotNull(chat, "Message cannot be null!");
        //TODO implement opening chat details
        mChatListView.showChatDetailsUi(chat);
    }


    @Override
    public void setLoggedInUser(long userId){mLoggedInUserId = userId;}

}
