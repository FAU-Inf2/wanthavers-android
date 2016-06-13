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
    public void getMessagesForChat(@NonNull final String chatId, final Long lastCreationTime, final Integer limit, @NonNull final GetMessagesForChatCallback callback) {
        checkNotNull(chatId);
        checkNotNull(callback);

        chatLocalDataSource.getMessagesForChat(chatId, lastCreationTime, limit, new GetMessagesForChatCallback() {
            @Override
            public void onAllMessagesForChat(List<Message> messages) {
                callback.onAllMessagesForChat(messages);
            }

            @Override
            public void onDataNotAvailable() {
                chatRemoteDataSource.getMessagesForChat(chatId, lastCreationTime, limit, new GetMessagesForChatCallback() {
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
    public void createChat(@NonNull Chat chat, @NonNull final CreateChatCallback callback) {
        checkNotNull(chat);
        checkNotNull(callback);

        chatRemoteDataSource.createChat(chat, new CreateChatCallback() {
            @Override
            public void onChatCreated(Chat chat) {
                callback.onChatCreated(chat);
            }

            @Override
            public void onCreateFailed() {
                callback.onCreateFailed();
            }
        });
    }

    @Override
    public void createMessage(@NonNull String chatId, @NonNull Message message, @NonNull final CreateMessageCallback callback) {
        checkNotNull(chatId);
        checkNotNull(message);
        checkNotNull(callback);

        chatRemoteDataSource.createMessage(chatId, message, new CreateMessageCallback() {
            @Override
            public void onMessageCreated(Message message) {
                callback.onMessageCreated(message);
            }

            @Override
            public void onCreateFailed() {
                callback.onCreateFailed();
            }
        });
    }
}
