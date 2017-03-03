package com.peng.htmlview;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.ArrayMap;

/**
 * 简单处理异步任务类
 */
public class SyncUtil {

    private static int UPDATE = 500;
    private static int RESULT = 501;

    public static SyncUtil newSync() {
        return new SyncUtil();
    }

    private ArrayMap<Long, Argument> arguArray;

    private SyncUtil() {
        this.arguArray = new ArrayMap<>();
    }

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == UPDATE) {
                Argument obj = (Argument) msg.obj;
                if (obj.update != null) {
                    obj.update.ui(obj.data);
                }
            }
            if (msg.what == RESULT) {
                Argument obj = (Argument) msg.obj;
                if (obj.result != null) {
                    obj.result.ui(obj.data);
                }
            }
        }
    };

    public void runUI(Runnable runnable) {
        if (runnable == null) return;
        handler.post(runnable);
    }

    public void runUI(Runnable runnable, long delay) {
        if (runnable == null) return;
        handler.postDelayed(runnable, delay);
    }

    public void submit(Runnable runnable) {
        if (runnable == null) return;
        new Thread(runnable).start();
    }

    public void submit(final RunBack task, final RunUI result) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                long tid = Thread.currentThread().getId();
                Argument as = new Argument();
                as.update = null;
                as.result = result;
                arguArray.put(tid, as);
                task.back();
                arguArray.remove(tid);
            }
        }).start();
    }

    public void submit(final RunBack task, final RunUI update, final RunUI result) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                long tid = Thread.currentThread().getId();
                Argument as = new Argument();
                as.update = update;
                as.result = result;
                arguArray.put(tid, as);
                task.back();
                arguArray.remove(tid);
            }
        }).start();
    }

    //发送更新数据到UI线程
    public void sendUpdate(Object data) {
        long tid = Thread.currentThread().getId();
        Argument argument = arguArray.get(tid);

        Argument obj = new Argument();
        obj.data = data;
        obj.update = argument.update;

        Message msg = Message.obtain(handler, UPDATE);
        msg.obj = obj;
        handler.sendMessage(msg);
    }

    //发送最后的结果到UI线程
    public void sendResult(Object data) {
        long tid = Thread.currentThread().getId();
        Argument argument = arguArray.get(tid);

        Argument obj = new Argument();
        obj.data = data;
        obj.result = argument.result;

        Message msg = Message.obtain(handler, RESULT);
        msg.obj = obj;
        handler.sendMessage(msg);
    }

    private class Argument {
        Object data;
        RunUI update;
        RunUI result;
    }

    public interface RunBack {
        void back();
    }

    public interface RunUI {
        void ui(Object object);
    }


}
