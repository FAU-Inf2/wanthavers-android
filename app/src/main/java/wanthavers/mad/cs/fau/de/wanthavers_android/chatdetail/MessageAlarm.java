package wanthavers.mad.cs.fau.de.wanthavers_android.chatdetail;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.widget.Toast;

public class MessageAlarm extends BroadcastReceiver {


    private static MessageAlarm mMessageAlarm;
    private final long POLLING_INTERVAL = 1000;

    public static MessageAlarm getInstance(){
        if(mMessageAlarm == null){
           mMessageAlarm = new MessageAlarm();
        }

        return mMessageAlarm;
    }




    /*   think about whether singletion good idea hrere
    public MessageAlarm getInstance(ChatDetailContract.Presenter chatPresenter){

        if(mMessageAlarm == null){
            mMessageAlarm = new MessageAlarm();
        }

        return mMessageAlarm;
    }
*/

    @Override
    public void onReceive(Context context, Intent intent)
    {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        //PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");

        //wl.acquire();
        // Put here YOUR code.

        //Toast.makeText(context, "Alarm !!!!!!!!!!", Toast.LENGTH_LONG).show(); // For example

        //start polling service
        Intent serviceIntent = new Intent(context, MessageService.class);
        serviceIntent.putExtras(intent);
        context.startService(serviceIntent);

        //wl.release();
    }

    public void setAlarm(Context context)
    {
        AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, MessageAlarm.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), POLLING_INTERVAL, pi); // Millisec * Second * Minute
    }

    public void cancelAlarm(Context context)
    {
        Intent intent = new Intent(context, MessageAlarm.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
}
