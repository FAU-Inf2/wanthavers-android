package wanthavers.mad.cs.fau.de.wanthavers_android.chatdetail;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import de.fau.cs.mad.wanthavers.common.Message;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCaseHandler;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetMessageList;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.SendMessage;

import static com.google.common.base.Preconditions.checkNotNull;

public class ChatDetailPresenter implements ChatDetailContract.Presenter {


    private final ChatDetailContract.View mMessageListView;
    private final GetMessageList mGetMessageList;
    private boolean mFirstLoad = true;
    private final UseCaseHandler mUseCaseHandler;
    private String mChatId;
    private final SendMessage mSendMessage;
    private static final int MESSAGE_LOAD_LIMIT  = 2;
    private List<Message> mMessageList = new ArrayList<>();

    public ChatDetailPresenter(@NonNull UseCaseHandler useCaseHandler,@NonNull String chatId, @NonNull ChatDetailContract.View chatListView,
                             @NonNull GetMessageList getMessageList, @NonNull SendMessage sendMessage){

        mUseCaseHandler = checkNotNull(useCaseHandler, "usecaseHandle cannot be null");
        mMessageListView = checkNotNull(chatListView, "desirelist view cannot be null!");
        mGetMessageList = checkNotNull(getMessageList);
        mSendMessage = checkNotNull(sendMessage);
        mChatId = chatId;

        mMessageListView.setPresenter(this);
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
            mMessageListView.setLoadingIndicator(true);
        }


        Date lastCreatedAt = new Date();

        if(mMessageList.size() > 0) {
            lastCreatedAt = mMessageList.get(mMessageList.size() - 1).getCreatedAt();
        }

        GetMessageList.RequestValues requestValue = new GetMessageList.RequestValues(mChatId, lastCreatedAt ,MESSAGE_LOAD_LIMIT);

        mUseCaseHandler.execute(mGetMessageList, requestValue,
                new UseCase.UseCaseCallback<GetMessageList.ResponseValue>() {
                    @Override
                    public void onSuccess(GetMessageList.ResponseValue response) {
                        List<Message> messageList = response.getMessages();
                        mMessageList.addAll(messageList);
                        // The view may not be able to handle UI updates anymore
                        //if (!mMessageListView.isActive()) {
                        //    return;
                        //}
                        if (showLoadingUI) {
                            mMessageListView.setLoadingIndicator(false);
                        }

                        processMessages(mMessageList);
                    }

                    @Override
                    public void onError() {
                        // The view may not be able to handle UI updates anymore
                        if (!mMessageListView.isActive()) {
                            return;
                        }
                        mMessageListView.showLoadingMessagesError();
                    }
                });

    }


    private void processMessages(List<Message> messageList) {
        if (messageList.isEmpty()) {
            // Show a message indicating there are no tasks for that filter type.
            //TODO add what to do if no desires
        } else {
            // Show the list of tasks

            Collections.reverse(messageList);
            mMessageListView.showMessages(messageList);
            // Set the filter label's text.
        }
    }


    public void sendMessage(Message message){

        SendMessage.RequestValues requestValue = new SendMessage.RequestValues(mChatId, message);

        mUseCaseHandler.execute(mSendMessage, requestValue,
                new UseCase.UseCaseCallback<SendMessage.ResponseValue>() {
                    @Override
                    public void onSuccess(SendMessage.ResponseValue response) {

                        //TODO : JuG and Nico - find useful way to update rest client after user switch
                        //TODO: this has to be done as the server currently sets the auth user as message.getFrom

                        Message message = response.getResponseMessage();


                        mMessageListView.showUpdatedMessageListonSendSuccess(message);
                    }

                    @Override
                    public void onError() {
                        mMessageListView.showSendMessageError();
                    }
                }
        );


    }
}
