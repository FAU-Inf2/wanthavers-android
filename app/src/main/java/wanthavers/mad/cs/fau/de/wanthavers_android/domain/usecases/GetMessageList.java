package wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases;

import de.fau.cs.mad.wanthavers.common.Message;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import android.support.annotation.NonNull;

import java.util.Date;
import java.util.List;

import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.chat.ChatDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.chat.ChatRepository;

import static com.google.common.base.Preconditions.checkNotNull;

public class GetMessageList extends UseCase<GetMessageList.RequestValues, GetMessageList.ResponseValue> {




    private final ChatRepository mChatRepository;


    public GetMessageList(@NonNull ChatRepository chatRepository) {
        mChatRepository = checkNotNull(chatRepository, "chat repository cannot be null!");
    }


    @Override
    protected void executeUseCase(final RequestValues values) {



        mChatRepository.getMessagesForChat(values.getChatId(), values.getLastCreationDate(), values.getLimit(), new ChatDataSource.GetMessagesForChatCallback()
        {

            @Override
            public void onAllMessagesForChat(List<Message> messageList) {
                ResponseValue responseValue = new ResponseValue(messageList);
                getUseCaseCallback().onSuccess(responseValue);
            }

            @Override
            public void onDataNotAvailable() {
                getUseCaseCallback().onError();
            }
        });


    }



    public static final class RequestValues implements UseCase.RequestValues {

        private String  mChatid;
        private Date    mLastCreationDate;
        private int     mLimit;

        public RequestValues(String chatId,Date lastCreationDate, int limit) {
            mChatid = chatId;
            mLastCreationDate = lastCreationDate;
            mLimit = limit;
        }


        public String getChatId() {
            return mChatid;
        }

        public long getLastCreationDate() { return mLastCreationDate.getTime();}

        public int getLimit() {return mLimit;}

    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private List<Message> mMessageList;


        public ResponseValue(@NonNull List<Message> messageList) {
            mMessageList = checkNotNull(messageList, "message List cannot be null!");
        }

        public List<Message> getMessages() {
            return mMessageList;
        }
    }


}
