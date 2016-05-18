package wanthavers.mad.cs.fau.de.wanthavers_android.desirecreate;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCaseHandler;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.ActivityUtils;

import static com.google.common.base.Preconditions.checkNotNull;

public class DesireCreateActivity3rdStep extends AppCompatActivity {
    private DesireCreatePresenter mDesireCreatePresenter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.desirecreate_act);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.createDesire_title_3rd_Step);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);

        DesireCreateFragment3rdStep desireCreateFragment = (DesireCreateFragment3rdStep) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if (desireCreateFragment == null) {
            desireCreateFragment = DesireCreateFragment3rdStep.newInstance(); //TODO

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    desireCreateFragment, R.id.contentFrame);
        }


        //TODO
        mDesireCreatePresenter = new DesireCreatePresenter(UseCaseHandler.getInstance(), desireCreateFragment);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
