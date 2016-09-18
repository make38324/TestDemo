package com.example.mac.testdemo1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mac.testdemo1.application.HotFixApplication;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import org.kymjs.kjframe.ui.ViewInject;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_hello)
    TextView tvHello;
    @BindView(R.id.TestAll)
    Button btnTestAll;
    private Button btnAndfix;
    private ProgressBar progressBar;

    private void assignViews() {
        btnAndfix = (Button) findViewById(R.id.btn_Andfix);
        progressBar = (ProgressBar) findViewById(R.id.id_progress);
        progressBar.setMax(100);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        assignViews();
        initListener();
    }

    private void initListener() {
        btnAndfix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkHttpUtils.get().url("http://192.168.1.122/aa.apatch").build().execute(new FileCallBack(getFilesDir().getAbsolutePath(), "aa.apatch") {
                    @Override
                    public void inProgress(float v) {
                        progressBar.setProgress((int) (v * 100));
                    }

                    @Override
                    public void onBefore(Request request) {

                    }

                    @Override
                    public void onAfter() {
                        ViewInject.toast(getApplicationContext(), "下载完成");
                        File file = new File(getFilesDir(), "aa.apatch");
                        HotFixApplication.getInstance().addpatch(file.getAbsolutePath());
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        try {
                            Log.d("MainActivity", call.execute().body().string());
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }

                    @Override
                    public void onResponse(File file) {

                    }
                });
            }
        });
    }

    private void toTestAll() {
        Intent intent = new Intent(this, TestallActivity.class);
        startActivity(intent);
    }

    @OnClick({R.id.tv_hello, R.id.TestAll})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_hello:
                break;
            case R.id.TestAll:
                toTestAll();
                break;
        }
    }
}
