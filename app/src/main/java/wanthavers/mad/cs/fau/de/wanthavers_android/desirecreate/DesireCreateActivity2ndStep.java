package wanthavers.mad.cs.fau.de.wanthavers_android.desirecreate;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCaseHandler;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.ActivityUtils;

import static com.google.common.base.Preconditions.checkNotNull;

public class DesireCreateActivity2ndStep extends AppCompatActivity {
    private DesireCreatePresenter mDesireCreatePresenter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.desirecreate_act);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.createDesire_title_2nd_Step);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);

        DesireCreateFragment2ndStep desireCreateFragment = (DesireCreateFragment2ndStep) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if (desireCreateFragment == null) {
            desireCreateFragment = DesireCreateFragment2ndStep.newInstance(); //TODO

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    desireCreateFragment, R.id.contentFrame);
        }

        overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right);


        //TODO
        mDesireCreatePresenter = new DesireCreatePresenter(UseCaseHandler.getInstance(), desireCreateFragment, null, this,null, null);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_left);
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
}
