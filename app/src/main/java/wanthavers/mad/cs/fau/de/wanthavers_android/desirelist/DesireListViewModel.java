/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package wanthavers.mad.cs.fau.de.wanthavers_android.desirelist;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import wanthavers.mad.cs.fau.de.wanthavers_android.BR;
import wanthavers.mad.cs.fau.de.wanthavers_android.R;


public class DesireListViewModel extends BaseObservable {

    int mDesireListSize = 0;

    private final DesireListContract.Presenter mPresenter;

    private Context mContext;

    public DesireListViewModel(Context context, DesireListContract.Presenter presenter) {
        mContext = context;
        mPresenter = presenter;

    }

    @Bindable
    public boolean isNotEmpty() {
        return mDesireListSize > 0;
    }

    public void setDesireListSize(int desireListSize) {
        mDesireListSize = desireListSize;
        notifyPropertyChanged(BR.notEmpty);

    }

    /* TODO check if we need any of the filters
    @Bindable
    public String getCurrentFilteringLabel() {
        switch (mPresenter.getFiltering()) {
            case ALL_TASKS:
                return mContext.getResources().getString(R.string.label_all);
            case ACTIVE_TASKS:
                return mContext.getResources().getString(R.string.label_active);
            case COMPLETED_TASKS:
                return mContext.getResources().getString(R.string.label_completed);
        }
        return null;
    }

    @Bindable
    public String getNoTasksLabel() {
        switch (mPresenter.getFiltering()) {
            case ALL_TASKS:
                return mContext.getResources().getString(R.string.no_tasks_all);
            case ACTIVE_TASKS:
                return mContext.getResources().getString(R.string.no_tasks_active);
            case COMPLETED_TASKS:
                return mContext.getResources().getString(R.string.no_tasks_completed);
        }
        return null;
    }

    @Bindable
    public Drawable getNoTaskIconRes() {
        switch (mPresenter.getFiltering()) {
            case ALL_TASKS:
                return mContext.getResources().getDrawable(R.drawable.ic_assignment_turned_in_24dp);
            case ACTIVE_TASKS:
                return mContext.getResources().getDrawable(R.drawable.ic_check_circle_24dp);
            case COMPLETED_TASKS:
                return mContext.getResources().getDrawable(R.drawable.ic_verified_user_24dp);
        }
        return null;
    }

    @Bindable
    public boolean getTasksAddViewVisible() {
        return mPresenter.getFiltering() == ALL_TASKS;
    }
    */

}
