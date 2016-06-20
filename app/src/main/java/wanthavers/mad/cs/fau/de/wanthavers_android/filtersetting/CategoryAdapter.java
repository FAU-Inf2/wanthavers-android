package wanthavers.mad.cs.fau.de.wanthavers_android.filtersetting;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

import de.fau.cs.mad.wanthavers.common.Category;
import wanthavers.mad.cs.fau.de.wanthavers_android.databinding.CategoryItemBinding;

public class CategoryAdapter extends ArrayAdapter<Category> {

    private List<Category> mCategoryList;
    private List<Category> mFilteredCategories = new ArrayList<>();
    private FilterSettingActionHandler mFilterSettingActionHandler;

    public CategoryAdapter(Context context, int resource, List<Category> categoryList, FilterSettingActionHandler filterSettingActionHandler) {
        super(context, 0, categoryList);
        setList(categoryList);

        mFilterSettingActionHandler = filterSettingActionHandler;
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
    public int getCount() {
        //return mCategoryList != null ? mCategoryList.size() : 0;
        return mFilteredCategories.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Category getItem(int position) {
        return mFilteredCategories.get(position);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Category category = getItem(position);
        CategoryItemBinding binding;

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            binding = CategoryItemBinding.inflate(inflater, parent, false);
        } else {
            binding = DataBindingUtil.getBinding(view);
        }

        binding.setCategory(category);
        binding.setActionHandler(mFilterSettingActionHandler);

        return binding.getRoot();
    }

    @Override
    public Filter getFilter() {
        return new CustomFilter(this, mCategoryList);
    }

    private class CustomFilter extends Filter {

        CategoryAdapter mAdapter;
        List<Category> mOriginalList;
        List<Category> mFilteredList;

        public CustomFilter(CategoryAdapter adapter, List<Category> originalList) {
            super();
            mAdapter = adapter;
            mOriginalList = originalList;
            mFilteredList = new ArrayList<>();
        }

        @Override
        public String convertResultToString(Object resultValue) {
            return ((Category) resultValue).getName();
        }

        @Override
        public FilterResults performFiltering(CharSequence constraint) {
            mFilteredList.clear();
            final FilterResults results = new FilterResults();

            if (constraint == null || constraint.length() == 0) {
                mFilteredList.addAll(mOriginalList);
            } else {
                for (Category category : mCategoryList) {
                    //"startsWith" or "contains"?
                    if (category.getName().toLowerCase().startsWith(constraint.toString().toLowerCase().trim())) {
                        mFilteredList.add(category);
                    }
                }
            }
            results.values = mFilteredList;
            results.count = mFilteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, Filter.FilterResults results) {
            mAdapter.mFilteredCategories.clear();
            mAdapter.mFilteredCategories.addAll((List) results.values);
            mAdapter.notifyDataSetChanged();
        }
    }
}
