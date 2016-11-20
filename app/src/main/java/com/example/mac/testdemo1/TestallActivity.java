package com.example.mac.testdemo1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.swntek.czm.kzmlib.pojo.ItemData;

import org.kymjs.kjframe.widget.AdapterHolder;
import org.kymjs.kjframe.widget.KJAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TestallActivity extends AppCompatActivity {

    @BindView(R.id.lv)
    ListView lv;
    private List<ItemData> itemDatas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testall);
        ButterKnife.bind(this);
        initData();
        lv.setAdapter(new KJAdapter<ItemData>(lv, itemDatas, R.layout.item_lv) {
            @Override
            public void convert(AdapterHolder helper, final ItemData item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                TextView tv_item = helper.getView(R.id.tv_item);
                tv_item.setText(item.getItemStr());
                tv_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), item.getClazz());
                        startActivity(intent);
                    }
                });
            }
        });
    }

    private void initData() {
        itemDatas.add(new ItemData("圆形调节器", CircleBarActivity.class));
        itemDatas.add(new ItemData("测试截屏",ScreenCatchActivity.class));
        itemDatas.add(new ItemData("Pop动画测试",AnimTestActivity.class));
    }
}
