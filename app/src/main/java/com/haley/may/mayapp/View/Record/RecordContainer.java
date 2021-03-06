package com.haley.may.mayapp.View.Record;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.haley.may.mayapp.Model.Record.RecordModel;
import com.haley.may.mayapp.R;
import com.haley.may.mayapp.Base.BaseContainer;

import java.util.Calendar;

/**
 * Created by haley on 2015/10/28.
 */
public class RecordContainer extends BaseContainer {

    private RecordModel model = null;
    private boolean isSelectDate = false;

    public RecordContainer(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.layout_record, this);

        this.model = new RecordModel();

        final DatePicker datePicker = (DatePicker)this.findViewById(R.id.datePicker);
        datePicker.init(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                ((TextView) findViewById(R.id.textViewDate)).setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            }
        });

        ((TextView)findViewById(R.id.textViewDate)).setText(Calendar.getInstance().get(Calendar.YEAR) + "-" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "-" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        this.findViewById(R.id.textViewDate).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = new Animation() {
                    @Override
                    protected void applyTransformation(float interpolatedTime, Transformation t) {
                        LayoutParams params = (LayoutParams) findViewById(R.id.datePicker).getLayoutParams();

                        if (isSelectDate == true)
                            params.height = (int)(getHeight()/2 * interpolatedTime);
                        else
                            params.height = (int)(getHeight()/2 * (1-interpolatedTime));

                        findViewById(R.id.datePicker).setLayoutParams(params);
                        super.applyTransformation(interpolatedTime, t);
                    }
                };
                animation.setDuration(500);

                findViewById(R.id.datePicker).startAnimation(animation);

                isSelectDate = !isSelectDate;
            }
        });


        this.findViewById(R.id.buttonAdd).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new AlertDialog.Builder(getContext())
                        .setTitle("添加")
                        .setMessage("确定要添加吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                model.addRecord(((TextView) findViewById(R.id.textViewDate)).getText().toString()+" 00:00:00",((EditText) findViewById(R.id.editTextTitle)).getText().toString(),"");
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();

                dialog.show();
            }
        });

        final ListView listView = (ListView)this.findViewById(R.id.listView);
        final BaseAdapter adapter;
        listView.setAdapter(adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return model.getRecordInfos().size();
            }

            @Override
            public Object getItem(int position) {
                return model.getRecordInfos().get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null){
                    TextView textView = new TextView(getContext());
                    textView.setText(model.getRecordInfos().get(position).toString());
                    convertView = textView;
                }
                return convertView;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Dialog dialog = new AlertDialog.Builder(getContext())
                        .setTitle("删除")
                        .setMessage("确定要删除吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                model.deleteRecord(position);
                                listView.setAdapter(adapter);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();

                dialog.show();
            }
        });
    }
}
