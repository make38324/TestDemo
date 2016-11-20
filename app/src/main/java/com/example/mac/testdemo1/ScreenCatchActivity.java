package com.example.mac.testdemo1;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ScreenCatchActivity extends AppCompatActivity {
    private ImageView iv_screencatch;
    private LinearLayout ll_root;
    private Button btn_save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_catch);
        btn_save= (Button) findViewById(R.id.btn_save);
        iv_screencatch= (ImageView) findViewById(R.id.iv_screencatch);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_root= (LinearLayout) iv_screencatch.getParent();
                ll_root.setDrawingCacheEnabled(true);
                ll_root.buildDrawingCache();
                Bitmap bitmap = ll_root.getDrawingCache();
                iv_screencatch.setImageBitmap(bitmap);
            }
        });
    }
}
