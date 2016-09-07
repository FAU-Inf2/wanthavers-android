package wanthavers.mad.cs.fau.de.wanthavers_android.cloudmessaging;

import android.content.Intent;
import android.os.Bundle;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.lang.reflect.Field;
import java.util.Date;
import de.fau.cs.mad.wanthavers.common.AppChatLastSeen;
import de.fau.cs.mad.wanthavers.common.CloudMessageSubject;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.ormlite.AppChatLastSeenDatabaseHelper;

public class MessageListenerService extends FirebaseMessagingService {
    private static final String TAG = "MessageListenerService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        String newMessage = remoteMessage.getData().get(CloudMessageSubject.NEWMESSAGE);

        PushMessageNotification pushMessage = remoteMessage2pushMessage(remoteMessage);


        PushMessageNotification abc = pushMessage;
        System.out.println("*****begin print desireFilter in getDesiresByFilter****");
        for (Field field : abc.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            String name = field.getName();
            Object value = null;
            try {
                value = field.get(abc);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            System.out.printf("Field name: %s, Field value: %s%n", name, value);
        }

        if(pushMessage.mMessageNotificationType.equals(MessageNotificationType.NOT_DEFINED.toString())){
            return;
        }


        //push notification
        Bundle extras = new Bundle();
        extras.putParcelable("WH_PUSH_NOTIFICATION", pushMessage);

        //start broadcast
        Intent intent = new Intent(this, MessagePushIntentService.class);
        intent.putExtras(extras);
        startService(intent);
    }

    private PushMessageNotification remoteMessage2pushMessage(RemoteMessage remoteMessage){

        MessageNotificationType messageType = getMessageType(remoteMessage);
        String message = remoteMessage.getData().get("message");

        PushMessageNotification pushMessage = new PushMessageNotification(messageType.toString(), message);

        if(messageType.equals(MessageNotificationType.CHAT_MESSAGE)){
            pushMessage.mChatId = remoteMessage.getData().get(CloudMessageSubject.NEWMESSAGE_CHATID);
            pushMessage.mSender = remoteMessage.getData().get(CloudMessageSubject.NEWMESSAGE_SENDER);
        } else {
            pushMessage.mDesireId = getDesireId(remoteMessage);
            pushMessage.mDesireTitle = getDesireTitle(remoteMessage);
        }

        return pushMessage;
    }


    private MessageNotificationType getMessageType(RemoteMessage remoteMessage){

        String subject    = remoteMessage.getData().get("subject");

        if(subject.equals(CloudMessageSubject.NEWMESSAGE)){
            return MessageNotificationType.CHAT_MESSAGE;
        } else if (subject.equals(CloudMessageSubject.DESIREUPDATE)){
            return MessageNotificationType.DESIRE_UPDATE;
        } else if (subject.equals(CloudMessageSubject.HAVERACCEPTED)){
            return MessageNotificationType.HAVER_ACCEPTED;
        } else if (subject.equals(CloudMessageSubject.HAVERREJECTED)){
            return MessageNotificationType.HAVER_REJECTED;
        } else if (subject.equals(CloudMessageSubject.NEWHAVER)){
            return MessageNotificationType.NEW_HAVER;
        } else if (subject.equals(CloudMessageSubject.HAVERUNACCEPTED)){
            return MessageNotificationType.HAVER_UNACCEPTED;
        }else if (subject.equals(CloudMessageSubject.WANTERUNACCEPTED)){
            return  MessageNotificationType.WANTER_UNACCEPTED;
        }

        return MessageNotificationType.NOT_DEFINED;
    }


    private String getDesireId(RemoteMessage remoteMessage){

        String subject = remoteMessage.getData().get("subject");

        if(subject.equals(CloudMessageSubject.DESIRECOMPLETE)){
            return remoteMessage.getData().get(CloudMessageSubject.DESIRECOMPLETE_DESIREID);
        } else if (subject.equals(CloudMessageSubject.HAVERACCEPTED)){
            return remoteMessage.getData().get(CloudMessageSubject.HAVERACCEPTED_DESIREID);
        }  else if (subject.equals(CloudMessageSubject.HAVERREJECTED)){
            return remoteMessage.getData().get(CloudMessageSubject.HAVERREJECTED_DESIREID);
        }else if (subject.equals(CloudMessageSubject.NEWHAVER)){
            return remoteMessage.getData().get(CloudMessageSubject.NEWHAVER_DESIREID);
        }else if (subject.equals(CloudMessageSubject.HAVERUNACCEPTED)){
            return remoteMessage.getData().get(CloudMessageSubject.HAVERUNACCEPTED_DESIREID);
        }else if (subject.equals(CloudMessageSubject.WANTERUNACCEPTED)){
            return remoteMessage.getData().get(CloudMessageSubject.WANTERUNACCEPTED_DESIREID);
        }

        return null;
    }

    private String getDesireTitle(RemoteMessage remoteMessage){

        String subject = remoteMessage.getData().get("subject");

        if(subject.equals(CloudMessageSubject.DESIRECOMPLETE)){
            return remoteMessage.getData().get(CloudMessageSubject.DESIRECOMPLETE_DESIRETITLE);
        } else if (subject.equals(CloudMessageSubject.HAVERACCEPTED)){
            return remoteMessage.getData().get(CloudMessageSubject.HAVERACCPETED_DESIRETITLE);
        }  else if (subject.equals(CloudMessageSubject.HAVERREJECTED)){
            return remoteMessage.getData().get(CloudMessageSubject.HAVERREJECTED_DESIRETITLE);
        }else if (subject.equals(CloudMessageSubject.NEWHAVER)){
            return remoteMessage.getData().get(CloudMessageSubject.NEWHAVER_DESIRETITLE);
        }else if (subject.equals(CloudMessageSubject.HAVERUNACCEPTED)){
            return remoteMessage.getData().get(CloudMessageSubject.HAVERUNACCEPTED_DESIRETITLE);
        }else if (subject.equals(CloudMessageSubject.WANTERUNACCEPTED)){
            return remoteMessage.getData().get(CloudMessageSubject.WANTERUNACCEPTED_DESIRETITLE);
        }

        return null;
    }
}
