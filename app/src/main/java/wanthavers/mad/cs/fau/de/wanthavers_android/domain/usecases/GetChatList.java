package wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases;

import android.support.annotation.NonNull;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.Chat;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.chat.ChatDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.chat.ChatRepository;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.desire.DesireRepository;

import static com.google.common.base.Preconditions.checkNotNull;

public class GetChatList extends UseCase<GetChatList.RequestValues, GetChatList.ResponseValue> {

    private final ChatRepository mChatRepository;


    public GetChatList(@NonNull ChatRepository chatRepository) {
        mChatRepository = checkNotNull(chatRepository, "chat repository cannot be null!");
    }


    @Override
    protected void executeUseCase(final RequestValues values) {


        mChatRepository.getAllChatsForLoggedInUser(new ChatDataSource.GetAllChatsForLoggedInUserCallback(){

            @Override
            public void onAllChatsForLoggedInUserLoaded(List<Chat> chatList) {
                ResponseValue responseValue = new ResponseValue(chatList);
                getUseCaseCallback().onSuccess(responseValue);
            }

            @Override
            public void onDataNotAvailable() {
                getUseCaseCallback().onError();
            }
        });


    }



    public static final class RequestValues implements UseCase.RequestValues {

        //private final long mUserId;

        public RequestValues() {
            //mUserId = userId;
        }


    /* TODO add getters here if needed
    public long getDesireId() {
        return mDesireId;
    }
    */
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private List<Chat> mChatList;


        public ResponseValue(@NonNull List<Chat> chatList) {
            mChatList = checkNotNull(chatList, "chatList cannot be null!");
        }

        public List<Chat> getChats() {
            return mChatList;
        }
    }

}

