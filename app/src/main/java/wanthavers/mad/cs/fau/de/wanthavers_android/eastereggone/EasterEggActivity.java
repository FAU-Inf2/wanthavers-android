package wanthavers.mad.cs.fau.de.wanthavers_android.eastereggone;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ViewAnimator;

import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCaseHandler;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.media.MediaLocalDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.media.MediaRemoteDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.media.MediaRepository;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.user.UserLocalDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.user.UserRemoteDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.user.UserRepository;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetUser;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.SetImage;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.UpdateUser;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.ActivityUtils;
import wanthavers.mad.cs.fau.de.wanthavers_android.welcome.WelcomeFragment;

public class EasterEggActivity extends AppCompatActivity implements View.OnTouchListener {
    private EasterEggPresenter mEasterEggPresenter;

    private float mDx;
    private float mDy;
    private float mDcenterY;
    private boolean mMoved;
    private ImageView mHappySmiley;
    private ImageView mSadSmiley;
    private ImageView mLogo;
    private Handler mTimeoutHanlder;
    private boolean isMovable = true;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.easteregg_act);

        //set up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("");
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);


        EasterEggFragment easterEggFragment = (EasterEggFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if (easterEggFragment == null) {
            easterEggFragment = EasterEggFragment.newInstance();

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    easterEggFragment, R.id.contentFrame);
        }

        Context context = getApplicationContext();

        MediaRepository mediaRepo = MediaRepository.getInstance(MediaRemoteDataSource.getInstance(context), MediaLocalDataSource.getInstance(context));
        SetImage setImage = new SetImage(mediaRepo);
        UserRepository userRepository = UserRepository.getInstance(UserRemoteDataSource.getInstance(context), UserLocalDataSource.getInstance(context));
        UpdateUser updateUser = new UpdateUser(userRepository);
        GetUser getUser = new GetUser(userRepository);

        mEasterEggPresenter = new EasterEggPresenter(UseCaseHandler.getInstance(), easterEggFragment, getApplicationContext(), this,setImage, updateUser,getUser);

        mTimeoutHanlder = new Handler();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mLogo = (ImageView) findViewById(R.id.easter_egg_wh_logo);
        mHappySmiley = (ImageView) findViewById(R.id.easter_egg_happy_smiley);
        mSadSmiley = (ImageView) findViewById(R.id.easter_egg_sad_smiley);
        mLogo.setOnTouchListener(this);
        mMoved = false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onTouch(View view, MotionEvent event) {


        if(!isMovable){
            return true;
        }

        ViewAnimator myParent = (ViewAnimator) view.getParent();

        if(mMoved == false){
            mMoved = true;
            mDcenterY = view.getY();
        }

        int[] coordsParent = {0,0};
        myParent.getLocationOnScreen(coordsParent);
        int parentAbsoluteTop = coordsParent[1];
        int parentAbsoluteBottom = coordsParent[1] + myParent.getHeight();

        int[] coords = {0,0};
        view.getLocationOnScreen(coords);
        int absoluteTop = coords[1];
        int absoluteBottom = coords[1] + view.getHeight();


        if(absoluteTop <= parentAbsoluteTop){
            isMovable = false;
            view.setVisibility(View.GONE);
            view.setY(mDcenterY);
            mSadSmiley.setVisibility(View.VISIBLE);

            mTimeoutHanlder.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finishSmiley(mSadSmiley); }
            }, 2000);

            return true;
        }

        if(absoluteBottom >= parentAbsoluteBottom){
            isMovable = false;
            view.setVisibility(View.GONE);
            view.setY(mDcenterY);
            mHappySmiley.setVisibility(View.VISIBLE);


            mTimeoutHanlder.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finishSmiley(mHappySmiley); }
            }, 2000);

            return true;
        }

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:

                mDx = view.getX();//view.getX() - event.getRawX();
                mDy = view.getY() - event.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:

                view.animate()
                        .y(event.getRawY() + mDy)
                        .setDuration(0)
                        .start();
                break;
            default:
                return false;
        }
        return true;
    }


    private void finishSmiley(ImageView smiley){
        smiley.setVisibility(View.GONE);
        mLogo.setY(mDcenterY);
        mLogo.setVisibility(View.VISIBLE);
        isMovable = true;
    }
}
