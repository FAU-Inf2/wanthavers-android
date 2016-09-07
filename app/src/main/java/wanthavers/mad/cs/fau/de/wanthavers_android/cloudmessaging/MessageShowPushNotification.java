package wanthavers.mad.cs.fau.de.wanthavers_android.cloudmessaging;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import java.util.Date;

import de.fau.cs.mad.wanthavers.common.AppChatLastSeen;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.WantHaversApplication;
import wanthavers.mad.cs.fau.de.wanthavers_android.chatdetail.ChatDetailActivity;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.ormlite.AppChatLastSeenDatabaseHelper;
import wanthavers.mad.cs.fau.de.wanthavers_android.desiredetail.DesireDetailActivity;
import wanthavers.mad.cs.fau.de.wanthavers_android.desirelist.DesireListActivity;

public class MessageShowPushNotification extends IntentService {

    public MessageShowPushNotification() {
        super("MessageShowPushNotification");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        handleNotification(intent.getExtras());
    }



    private void handleNotification(Bundle extras){

        Intent intent = new Intent(this, DesireListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        String notificationType = MessageNotificationType.NOT_DEFINED.toString();

        PushMessageNotification pushMessage = extras.getParcelable("WH_PUSH_NOTIFICATION");

        if(pushMessage.mMessageNotificationType.equals(MessageNotificationType.POLLING_MESSAGE.toString())){
            return;
        }

        if(pushMessage.mMessageNotificationType.equals(MessageNotificationType.CHAT_MESSAGE.toString())) {
            //save new ChatInfo to Db
            WantHaversApplication.setNewMessages(true);
            AppChatLastSeen chatLastSeen = new AppChatLastSeen(pushMessage.mChatId, new Date(), pushMessage.message);
            AppChatLastSeenDatabaseHelper appChatLastSeenDatabaseHelper = AppChatLastSeenDatabaseHelper.getInstance(getApplicationContext());
            appChatLastSeenDatabaseHelper.createOrUpdate(chatLastSeen);

            //overwrite default intent with redirect to appropriate chat
            intent = new Intent(this, ChatDetailActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(ChatDetailActivity.EXTRA_CHAT_ID, pushMessage.mChatId);
            notificationType = MessageNotificationType.CHAT_MESSAGE.toString() + "_" + pushMessage.mChatId;
        }

        if(pushMessage.mDesireId != null){
            intent = new Intent(this, DesireDetailActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            long desireId = Long.valueOf(pushMessage.mDesireId);
            intent.putExtra(DesireDetailActivity.EXTRA_DESIRE_ID, desireId);
            notificationType = MessageNotificationType.DESIRE_UPDATE.toString() + "_" + pushMessage.mDesireId;
        }

        //set message title
        String messageTitle = getMessageTitle(pushMessage);

        // notifications with same IDs will overwrite each other
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT);


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(getString(R.string.app_name) + " " + messageTitle)
                .setContentText(pushMessage.message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        if(android.os.Build.VERSION.SDK_INT >= 21) {
            notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo))
                    .setSmallIcon(R.drawable.ic_announcement_white);
        }


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.cancel(notificationType,0);
        notificationManager.notify(notificationType, 0 /* ID of notification */, notificationBuilder.build());
    }

    private String getMessageTitle(PushMessageNotification pushMessage){
        String messageTitle = " ";

        if(pushMessage.mMessageNotificationType.equals(MessageNotificationType.CHAT_MESSAGE.toString())){
            return pushMessage.mSender;
        }

        if(pushMessage.mMessageNotificationType.equals(MessageNotificationType.HAVER_ACCEPTED.toString())){
            return pushMessage.mDesireTitle + " " + getString(R.string.cm_haver_accepted);
        }

        if(pushMessage.mMessageNotificationType.equals(MessageNotificationType.HAVER_REJECTED.toString())){
            return pushMessage.mDesireTitle + " " + getString(R.string.cm_haver_rejected);
        }

        if(pushMessage.mMessageNotificationType.equals(MessageNotificationType.NEW_HAVER.toString())){
            return pushMessage.mDesireTitle;
        }

        if(pushMessage.mMessageNotificationType.equals(MessageNotificationType.DESIRE_UPDATE.toString())){
            return pushMessage.mDesireTitle + " " + getString(R.string.cm_desire_update);
        }

        if(pushMessage.mMessageNotificationType.equals(MessageNotificationType.HAVER_UNACCEPTED.toString())){
            return pushMessage.mDesireTitle + " " + getString(R.string.cm_haver_unaccepted);
        }

        if(pushMessage.mMessageNotificationType.equals(MessageNotificationType.WANTER_UNACCEPTED.toString())){
            return pushMessage.mDesireTitle + " " + getString(R.string.cm_wanter_unaccepted);
        }

        return messageTitle;
    }
}
