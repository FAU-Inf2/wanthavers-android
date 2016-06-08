package wanthavers.mad.cs.fau.de.wanthavers_android.chatdetail;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class MessageService extends Service {

        private static ChatDetailContract.Presenter mPresenter;
        MessageAlarm alarm = new MessageAlarm();
        private final IBinder mbinder = new MessageBinder();
        private static boolean mActive = true;

        public void onCreate()
        {
            super.onCreate();
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId)
        {

            if(mActive) {
                alarm.setAlarm(this);
                mPresenter.loadMessages(true);
                return START_STICKY;
            }

            return START_NOT_STICKY;
        }

        @Override
        public void onStart(Intent intent, int startId)
        {

            alarm.setAlarm(this);
            mPresenter.loadMessages(true);
        }


        @Override
        public IBinder onBind(Intent intent)
        {
            return mbinder;
        }

        public class MessageBinder extends Binder {
            MessageService getService(){
                return MessageService.this;
            }
        }

        public static void setPresenter(ChatDetailContract.Presenter presenter){
            mPresenter = presenter;
        }

        public static void setActive(boolean active){
            mActive = active;

        }
}
