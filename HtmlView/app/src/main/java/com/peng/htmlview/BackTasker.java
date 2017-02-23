package com.peng.htmlview;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

//后台线程任务队列
public class BackTasker {

    private HandlerThread thread;
    private Handler handler;

    public BackTasker() {
        thread = new HandlerThread("HandlerThread");
        thread.start();
        handler = new Handler(thread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                BackTasker.this.handleMessage(msg);
            }
        };
    }

    public void handleMessage(Message msg) {

    }

    public boolean isAlive() {
        return thread.isAlive();
    }

    public void quit() {
        thread.quit();
    }

    public void quitSafely() {
        thread.quitSafely();
    }

    public void sendMessage() {
        Message msg = Message.obtain(handler, 0);
        handler.sendMessage(msg);
    }

    public void sendMessage(Message msg) {
        handler.sendMessage(msg);
    }

    public void post(Runnable runnable) {
        handler.post(runnable);
    }

    public void postDelay(Runnable runnable, long delay) {
        handler.postDelayed(runnable, delay);
    }

}
