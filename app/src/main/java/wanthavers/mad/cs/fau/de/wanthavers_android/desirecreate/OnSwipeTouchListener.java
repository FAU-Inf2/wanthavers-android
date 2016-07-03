package wanthavers.mad.cs.fau.de.wanthavers_android.desirecreate;

import android.app.Activity;
import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.desiredetail.DesireDetailContract;
import wanthavers.mad.cs.fau.de.wanthavers_android.locationlist.LocationListActionHandler;
import wanthavers.mad.cs.fau.de.wanthavers_android.locationlist.LocationListContract;

public class OnSwipeTouchListener implements OnTouchListener {

    private final GestureDetector gestureDetector;
    private final DesireCreateContract.Presenter mPresenter;
    private final DesireDetailContract.Presenter mPresenterDetail;
    private final Activity mContext;
    private final DesireCreateActionHandler mActionHandler;
    private final LocationListContract.Presenter  mPresenterLocation;
    private final LocationListActionHandler mActionHandlerLocation;

    public OnSwipeTouchListener (Context context, DesireCreateContract.Presenter presenter , DesireCreateActionHandler actionHandler){
        gestureDetector = new GestureDetector(context, new GestureListener());
        mPresenter = presenter;
        mPresenterDetail = null;
        mContext = (Activity) context;
        mActionHandler = actionHandler;
        mPresenterLocation=null;
        mActionHandlerLocation = null;
    }

    public OnSwipeTouchListener (Context context, DesireDetailContract.Presenter presenter ){
        gestureDetector = new GestureDetector(context, new GestureListener());
        mPresenterDetail = presenter;
        mPresenter = null;
        mContext = (Activity) context;
        mActionHandler = null;
        mPresenterLocation = null;
        mActionHandlerLocation = null;
    }

    public OnSwipeTouchListener (Context context, LocationListContract.Presenter presenter ,
                                 LocationListActionHandler ac){
        gestureDetector = new GestureDetector(context, new GestureListener());
        mPresenterLocation = presenter;
        mPresenter = null;
        mPresenterDetail = null;
        mContext = (Activity) context;
        mActionHandler = null;
        mActionHandlerLocation = ac;
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private final class GestureListener extends SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                    }
                    result = true;
                }
                else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeBottom();
                    } else {
                        onSwipeTop();
                    }
                }
                result = true;

            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }

    public void onSwipeRight() {
        mContext.onBackPressed();
        if(mPresenter == null){
            mContext.finish();
            mContext.overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_left);
        }
    }

    public void onSwipeLeft() {
        if (mPresenterLocation !=null){
            mActionHandlerLocation.buttonAddLocation();
        }
        if (mActionHandler!= null) {
            mActionHandler.buttonNextDesireStep();
        }
    }

    public void onSwipeTop() {
        //do nothing
    }

    public void onSwipeBottom() {
        //do nothing
    }
}