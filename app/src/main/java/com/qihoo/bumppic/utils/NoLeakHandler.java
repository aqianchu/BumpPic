package com.qihoo.bumppic.utils;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;

public abstract class NoLeakHandler<T> extends Handler {

    private static Class<?> sFragmentClass;
    private static Method sGetActivity;
    private final WeakReference<T> mContext;

    static {
        try {
            sFragmentClass = Class.forName("android.support.v4.app.Fragment", false,
                    Thread.currentThread().getContextClassLoader());
            sGetActivity = sFragmentClass.getDeclaredMethod("getActivity", new Class[0]);
        } catch (Exception e) {
            e.printStackTrace();
            sFragmentClass = null;
            sGetActivity = null;
        }
    }

    public NoLeakHandler(T context) {
        this.mContext = new WeakReference(context);
    }

    public NoLeakHandler(Looper looper, T context) {
        super(looper);
        this.mContext = new WeakReference(context);
    }

    public NoLeakHandler(Callback callback, T context) {
        super(callback);
        this.mContext = new WeakReference(context);
    }

    public NoLeakHandler(Looper looper, Callback callback, T context) {
        super(looper, callback);
        this.mContext = new WeakReference(context);
    }


    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        T context = this.mContext.get();
        if (context != null) {
            Activity activity = null;
            if ((sFragmentClass != null) && (sFragmentClass.isInstance(context))) {
                try {
                    activity = (Activity) sGetActivity.invoke(context, new Object[0]);
                } catch (Exception localException) {
                }
            } else if ((context instanceof Activity)) {
                activity = (Activity) context;
            }
            if ((activity == null) || ((activity != null) && (activity.isFinishing()))) {
                return;
            }
            processMessage(context, msg);
        }
    }

    protected abstract void processMessage(T context, Message msg);
}
