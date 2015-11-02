package com.haley.may.mayapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Button;

import com.haley.may.mayapp.View.Daily.DailyActivity;

public class TestMainActivity extends AppCompatActivity {

    private View animationView ;
    private Animation animation;
    private int targtetHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_main_test);

        //拍照
        this.findViewById(R.id.buttonPhoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestMainActivity.this, PhotoActivity.class);
                startActivity(intent);
            }
        });

        //每日账单
        Button buttonDaily = (Button)this.findViewById(R.id.buttonDaily);
        buttonDaily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestMainActivity.this, DailyActivity.class);
                startActivity(intent);
            }
        });

        //动画测试
//        animationView = this.findViewById(R.id.button);
//        animationView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED));
//        targtetHeight = 500;//animationView.getMeasuredHeight();
//        Log.i("", "-->>" +targtetHeight);
//        animation = new Animation() {
//            @Override
//            protected void applyTransformation(float interpolatedTime, Transformation t) {
//
//
//
//                animationView.getLayoutParams().height = (int)((float)targtetHeight * interpolatedTime);
//                animationView.requestLayout();
//
//                Log.i("", interpolatedTime+"-->>" +animationView.getLayoutParams().height);
//                super.applyTransformation(interpolatedTime, t);
//            }
//        };
//        animation.setDuration(1000);
//
//        this.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                v.startAnimation(animation);
//            }
//        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
