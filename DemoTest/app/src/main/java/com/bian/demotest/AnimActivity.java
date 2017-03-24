package com.bian.demotest;

import android.os.Bundle;
import android.support.animation.DynamicAnimation;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;
import com.facebook.rebound.SpringUtil;

public class AnimActivity extends AppCompatActivity {

    ImageView imageView;
    SeekBar one;
    SeekBar two;

    Button btn_start;
    Button btn_stop;

    TextView tv1;
    TextView tv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anim);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imageView = (ImageView) findViewById(R.id.image_view);
        one = (SeekBar) findViewById(R.id.seekbar_one);
        two = (SeekBar) findViewById(R.id.seekbar_two);
        btn_start = (Button) findViewById(R.id.btn_start);
        btn_stop = (Button) findViewById(R.id.btn_stop);

        tv1 = (TextView) findViewById(R.id.tv_one);
        tv2 = (TextView) findViewById(R.id.tv_two);

        initView();
    }

    private void initView() {
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnim1();
            }
        });

        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopAnim();
            }
        });

        one.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv1.setText("刚性："+getOneValue() + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        two.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv2.setText("阻尼："+getTwoValue() + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        one.setProgress(70);
        two.setProgress(15);
    }

    private float getOneValue() {
        return one.getProgress();
    }

    private float getTwoValue() {
        return two.getProgress() / 10f;
    }

    SpringSystem system = SpringSystem.create();
    Spring spring;

    private void startAnim1() {
        if(spring != null){
            spring.destroy();
        }
        spring = system.createSpring();

        SpringConfig config = new SpringConfig(getOneValue(), getTwoValue());
        spring.setSpringConfig(config);
        spring.setCurrentValue(0.5f, true);

        spring.addListener(new SimpleSpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                float value = (float) spring.getCurrentValue();
                float cv = value;
                imageView.setScaleX(cv);
                imageView.setScaleY(cv);
            }
        });
        spring.setEndValue(1);
    }

    SpringAnimation animx;
    SpringAnimation animy;

    private void startAnim2() {
        if (animx != null) {
            animx.cancel();
        }
        if (animy != null) {
            animy.cancel();
        }

        animx = new SpringAnimation(imageView, SpringAnimation.SCALE_X);
        animy = new SpringAnimation(imageView, SpringAnimation.SCALE_Y);

        animx.setStartValue(0.5f);
        animy.setStartValue(0.5f);
        SpringForce force = new SpringForce(1f);

        force.setStiffness(getOneValue());
        force.setDampingRatio(getTwoValue()/10f);

        animx.setSpring(force);
        animy.setSpring(force);

        animx.start();
        animy.start();
    }

    private void stopAnim() {
        if (animx != null) {
            animx.cancel();
        }
        if (animy != null) {
            animy.cancel();
        }
        if(spring != null){
            spring.destroy();
        }
    }

}
