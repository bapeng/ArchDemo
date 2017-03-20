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
                startAnim2();
            }
        });

        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        one.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    tv1.setText(getOneValue() + "");
                }
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
                if (fromUser) {
                    tv2.setText(getTwoValue() + "");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    private float getOneValue() {
        return one.getProgress();
    }

    private float getTwoValue() {
        return two.getProgress() / 100f;
    }

    private void startAnim1() {
        SpringSystem system = SpringSystem.create();

        Spring spring = system.createSpring();
        SpringConfig config = SpringConfig.fromBouncinessAndSpeed(25, 20);

        spring.setSpringConfig(config);

        spring.addListener(new SimpleSpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                float value = (float) spring.getCurrentValue();
                float cv = 1f - (value * 0.5f);
                imageView.setScaleX(cv);
                imageView.setScaleY(cv);
            }
        });
        spring.setEndValue(1);
    }

    private void startAnim2() {
        SpringAnimation animx = new SpringAnimation(imageView, SpringAnimation.SCALE_X);
        SpringAnimation animy = new SpringAnimation(imageView, SpringAnimation.SCALE_Y);

        animx.setStartValue(1);
        animy.setStartValue(1);

        SpringForce force = new SpringForce(0.5f);

        force.setStiffness(getOneValue());
        force.setDampingRatio(getTwoValue());

        animx.setSpring(force);
        animy.setSpring(force);

        animx.start();
        animy.start();

    }

}
