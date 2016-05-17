package wanthavers.mad.cs.fau.de.wanthavers_android.data.source.local;

import android.content.Context;
import android.support.annotation.NonNull;

import de.fau.cs.mad.wanthavers.common.Chat;
import de.fau.cs.mad.wanthavers.common.Message;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.ChatDataSource;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Nico on 17.05.2016.
 */
public class ChatLocalDataSource implements ChatDataSource {

    private static ChatLocalDataSource INSTANCE;

    private Context context;

    private ChatLocalDataSource(@NonNull Context context) {
        checkNotNull(context);
        this.context = context;
    }

    public static ChatLocalDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new ChatLocalDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void getAllChatsForLoggedInUser(@NonNull GetAllChatsForLoggedInUserCallback callback) {
        //TODO: alter this method when we decide to store chats locally
        callback.onDataNotAvailable();
    }

    @Override
    public void getAllMessagesForChat(@NonNull String chatId, @NonNull GetAllMessagesForChatCallback callback) {
        //TODO: alter this method when we decide to store chats locally
        callback.onDataNotAvailable();
    }

    @Override
    public void createChat(@NonNull Chat chat, @NonNull CreateChatCallback callback) {
        //TODO: alter this method when we decide to store chats locally
        callback.onCreateFailed();
    }

    @Override
    public void createMessage(@NonNull String chatId, @NonNull Message message, @NonNull CreateMessageCallback callback) {
        //TODO: alter this method when we decide to store chats locally
        callback.onCreateFailed();
    }
}
