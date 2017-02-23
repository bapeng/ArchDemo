package com.peng.htmlview;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.util.SparseArrayCompat;
import android.util.ArrayMap;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.util.SparseLongArray;

import java.util.Objects;
import java.util.concurrent.ExecutorService;

/**
 * 简单处理异步任务类
 */
public class SyncUtil {

    public static SyncUtil newSync() {
        return new SyncUtil();
    }

    private ArrayMap<Long, RunUI> arr;

    private SyncUtil() {
        this.arr = new ArrayMap<>();
    }

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1000) {
                Object[] obs = (Object[]) msg.obj;
                Object data = obs[0];
                RunUI result = (RunUI) obs[2];
                result.ui(data);
            } else if (msg.what == 2000) {
                Object[] obs = (Object[]) msg.obj;
                Object data = obs[0];
                Object uiu = obs[1];
                if (uiu != null) {
                    RunUI update = (RunUI) obs[1];
                    update.ui(data);
                }
            }
        }
    };

    public void submit(Runnable runnable) {
        if (runnable == null) return;
        new Thread(runnable).start();
    }

    public void submit(final RunBack task, final RunUI result) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Object data = task.back();
                Object[] obs = new Object[3];
                obs[0] = data;
                obs[1] = null;
                obs[2] = result;

                Message msg = Message.obtain(handler, 1000);
                msg.obj = obs;
                handler.sendMessage(msg);
            }
        }).start();
    }

    public void submit(final RunBack task, final RunUI update, final RunUI result) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                long tid = Thread.currentThread().getId();
                arr.put(tid, update);

                Object data = task.back();
                Object[] obs = new Object[3];
                obs[0] = data;
                obs[1] = update;
                obs[2] = result;

                Message msg = Message.obtain(handler, 1000);
                msg.obj = obs;
                handler.sendMessage(msg);
                arr.remove(tid);
            }
        }).start();
    }

    public void publish(Object data) {
        long tid = Thread.currentThread().getId();
        RunUI uiu = arr.get(tid);

        Object[] obs = new Object[2];
        obs[0] = data;
        obs[1] = uiu;

        Message msg = Message.obtain(handler, 2000);
        msg.obj = obs;
        handler.sendMessage(msg);
    }

    public interface RunBack {
        public Object back();
    }

    public interface RunUI {
        public void ui(Object object);
    }


}
