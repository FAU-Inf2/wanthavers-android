package wanthavers.mad.cs.fau.de.wanthavers_android.rating;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCaseHandler;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.desire.DesireLocalDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.desire.DesireRemoteDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.desire.DesireRepository;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.haver.HaverLocalDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.haver.HaverRemoteDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.haver.HaverRepository;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.rating.RatingLocalDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.rating.RatingRemoteDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.rating.RatingRepository;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.DesireLogic;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.CreateRating;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetAcceptedHaver;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetDesire;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.ActivityUtils;

import static com.google.common.base.Preconditions.checkNotNull;

public class RatingActivity extends AppCompatActivity {

    public static final String EXTRA_DESIRE_ID = "DESIRE_ID";
    private RatingPresenter mRatingPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rating_act);

        //set up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.desire);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);

        RatingFragment ratingFragment = (RatingFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if (ratingFragment == null) {
            ratingFragment = RatingFragment.newInstance();

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), ratingFragment, R.id.contentFrame);
        }

        DesireLogic desireLogic = new DesireLogic(getApplicationContext());
        ratingFragment.setDesireLogic(desireLogic);

        //create repos
        Context context = getApplicationContext();
        checkNotNull(context);
        long desireId = getIntent().getLongExtra(EXTRA_DESIRE_ID, 4);

        HaverRepository haverRepository = HaverRepository.getInstance(HaverRemoteDataSource.getInstance(getApplicationContext()), HaverLocalDataSource.getInstance(context));
        DesireRepository desireRepository = DesireRepository.getInstance(DesireRemoteDataSource.getInstance(getApplicationContext()), DesireLocalDataSource.getInstance(context));
        RatingRepository ratingRepository = RatingRepository.getInstance(RatingRemoteDataSource.getInstance(getApplicationContext()), RatingLocalDataSource.getInstance(context));

        mRatingPresenter = new RatingPresenter(UseCaseHandler.getInstance(), ratingFragment, new GetDesire(desireRepository),
                new GetAcceptedHaver(haverRepository), new CreateRating(ratingRepository), desireId, desireLogic, this);
    }

}
