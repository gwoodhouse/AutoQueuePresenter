package com.simple.auto_queue_mvp.main;

import androidx.lifecycle.LifecycleOwner;
import com.simple.auto_queue_mvp.main.model.Comment;

public class MainContract {

    public interface Viewable extends LifecycleOwner {
        void showSuccessMessage();
        void showErrorMessage();
    }

    public interface Presentable {
        // Method name responds to view action not to business logic
        void onButtonClicked();
    }

    public interface Servicable {
        void persistComment(PersistCallback callback, Comment toPersistComment);
    }

    public interface PersistCallback {
        void onSuccess();
        void onFailure();
    }
}
