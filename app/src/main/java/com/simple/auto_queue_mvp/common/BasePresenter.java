package com.simple.auto_queue_mvp.common;

import android.util.Log;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;

/**
 * Assuming that all Views are LifecycleOwners (If not Activity/Fragment they can be made LifecycleOwners) presenters
 * can intelligently respect the lifecycle state of the view using this base class.
 *
 * When a decedent calls getView() they will either get the View, or a proxy which records method invocations as the
 * view. When the view resumes then these events will be replayed in the order they were recorded. This solves a
 * number of issues for "infinite spinner" and the like.
 *
 * The limitation this places on the system is the the View cannot return
 * @param <View>
 */
public class BasePresenter<View extends LifecycleOwner> {

    private static class MethodInvocation {
        Method method;
        Object[] args;

        public MethodInvocation(Method method, Object[] args) {
            this.method = method;
            this.args = args;
        }
    }

    private View mView;
    private View mProxy;

    private View mActive;

    final private ArrayList<MethodInvocation> mQueuedInvocations = new ArrayList<>();

    @SuppressWarnings("unchecked") // (View) proxyInstance
    public BasePresenter(View view) {
        mView = view;

        Object proxyInstance =
                Proxy.newProxyInstance(
                        mView.getClass().getClassLoader(),
                        new Class[]{mView.getClass().getInterfaces()[0]},
                        new InvocationHandler() {

                            @Override
                            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                                synchronized (mQueuedInvocations) {
                                    if(method.getReturnType().isInstance(Void.class)) {
                                        mQueuedInvocations.add(new MethodInvocation(method, args));
                                    } else {
                                        Log.e(BasePresenter.this.getClass().getName(), "Used non void method in View");
                                    }
                                }
                                return null;
                            }
                        });

        // Suppress warning for generics cast
        mProxy = (View) proxyInstance;

        // Bind getView() to always return mView or a proxy depending on the Views lifecycle state.
        mView.getLifecycle().addObserver(new LifecycleObserver() {
            @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
            private void onResume() {
                synchronized (mQueuedInvocations) {
                    for(MethodInvocation methodInvocation : mQueuedInvocations) {
                        try {
                            methodInvocation.method.invoke(mView, methodInvocation.args);
                        } catch (IllegalAccessException e) {
                            Log.e(BasePresenter.this.getClass().getName(),
                                    "Couldn't invoke " + methodInvocation.method.getName());
                        } catch (InvocationTargetException e) {
                            Log.e(BasePresenter.this.getClass().getName(),
                                    "Couldn't invoke " + methodInvocation.method.getName());
                        }
                    }

                    mActive = mView;

                    BasePresenter.this.onResume();
                }
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
            private void onPause() {
                mActive = mProxy;

                BasePresenter.this.onPause();
            }
        });
    }

    protected View getView() {
        return mActive;
    }

    protected void onResume() {
        // Intentionally blank. Most but not all Presenters will override this method - but they need to do so after
        // mActive has been updated
    }

    protected void onPause() {
        // Intentionally blank. Most but not all Presenters will override this method - but they need to do so after
        // mActive has been updated
    }
}
