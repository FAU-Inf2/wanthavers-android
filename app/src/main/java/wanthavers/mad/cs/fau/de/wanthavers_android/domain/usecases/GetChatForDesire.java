package wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutCompat;

import de.fau.cs.mad.wanthavers.common.Chat;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCase;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.desire.DesireDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.desire.DesireRepository;

import static com.google.common.base.Preconditions.checkNotNull;

public class GetChatForDesire extends UseCase<GetChatForDesire.RequestValues, GetChatForDesire.ResponseValue> {

    private final DesireRepository mDesireRepository;

    public GetChatForDesire(@NonNull DesireRepository desireRepository) {
        mDesireRepository = checkNotNull(desireRepository);
    }

    @Override
    protected void executeUseCase(final RequestValues values) {

        mDesireRepository.getChatForDesire(values.mUser2Id, values.getDesireId(),
                new DesireDataSource.GetChatForDesireCallback() {
                    @Override
                    public void onChatLoaded(Chat chat) {
                        ResponseValue responseValue = new ResponseValue(chat);
                        getUseCaseCallback().onSuccess(responseValue);
                    }

                    @Override
                    public void onLoadFailed() {
                        getUseCaseCallback().onError();
                    }
                }
        );
    }

    public static final class RequestValues implements UseCase.RequestValues {

        private final long mUser2Id;
        private final long mDesireId;

        public RequestValues(long user2Id, long desireId) {
            mUser2Id = user2Id;
            mDesireId = desireId;
        }

        public long getUser2Id() {
            return mUser2Id;
        }

        public long getDesireId() {
            return mDesireId;
        }

    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private Chat mChat;

        public ResponseValue(Chat chat) {
            mChat = chat;
        }

        public Chat getChat() {
            return mChat;
        }

    }

}
