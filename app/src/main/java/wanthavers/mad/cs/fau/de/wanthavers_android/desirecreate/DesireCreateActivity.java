package wanthavers.mad.cs.fau.de.wanthavers_android.desirecreate;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCaseHandler;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.ActivityUtils;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.WantHaversTextView;

import static com.google.common.base.Preconditions.checkNotNull;

public class DesireCreateActivity extends AppCompatActivity {
    private DesireCreatePresenter mDesireCreatePresenter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.desirecreate_act);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("");
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);

        WantHaversTextView abTitle = (WantHaversTextView) findViewById(R.id.toolbar_title);
        abTitle.setText(getString(R.string.createDesire_title_1st_Step));

        DesireCreateFragment desireCreateFragment = (DesireCreateFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if (desireCreateFragment == null) {
            desireCreateFragment = DesireCreateFragment.newInstance(); //TODO

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    desireCreateFragment, R.id.contentFrame);
        }

        //create fake task repo
        //Context context = getApplicationContext();
        //checkNotNull(context);

        //DesireRepository fake = DesireRepository.getInstance(DesireRemoteDataSource.getInstance(), DesireLocalDataSource.getInstance(context));

        // Create the presenter
        //TODO
        mDesireCreatePresenter = new DesireCreatePresenter(UseCaseHandler.getInstance(), desireCreateFragment,
                null,null, null, null, null, null);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                System.out.println("reached R.id.home");
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
