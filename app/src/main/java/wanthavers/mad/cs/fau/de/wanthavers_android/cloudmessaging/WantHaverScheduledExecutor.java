package wanthavers.mad.cs.fau.de.wanthavers_android.cloudmessaging;

import java.util.concurrent.ScheduledThreadPoolExecutor;

public class WantHaverScheduledExecutor extends ScheduledThreadPoolExecutor {


    public WantHaverScheduledExecutor(int corePoolSize) {
        super(corePoolSize);
    }

}
