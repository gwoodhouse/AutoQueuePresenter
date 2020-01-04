package com.simple.auto_queue_mvp.main;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.simple.auto_queue_mvp.R;

public class MainActivity extends AppCompatActivity implements MainContract.Viewable {

    private Button mButton;
    private TextView mResultTextView;

    private MainContract.Presentable mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mButton = findViewById(R.id.button);
        mResultTextView = findViewById(R.id.resultTextView);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.onButtonClicked();
            }
        });

        if(mPresenter == null) {
            // At this point you could use multiple ways of doing dependency injection (such as Dagger) or adding a
            // layer of abstraction using a construction design pattern. However, In order to not over engineer -
            // simply allowing those patterns using a contract interface allows us to use a concrete instance.
            mPresenter = new MainPresenter(this);
        }
    }

    @Override
    public void showSuccessMessage() {
        mResultTextView.setText(getString(R.string.success));
    }

    @Override
    public void showErrorMessage() {
        mResultTextView.setText(getString(R.string.error));
    }
}
