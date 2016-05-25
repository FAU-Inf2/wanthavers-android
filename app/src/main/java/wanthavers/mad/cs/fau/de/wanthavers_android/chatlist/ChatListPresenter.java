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
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;


public class ChatListPresenter implements ChatListContract.Presenter {


    private final ChatListContract.View mChatListView;

    private boolean mFirstLoad = true;
    private final UseCaseHandler mUseCaseHandler;
    private long mLoggedInUserId;

    private final GetChatList mGetChatList;
    private final GetUser mGetUser;
    private final GetDesire mGetDesire;

    private List<User> mUserList;
    private List<Chat> mChatList;
    private int counter = 0;


    public ChatListPresenter(@NonNull UseCaseHandler useCaseHandler, @NonNull ChatListContract.View chatListView,
                             @NonNull GetChatList getChatList, @NonNull long loggedInUserId,
                             @NonNull GetUser getUser, @NonNull GetDesire getDesire){

        mUseCaseHandler = checkNotNull(useCaseHandler, "usecaseHandle cannot be null");
        mChatListView = checkNotNull(chatListView, "desirelist view cannot be null!");
        mGetChatList = checkNotNull(getChatList);
        mGetUser = checkNotNull(getUser);
        mGetDesire = checkNotNull(getDesire);
        mLoggedInUserId = loggedInUserId;
        mChatListView.setPresenter(this);
        mUserList = new ArrayList<>();
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

    private void loadUsersForChats(final List<Chat> chatList, final List<User> userList){

            Chat chat = chatList.get(counter);

            long otherUserId = getOtherUserId(chat.getUser1(),chat.getUser2());


            GetUser.RequestValues requestValue = new GetUser.RequestValues(otherUserId);

            mUseCaseHandler.execute(mGetUser, requestValue,
                    new UseCase.UseCaseCallback<GetUser.ResponseValue>() {
                        @Override
                        public void onSuccess(GetUser.ResponseValue response) {
                            userList.add(response.getUser());



                            if(counter < chatList.size()-1){
                                counter++;
                                loadUsersForChats(chatList, userList);
                            }else{
                                //loadChatsWithUsers(chatList,userList);
                                List<Desire> desireList = new ArrayList<>();
                                counter = 0;
                                loadDesiresForChats(chatList, userList,desireList);
                            }


                        }

                        @Override
                        public void onError() {
                            mUserList.add(new User("ERROR", "ERRORMAL"));
                        /* TODO think about something useful to do on error
                        // The view may not be able to handle UI updates anymore
                        if (!mChatListView.isActive()) {
                            return;
                        }
                        mChatListView.showLoadingChatsError();
                        */
                        }

                    });


    }

    private void loadDesiresForChats(final List<Chat> chatList, final List<User> userList, final List<Desire> desireList){

        Chat chat = chatList.get(counter);
        long desireId = chat.getDesireId();

        mUseCaseHandler.execute(mGetDesire, new GetDesire.RequestValues(desireId),
                new UseCase.UseCaseCallback<GetDesire.ResponseValue>(){


                    @Override
                    public void onSuccess(GetDesire.ResponseValue response) {

                        desireList.add(response.getDesire());

                        if(counter < chatList.size()-1){
                            counter++;
                            loadDesiresForChats(chatList, userList, desireList);
                        }else{
                            //loadChatsWithUsers(chatList,userList);
                            loadFinishedChats(chatList, userList,desireList);
                        }

                    }

                    @Override
                    public void onError() {
                        //TODO error handling

                    }
                });
    }



    private void processChats(List<Chat> chatList) {

        if (chatList.isEmpty()) {
            // Show a message indicating there are no tasks for that filter type.
            //TODO add what to do if no desires
        } else {
            // Show the list of tasks
            counter = 0;
            List<User> userList = new ArrayList<>(0);
            loadUsersForChats(chatList, userList);
            //loadDesiresForChats(chatList);

            //mChatListView.showChats(chatList);
            // Set the filter label's text.
        }
    }

    private void loadFinishedChats(List<Chat> chatList, List<User> userList, List<Desire> desireList){

        if (chatList.isEmpty() || userList.isEmpty() || desireList.isEmpty()) {

        }else{
            mChatListView.showChats(chatList,userList, desireList);
        }


    }

    @Override
    public void openChatDetails(@NonNull Chat chat) {
        checkNotNull(chat, "Message cannot be null!");
        //TODO implement opening chat details
        mChatListView.showChatDetailsUi(chat);
    }

    private long getOtherUserId(long user1Id, long user2Id){

        if(user1Id == mLoggedInUserId){
            return user2Id;
        }

        return user1Id;
    }

}
