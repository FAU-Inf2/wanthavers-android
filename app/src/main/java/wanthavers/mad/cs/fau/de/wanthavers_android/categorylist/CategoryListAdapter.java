package wanthavers.mad.cs.fau.de.wanthavers_android.categorylist;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.fau.cs.mad.wanthavers.common.Category;
import de.fau.cs.mad.wanthavers.common.Media;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.CategoryItemBinding;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.CircularImageView;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.RoundedTransformation;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.ViewHolder> {

    private List<Category> mCategoryList;

    private CategoryListContract.Presenter mUserActionsListener;

    public CategoryListAdapter(List<Category> categoryList, CategoryListContract.Presenter actionsListener) {
        setList(categoryList);
        mUserActionsListener = actionsListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CategoryItemBinding mCategoryItemBinding;

        public ViewHolder(CategoryItemBinding categoryItemBinding) {
            super(categoryItemBinding.getRoot());
            mCategoryItemBinding = categoryItemBinding;
            mCategoryItemBinding.executePendingBindings();
        }

        public CategoryItemBinding getCategoryItemBinding() {
            return mCategoryItemBinding;
        }
    }

    @Override
    public CategoryListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        CategoryItemBinding categoryItemBinding = DataBindingUtil
                .inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.category_item, viewGroup, false);
        return new CategoryListAdapter.ViewHolder(categoryItemBinding);
    }

    @Override
    public void onBindViewHolder(CategoryListAdapter.ViewHolder viewHolder, int position) {
        Category category = mCategoryList.get(position);

        CategoryItemBinding categoryItemBinding = viewHolder.getCategoryItemBinding();

        categoryItemBinding.setCategory(category);
        categoryItemBinding.setActionHandler(mUserActionsListener);

        Media media = categoryItemBinding.getCategory().getImage();

        if (media != null) {
            final CircularImageView profileView = categoryItemBinding.imageCategory;
            Picasso.with(categoryItemBinding.getRoot().getContext()).load(media.getLowRes()).into(profileView);
            //Picasso.with(categoryItemBinding.getRoot().getContext()).load(media.getLowRes()).transform(new RoundedTransformation(1000,0)).into(profileView);
        } else{
            //else case is neccessary as the image is otherwise overwritten on scroll
            final ImageView profileView = categoryItemBinding.imageCategory;
            profileView.setImageResource(R.drawable.no_pic);
        }

    }

    public void replaceData(List<Category> categoryList) {
        setList(categoryList);
    }

    private void setList(List<Category> categoryList) {
        mCategoryList = categoryList;
        notifyDataSetChanged();
    }

    public List<Category> getList() {
        return mCategoryList;
    }

    @Override
    public int getItemCount() {
        return mCategoryList != null ? mCategoryList.size() : 0;
    }

    /*public Category getItem(int position) {
        return mCategoryList.get(position);
    }*/

    @Override
    public long getItemId(int position) {
        return position;
    }

}
