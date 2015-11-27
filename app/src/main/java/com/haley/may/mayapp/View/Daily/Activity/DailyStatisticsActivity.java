package com.haley.may.mayapp.View.Daily.Activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.haley.may.mayapp.Base.BaseActivity;
import com.haley.may.mayapp.View.Daily.DailyModelRequestEvent;
import com.haley.may.mayapp.Model.Daily.DailyModel;
import com.haley.may.mayapp.R;
import com.haley.may.mayapp.View.Daily.DailyStatisticsView;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * 为在创建该activity时，能够向该类传递数据类对象
 * 用到了两次eventBus的post功能，且调用函数是不同
 * 第一次：该类在创建时注册自身（注册代码在基类中实现），然后post(new DailyModelRequestEvent())，通知创建该activity的类
 * 第二次：创建该activity的类再post对应数据类
 */
public class DailyStatisticsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_statistics);

        EventBus.getDefault().post(new DailyModelRequestEvent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setTitle("统计");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
            overridePendingTransition(R.anim.anim_activity_exit_stop, R.anim.anim_activity_close_exit);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_activity_exit_stop, R.anim.anim_activity_close_exit);
    }

    @Subscribe
    public void onEventMainThread(DailyModel model){
        ((DailyStatisticsView)findViewById(R.id.dailyStatisticsView)).setmModel(model);
    }
}
