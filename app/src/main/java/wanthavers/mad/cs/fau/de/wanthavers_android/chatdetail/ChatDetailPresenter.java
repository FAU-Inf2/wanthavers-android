package wanthavers.mad.cs.fau.de.wanthavers_android.chatdetail;

import android.support.annotation.NonNull;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCaseHandler;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetMessageList;
import static com.google.common.base.Preconditions.checkNotNull;

public class ChatDetailPresenter implements ChatDetailContract.Presenter {


    private final ChatDetailContract.View mMessageListView;
    private final GetMessageList mGetMessageList;
    private boolean mFirstLoad = true;
    private final UseCaseHandler mUseCaseHandler;
    static final int MAX_CHAT_MESSAGES_TO_SHOW = 50;

    public ChatDetailPresenter(@NonNull UseCaseHandler useCaseHandler, @NonNull ChatDetailContract.View chatListView,
                             @NonNull GetMessageList getMessageList){

        mUseCaseHandler = checkNotNull(useCaseHandler, "usecaseHandle cannot be null");
        mMessageListView = checkNotNull(chatListView, "desirelist view cannot be null!");
        mGetMessageList = checkNotNull(getMessageList);

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

        // Construct query to execute
        ParseQuery<Message> query = ParseQuery.getQuery(Message.class);
        // Configure limit and sort order
        query.setLimit(MAX_CHAT_MESSAGES_TO_SHOW);
        query.orderByAscending("createdAt");
        // Execute query to fetch all messages from Parse asynchronously
        // This is equivalent to a SELECT query with SQL
        query.findInBackground(new FindCallback<Message>() {
            public void done(List<Message> messageList, ParseException e) {
                if (e == null) {
                    processMessages(messageList);
                } else {
                    if (!mMessageListView.isActive()) {
                        return;
                    }
                    mMessageListView.showLoadingChatsError();
                }
            }

        });



        /*
        GetDesireList.RequestValues requestValue = new GetDesireList.RequestValues();

        mUseCaseHandler.execute(mGetDesireList, requestValue,
                new UseCase.UseCaseCallback<GetDesireList.ResponseValue>() {
                    @Override
                    public void onSuccess(GetDesireList.ResponseValue response) {
                        List<Desire> desires = response.getDesires();
                        // The view may not be able to handle UI updates anymore
                        if (!mMessageListView.isActive()) {
                            return;
                        }
                        if (showLoadingUI) {
                            mMessageListView.setLoadingIndicator(false);
                        }

                        processDesires(desires);
                    }

                    @Override
                    public void onError() {
                        // The view may not be able to handle UI updates anymore
                        if (!mMessageListView.isActive()) {
                            return;
                        }
                        mMessageListView.showLoadingDesiresError();
                    }
                });
                */
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
