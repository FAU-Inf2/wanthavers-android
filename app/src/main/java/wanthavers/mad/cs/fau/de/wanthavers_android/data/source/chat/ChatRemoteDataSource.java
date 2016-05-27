package wanthavers.mad.cs.fau.de.wanthavers_android.data.source.chat;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.Chat;
import de.fau.cs.mad.wanthavers.common.Message;
import wanthavers.mad.cs.fau.de.wanthavers_android.rest.ChatClient;

/**
 * Created by Nico on 17.05.2016.
 */
public class ChatRemoteDataSource implements ChatDataSource {

    private static ChatRemoteDataSource INSTANCE;

    private ChatClient chatClient;

    private ChatRemoteDataSource(Context context) {
        chatClient = ChatClient.getInstance(context);
    }

    public static ChatRemoteDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new ChatRemoteDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void getAllChatsForLoggedInUser(@NonNull GetAllChatsForLoggedInUserCallback callback) {
        try {
            List<Chat> chats = chatClient.getAllChatsForLoggedInUser();
            callback.onAllChatsForLoggedInUserLoaded(chats);
        } catch (Throwable t) {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void getAllMessagesForChat(@NonNull String chatId, @NonNull GetAllMessagesForChatCallback callback) {
        try {
            List<Message> messages = chatClient.getAllMessagesForChat(chatId);
            callback.onAllMessagesForChat(messages);
        } catch (Throwable t) {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void createChat(@NonNull Chat chat, @NonNull CreateChatCallback callback) {
        try {
            Chat ret = chatClient.createChat(chat);
            callback.onChatCreated(ret);
        } catch (Throwable t) {
            callback.onCreateFailed();
        }
    }

    @Override
    public void createMessage(@NonNull String chatId, @NonNull Message message, @NonNull CreateMessageCallback callback) {
        try {
            Message ret = chatClient.createMessage(chatId, message);
            callback.onMessageCreated(ret);
        } catch (Throwable t) {
            callback.onCreateFailed();
        }
    }
}
