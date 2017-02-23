package com.peng.htmlview;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;

public class HtmlActivity extends Activity {

    private SyncUtil sync = SyncUtil.newSync();
    private LinearLayout rootView;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html);
        initView();
        loadData();
    }

    private void initView() {
        rootView = (LinearLayout) findViewById(R.id.activity_html);
        textView = (TextView) findViewById(R.id.tv_html);
    }

    private void loadData() {
        StringBuilder builder = new StringBuilder();
        Intent intent = getIntent();
        builder.append(intent.toString()).append("\n\n");
        textView.append(builder.toString());

        Uri uri = intent.getData();
        final String path = uri.getPath();

        sync.submit(new SyncUtil.RunBack() {
            @Override
            public Object back() {
                File file = new File(path);
                try {
                    FileReader fr = new FileReader(file);
                    BufferedReader reader = new BufferedReader(fr);
                    String str;
                    while (true) {
                        str = reader.readLine();
                        if (str == null) {
                            break;
                        } else {
                            sync.publish(str + "\n");
                        }
                    }
                } catch (FileNotFoundException e) {
                    ShowToast("文件不存在");
                } catch (IOException e) {
                }
                return null;
            }
        }, new SyncUtil.RunUI() {
            @Override
            public void ui(Object object) {
                if (object != null) {
                    textView.append(object.toString());
                }
            }
        }, new SyncUtil.RunUI() {
            @Override
            public void ui(Object object) {
                if (object != null) {
                    textView.append(object.toString());
                }
            }
        });
    }

    private String getFileString(String filepath) {
        File file = new File(filepath);
        try {
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);
            StringBuilder builder = new StringBuilder();
            String str;
            while (true) {
                str = reader.readLine();
                if (str == null) {
                    break;
                } else {
                    builder.append(str).append("\n");
                }
            }
            return builder.toString();
        } catch (FileNotFoundException e) {
            ShowToast("文件不存在");
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    private void ShowToast(String msg) {
        Snackbar.make(rootView, msg, Snackbar.LENGTH_LONG).show();
    }

}
