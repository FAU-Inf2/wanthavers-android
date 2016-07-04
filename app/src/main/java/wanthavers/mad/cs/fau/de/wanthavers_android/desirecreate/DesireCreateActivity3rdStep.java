package wanthavers.mad.cs.fau.de.wanthavers_android.desirecreate;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCaseHandler;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.desire.DesireLocalDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.desire.DesireRemoteDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.desire.DesireRepository;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.media.MediaLocalDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.media.MediaRemoteDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.media.MediaRepository;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.SetDesire;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.SetImage;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.ActivityUtils;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.WantHaversTextView;

import static com.google.common.base.Preconditions.checkNotNull;

public class DesireCreateActivity3rdStep extends AppCompatActivity {
    private DesireCreatePresenter mDesireCreatePresenter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.desirecreate_act);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("");
        ab.setDisplayHomeAsUpEnabled(false);
        ab.setDisplayShowHomeEnabled(false);

        WantHaversTextView abTitle = (WantHaversTextView) findViewById(R.id.toolbar_title);
        abTitle.setText(getString(R.string.createDesire_title_3rd_Step));

        DesireCreateFragment3rdStep desireCreateFragment = (DesireCreateFragment3rdStep) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if (desireCreateFragment == null) {
            desireCreateFragment = DesireCreateFragment3rdStep.newInstance(); //TODO

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    desireCreateFragment, R.id.contentFrame);
        }



        //create desireRepo
        Context context = getApplicationContext();
        checkNotNull(context);


        DesireRepository desRepo = DesireRepository.getInstance(DesireRemoteDataSource.getInstance(context), DesireLocalDataSource.getInstance(context));
        MediaRepository mediaRepo = MediaRepository.getInstance(MediaRemoteDataSource.getInstance(context), MediaLocalDataSource.getInstance(context));

        mDesireCreatePresenter = new DesireCreatePresenter(UseCaseHandler.getInstance(), desireCreateFragment,this,
                null ,new SetDesire(desRepo), new SetImage(mediaRepo), null, null );
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                System.out.println("reached R.id.home");
                onBackPressed();
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //disables Back Button
    }
}
