package wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases;

import android.content.res.Resources;
import android.support.annotation.NonNull;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.Chat;
import de.fau.cs.mad.wanthavers.common.Message;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.chat.ChatDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.chat.ChatRepository;

import static com.google.common.base.Preconditions.checkNotNull;

public class SendMessage extends UseCase<SendMessage.RequestValues, SendMessage.ResponseValue> {

    private final ChatRepository mChatRepository;


    public SendMessage(@NonNull ChatRepository chatRepository) {
        mChatRepository = checkNotNull(chatRepository, "chat repository cannot be null!");
    }


    @Override
    protected void executeUseCase(final RequestValues values) {


        mChatRepository.createMessage(values.getChatId(), values.getMessage(),new ChatDataSource.CreateMessageCallback(){

            @Override
            public void onMessageCreated(Message message) {
                ResponseValue responseValue = new ResponseValue(message);
                getUseCaseCallback().onSuccess(responseValue);
            }

            @Override
            public void onCreateFailed() {
                getUseCaseCallback().onError();
            }

        });


    }



    public static final class RequestValues implements UseCase.RequestValues {

        private final Message mMessage;
        private final String mChatId;

        public RequestValues(String chatId, Message message) {
            mMessage = message;
            mChatId = chatId;
        }


        public Message getMessage(){
            return mMessage;
        }

        public String getChatId(){
            return mChatId;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private Message mMessage;


        public ResponseValue(@NonNull Message message) {
            mMessage = checkNotNull(message, "chatList cannot be null!");
        }

        public Message getResponseMessage() {
            return mMessage;
        }
    }

}

