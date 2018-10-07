package com.riffpoint.maptask.activities;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.riffpoint.maptask.Maptask;
import com.riffpoint.maptask.di.components.ActivityComponent;
import com.riffpoint.maptask.di.components.DaggerActivityComponent;
import com.riffpoint.maptask.di.module.ActivityModule;
import com.riffpoint.maptask.utils.CommonUtils;
import com.riffpoint.maptask.viewmodels.BaseViewModel;

/**
 * Created by Fedchuk Maxim on 2018-10-06.
 * Copyright (c) 2018 Fedchuk Maxim All rights reserved.
 */
public abstract class BaseActivity<T extends ViewDataBinding, V extends BaseViewModel> extends AppCompatActivity {

    private ActivityComponent mActivityComponent;
    private ProgressDialog mProgressDialog;

    private T mViewDataBinding;
    private V mViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeActivityComponent();
        performDependencyInjection();
        performDataBinding();
        //allow portrait mode only
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    private void initializeActivityComponent() {
        mActivityComponent = DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(((Maptask) getApplication()).getComponent())
                .build();
    }

    private void performDataBinding() {
        mViewDataBinding = DataBindingUtil.setContentView(this, getLayoutId());
        this.mViewModel = mViewModel == null ? getViewModel() : mViewModel;
        mViewDataBinding.setVariable(getBindingVariable(), mViewModel);
        mViewDataBinding.executePendingBindings();
    }

    public ActivityComponent getActivityComponent() {
        return mActivityComponent;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public T getViewDataBinding() {
        return mViewDataBinding;
    }

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    public abstract V getViewModel();

    /**
     * Override for set binding variable
     *
     * @return variable id
     */
    public abstract
    @IdRes
    int getBindingVariable();

    /**
     * @return layout resource id
     */
    public abstract
    @LayoutRes
    int getLayoutId();

    public abstract void performDependencyInjection();

    public void showLoading() {
        hideLoading();
        mProgressDialog = CommonUtils.showLoadingDialog(this);
    }

    public void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }
}

