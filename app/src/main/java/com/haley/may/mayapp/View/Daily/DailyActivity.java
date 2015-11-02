package com.haley.may.mayapp.View.Daily;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.haley.may.mayapp.Model.Daily.DailyModel;
import com.haley.may.mayapp.R;
import com.haley.may.mayapp.View.Base.MayListView;


public class DailyActivity extends AppCompatActivity {

    private DailyModel dailyModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily);

        //初始化Model
        this.dailyModel = new DailyModel();

        //添加控件
        ListView listView = (ListView)this.findViewById(R.id.listView);
        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return dailyModel.getDailyInfoList().size();
            }

            @Override
            public Object getItem(int position) {
                return dailyModel.getDailyInfoList().get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null){

                    convertView = new DailyInfoPanel(DailyActivity.this);
                    ((DailyInfoPanel)convertView).setModel(dailyModel.getDailyInfoList().get(position),(MayListView)parent);
                }
                return convertView;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_daily, menu);
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
