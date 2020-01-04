package com.simple.auto_queue_mvp.main;

import com.simple.auto_queue_mvp.common.BasePresenter;
import com.simple.auto_queue_mvp.main.model.Comment;
import com.simple.auto_queue_mvp.main.service.MainService;

public class MainPresenter extends BasePresenter<MainContract.Viewable> implements MainContract.Presentable {

    MainContract.Servicable mService;

    public MainPresenter(MainContract.Viewable view) {
        super(view);

        // As per MainActivity @ new MainPresenter()
        // This can very easily be changed from a Service to a Storage
        mService = new MainService();
    }

    @Override
    public void onButtonClicked() {
        // Not sure where "Comment" is meant to come from, Assume it doesn't matter.
        Comment toPersistComment = new Comment();
        mService.persistComment(new MainContract.PersistCallback() {
            @Override
            public void onSuccess() {
                getView().showSuccessMessage();
            }

            @Override
            public void onFailure() {
                getView().showErrorMessage();
            }
        }, toPersistComment);
    }
}
