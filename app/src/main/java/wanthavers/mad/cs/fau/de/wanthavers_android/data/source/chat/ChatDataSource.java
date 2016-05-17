package wanthavers.mad.cs.fau.de.wanthavers_android.data.source.chat;

import android.support.annotation.NonNull;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.Chat;
import de.fau.cs.mad.wanthavers.common.Message;

/**
 * Created by Nico on 17.05.2016.
 */
public interface ChatDataSource {

    interface GetAllChatsForLoggedInUserCallback {

        void onAllChatsForLoggedInUserLoaded(List<Chat> chats);

        void onDataNotAvailable();

    }

    interface GetAllMessagesForChatCallback {

        void onAllMessagesForChat(List<Message> messages);

        void onDataNotAvailable();

    }

    interface CreateChatCallback {

        void onChatCreated(Chat chat);

        void onCreateFailed();

    }

    interface CreateMessageCallback {

        void onMessageCreated(Message message);

        void onCreateFailed();

    }

    void getAllChatsForLoggedInUser(@NonNull GetAllChatsForLoggedInUserCallback callback);

    void getAllMessagesForChat(@NonNull String chatId, @NonNull GetAllMessagesForChatCallback callback);

    void createChat(@NonNull Chat chat, @NonNull CreateChatCallback callback);

    void createMessage(@NonNull String chatId, @NonNull Message message, @NonNull CreateMessageCallback callback);

}
