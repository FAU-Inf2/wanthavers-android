package wanthavers.mad.cs.fau.de.wanthavers_android.categorylist;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCaseHandler;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.category.CategoryLocalDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.category.CategoryRemoteDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.category.CategoryRepository;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetSubcategories;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.ActivityUtils;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.WantHaversTextView;

import static com.google.common.base.Preconditions.checkNotNull;

public class CategoryListActivity extends AppCompatActivity {

    private CategoryListPresenter mCategoryListPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categorylist_act);

        //set up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");

        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        WantHaversTextView abTitle = (WantHaversTextView) findViewById(R.id.toolbar_title);
        abTitle.setText(getString(R.string.categorylist_title));


        CategoryListFragment categoryListFragment = (CategoryListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if (categoryListFragment == null) {
            categoryListFragment = CategoryListFragment.newInstance();

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), categoryListFragment, R.id.contentFrame);
        }

        //create repos
        Context context = getApplicationContext();
        checkNotNull(context);

        CategoryRepository categoryRepository = CategoryRepository.getInstance(CategoryRemoteDataSource.getInstance(getApplicationContext()), CategoryLocalDataSource.getInstance(context));

        mCategoryListPresenter = new CategoryListPresenter(UseCaseHandler.getInstance(), categoryListFragment,
                new GetSubcategories(categoryRepository));

        categoryListFragment.setPresenter(mCategoryListPresenter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
