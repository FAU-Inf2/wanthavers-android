package wanthavers.mad.cs.fau.de.wanthavers_android.data.source.chat;

import android.support.annotation.NonNull;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.Chat;
import de.fau.cs.mad.wanthavers.common.Message;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Nico on 17.05.2016.
 */
public class ChatRepository implements ChatDataSource {

    private static ChatRepository INSTANCE = null;

    private final ChatDataSource chatRemoteDataSource;

    private final ChatDataSource chatLocalDataSource;

    private ChatRepository(@NonNull ChatDataSource chatRemoteDataSource, @NonNull ChatDataSource chatLocalDataSource) {
        this.chatRemoteDataSource = checkNotNull(chatRemoteDataSource);
        this.chatLocalDataSource = checkNotNull(chatLocalDataSource);
    }

    public static ChatRepository getInstance(ChatDataSource chatRemoteDataSource, ChatDataSource chatLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new ChatRepository(chatRemoteDataSource, chatLocalDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void getAllChatsForLoggedInUser(@NonNull final GetAllChatsForLoggedInUserCallback callback) {
        checkNotNull(callback);

        chatLocalDataSource.getAllChatsForLoggedInUser(new GetAllChatsForLoggedInUserCallback() {
            @Override
            public void onAllChatsForLoggedInUserLoaded(List<Chat> chats) {
                callback.onAllChatsForLoggedInUserLoaded(chats);
            }

            @Override
            public void onDataNotAvailable() {
                chatRemoteDataSource.getAllChatsForLoggedInUser(new GetAllChatsForLoggedInUserCallback() {
                    @Override
                    public void onAllChatsForLoggedInUserLoaded(List<Chat> chats) {
                        callback.onAllChatsForLoggedInUserLoaded(chats);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
            }
        });
    }

    @Override
    public void getAllMessagesForChat(@NonNull final String chatId, @NonNull final GetAllMessagesForChatCallback callback) {
        checkNotNull(chatId);
        checkNotNull(callback);

        chatLocalDataSource.getAllMessagesForChat(chatId, new GetAllMessagesForChatCallback() {
            @Override
            public void onAllMessagesForChat(List<Message> messages) {
                callback.onAllMessagesForChat(messages);
            }

            @Override
            public void onDataNotAvailable() {
                chatRemoteDataSource.getAllMessagesForChat(chatId, new GetAllMessagesForChatCallback() {
                    @Override
                    public void onAllMessagesForChat(List<Message> messages) {
                        callback.onAllMessagesForChat(messages);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
            }
        });
    }

    @Override
    public void createChat(@NonNull Chat chat, @NonNull CreateChatCallback callback) {
        checkNotNull(chat);
        checkNotNull(callback);

        chatLocalDataSource.createChat(chat, callback);
        chatRemoteDataSource.createChat(chat, callback);
    }

    @Override
    public void createMessage(@NonNull String chatId, @NonNull Message message, @NonNull CreateMessageCallback callback) {
        checkNotNull(chatId);
        checkNotNull(message);
        checkNotNull(callback);

        chatLocalDataSource.createMessage(chatId, message, callback);
        chatRemoteDataSource.createMessage(chatId, message, callback);
    }
}
