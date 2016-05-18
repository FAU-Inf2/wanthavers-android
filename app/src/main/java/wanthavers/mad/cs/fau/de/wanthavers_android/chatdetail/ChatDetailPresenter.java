package wanthavers.mad.cs.fau.de.wanthavers_android.chatdetail;

import android.support.annotation.NonNull;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.Message;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCaseHandler;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetMessageList;
import static com.google.common.base.Preconditions.checkNotNull;

public class ChatDetailPresenter implements ChatDetailContract.Presenter {


    private final ChatDetailContract.View mMessageListView;
    private final GetMessageList mGetMessageList;
    private boolean mFirstLoad = true;
    private final UseCaseHandler mUseCaseHandler;
    private String mChatId;

    public ChatDetailPresenter(@NonNull UseCaseHandler useCaseHandler,@NonNull String chatId, @NonNull ChatDetailContract.View chatListView,
                             @NonNull GetMessageList getMessageList){

        mUseCaseHandler = checkNotNull(useCaseHandler, "usecaseHandle cannot be null");
        mMessageListView = checkNotNull(chatListView, "desirelist view cannot be null!");
        mGetMessageList = checkNotNull(getMessageList);
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


        GetMessageList.RequestValues requestValue = new GetMessageList.RequestValues(mChatId);    //TODO pass on chat id

        mUseCaseHandler.execute(mGetMessageList, requestValue,
                new UseCase.UseCaseCallback<GetMessageList.ResponseValue>() {
                    @Override
                    public void onSuccess(GetMessageList.ResponseValue response) {
                        List<Message> messageList = response.getMessages();
                        // The view may not be able to handle UI updates anymore
                        if (!mMessageListView.isActive()) {
                            return;
                        }
                        if (showLoadingUI) {
                            mMessageListView.setLoadingIndicator(false);
                        }

                        processMessages(messageList);
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
            mMessageListView.showMessages(messageList);
            // Set the filter label's text.
        }
    }

}
