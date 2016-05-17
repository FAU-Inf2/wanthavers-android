package wanthavers.mad.cs.fau.de.wanthavers_android.rest;

import android.content.Context;

import org.glassfish.jersey.client.proxy.WebResourceFactory;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.Chat;
import de.fau.cs.mad.wanthavers.common.Message;
import de.fau.cs.mad.wanthavers.common.rest.api.ChatResource;

/**
 * Created by Nico on 16.05.2016.
 */
public class ChatClient extends RestClient {
    private static ChatClient INSTANCE;

    private ChatResource chatEndpoint;

    private ChatClient(Context context) {
        super(context);
        chatEndpoint = WebResourceFactory.newResource(ChatResource.class, target);
    }

    public List<Chat> getAllChatsForLoggedInUser() {
        return chatEndpoint.get(null);
    }

    public List<Message> getAllMessagesForChat(String chatId) {
        return chatEndpoint.getMessages(chatId, null);
    }

    public Chat createChat(Chat chat) {
        return chatEndpoint.createChat(chat, null);
    }

    public Message createMessage(String chatId, Message message) {
        return chatEndpoint.createMessage(chatId, message, null);
    }
}
