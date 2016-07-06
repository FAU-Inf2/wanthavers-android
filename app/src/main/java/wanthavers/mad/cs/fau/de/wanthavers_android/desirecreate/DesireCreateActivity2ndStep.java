package wanthavers.mad.cs.fau.de.wanthavers_android.desirecreate;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
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
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.category.CategoryLocalDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.category.CategoryRemoteDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.category.CategoryRepository;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.location.LocationLocalDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.location.LocationRemoteDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.location.LocationRepository;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.SelectImageLogic;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetCategory;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetSubcategories;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.ActivityUtils;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.WantHaversTextView;

import static com.google.common.base.Preconditions.checkNotNull;

public class DesireCreateActivity2ndStep extends AppCompatActivity {
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
        abTitle.setText(getString(R.string.createDesire_title_2nd_Step));

        DesireCreateFragment2ndStep desireCreateFragment = (DesireCreateFragment2ndStep) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if (desireCreateFragment == null) {
            desireCreateFragment = DesireCreateFragment2ndStep.newInstance(); //TODO

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    desireCreateFragment, R.id.contentFrame);
        }

        overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right);



        //create fake repo for categories
        Context context = getApplicationContext();
        checkNotNull(context);

        CategoryRepository categoryRepository = CategoryRepository.getInstance(CategoryRemoteDataSource.getInstance(getApplicationContext()), CategoryLocalDataSource.getInstance(context));
        LocationRepository locationRepository = LocationRepository.getInstance(LocationRemoteDataSource.getInstance(getApplicationContext()), LocationLocalDataSource.getInstance(context));

        mDesireCreatePresenter = new DesireCreatePresenter(UseCaseHandler.getInstance(), desireCreateFragment,
                null, this,null, null, new GetSubcategories(categoryRepository), new GetCategory(categoryRepository));
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
    }
}
