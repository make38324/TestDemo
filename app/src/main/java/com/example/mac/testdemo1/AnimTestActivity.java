package com.example.mac.testdemo1;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AnimTestActivity extends Activity {

    @BindView(R.id.tv_showpop)
    TextView tvShowpop;
    @BindView(R.id.iv_test)
    ImageView iv_test;
    private PopupWindow popupWindow;
    private View populView;
    private MyPop myPop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anim_test);
        ButterKnife.bind(this);
        initPop();
    }

    private void initPop() {
        populView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.popul_layout, null);
        popupWindow = new PopupWindow(populView, getResources().getDimensionPixelSize(R.dimen.a60d), getResources().getDimensionPixelSize(R.dimen.a90d));
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
        myPop = new MyPop();
        myPop.init();
        myPop.llClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }
        });
    }

    @OnClick({R.id.tv_showpop, R.id.iv_test})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_showpop:
                popupWindow.showAsDropDown(tvShowpop);
                rotateyAnimRun(myPop.llAddcursor, myPop.llTietu, myPop.llClose);
                backgroundAlpha(0.5f);
                break;
            case R.id.iv_test:
                verticalRun(view);
                break;
        }
    }

    private void startIvAnim(final View view) {//自定义动画属性同时执行多个动画
        ObjectAnimator anim = ObjectAnimator//
                .ofFloat(view, "zhy", 1.0F, 0.0F,1.0f)//
                .setDuration(500);//
        anim.start();
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                float cVal = (Float) animation.getAnimatedValue();
                view.setAlpha(cVal);
                view.setScaleX(cVal);
                view.setScaleY(cVal);
            }
        });
    }
    public void propertyValuesHolder(View view)
    {
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("alpha", 1f, 0f, 1f);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleX", 1f, 0, 1f);
        PropertyValuesHolder pvhZ = PropertyValuesHolder.ofFloat("scaleY", 1f, 0, 1f);
        ObjectAnimator.ofPropertyValuesHolder(view, pvhX, pvhY,pvhZ).setDuration(1000).start();
    }
    public void verticalRun(final View view)
    {
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1000
                - view.getHeight());
//        animator.setTarget(view);
        animator.setDuration(1000).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view.setTranslationY((Float) animation.getAnimatedValue());
            }
        });
    }
    class MyPop {
        @BindView(R.id.ll_tietu)
        LinearLayout llTietu;
        @BindView(R.id.ll_addcursor)
        LinearLayout llAddcursor;
        @BindView(R.id.ll_close)
        LinearLayout llClose;

        public void init() {
            ButterKnife.bind(this, populView);
        }
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    public void rotateyAnimRun(View view, View view2, View view3) {
        view.setPivotX(view.getLeft());
        view.setPivotX(view.getTop());
        float h1 = -view.getHeight();
        float h2 = h1 - view2.getHeight();
        float h3 = h2 - view3.getHeight();
        view.setTranslationY(h1);
        view2.setTranslationY(h2);
        view3.setTranslationY(h3);
        ObjectAnimator moveIn = ObjectAnimator.ofFloat(view, "translationY", h1, 0f);
        ObjectAnimator moveIn2 = ObjectAnimator.ofFloat(view2, "translationY", h2, 0f);
        ObjectAnimator moveIn3 = ObjectAnimator.ofFloat(view3, "translationY", h3, 0f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(moveIn3).after(moveIn2)
                 .after(moveIn);//先执行moveIn动画之后同时执行旋转和透明度动画
        animSet.setDuration(500);
        animSet.start();
    }
}
