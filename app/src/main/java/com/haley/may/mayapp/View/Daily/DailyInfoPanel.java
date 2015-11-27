package com.haley.may.mayapp.View.Daily;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haley.may.mayapp.Model.Daily.DailyInfo;
import com.haley.may.mayapp.Model.Daily.DailyModel;
import com.haley.may.mayapp.R;
import com.haley.may.mayapp.System.Public;
import com.haley.may.mayapp.Base.MayListView;
import com.haley.may.mayapp.Base.StretchPanel;

import java.text.SimpleDateFormat;


/**
 *
 * Created by haley on 2015/10/9.
 */
public class DailyInfoPanel extends StretchPanel{

    //region variable
    private MayListView parentListView = null;
    private View dialogView = null;
    private Dialog dialog = null;
    private DailyInfo dailyInfo = null;
    private int xDelta,yDelta,xStart,yStart;
    private boolean isLabelMoved =  false;
    //endregion

    //region structure
    public DailyInfoPanel(Context context) {
        super(context, null);

        final View contentView = View.inflate(context, R.layout.layout_dailyinfoitem, null);
        final View stretchView = new RelativeLayout(context);

        stretchView.setBackgroundColor(0xffa0a0a0);
        stretchView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300));

        this.setContentView(contentView);
        this.setStretchView(stretchView);

        contentView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DailyInfoPanel.this.isStretchViewOpened() == true)
                    DailyInfoPanel.this.closeStretchView();
                else
                    DailyInfoPanel.this.openStretchView();
            }
        });

        this.initAddButton();
    }
    //endregion

    //region override
    @Override
    public void openStretchView() {
        super.openStretchView();

        //应该用监听器替换？
//        if (this.isStretchViewOpened() == false)
//            ((Button)this.getContentView().findViewById(R.id.button)).setText("收起");
    }

    @Override
    public void closeStretchView() {
        super.closeStretchView();

        //应该用监听器替换？
//        if (this.isStretchViewOpened() == true)
//            ((Button)this.getContentView().findViewById(R.id.button)).setText("展开");
    }
    //endregion

    //region public function
    public void setModel(DailyInfo model, final MayListView parentView){
        this.parentListView = parentView;
        this.dailyInfo = model;

        ((RelativeLayout)this.stretchView).removeAllViews();
        this.stretchView.invalidate();

        //设置标题
        this.setTitle();

        //添加对应标签内容
        for(DailyInfo.DailyInfoLabel label : this.dailyInfo.getDailyInfoLabelList()){
            this.initLabelToView(label);
        }
    }
    //endregion

    //region private function

    /**
     * 删除弹框调用的删除标签方法
     * @param v
     */
    private void removeLabel(View v){
        ((ViewGroup)getStretchView()).removeView(v);

        this.dailyInfo.removeLabel(((DailyInfoLabelButton) v).getDailyInfoLabel());

        this.setTitle();
    }

    /**
     * 添加弹框确定调用的添加标签方法
     * @param label
     * @param value
     */
    private void addLabel(String label,String value) {
        DailyInfo.DailyInfoLabel temp = this.dailyInfo.addLabel(label, value);
        if (temp == null)
            return;

        this.initLabelToView(temp);

        this.setTitle();
    }

    /**
     * 标签添加的视图修改函数
     * @param label
     */
    private void initLabelToView(DailyInfo.DailyInfoLabel label){
        //当前Button作为标签控件
        DailyInfoLabelButton button = new DailyInfoLabelButton(this.getContext(),label);
        button.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        //设置初始位置，随机
        this.getStretchView().measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        MarginLayoutParams marginLayoutParams = new MarginLayoutParams(button.getLayoutParams());
        marginLayoutParams.setMargins((int) (Math.random() * this.getStretchView().getMeasuredWidth()), (int) (Math.random() * this.getStretchView().getMeasuredHeight()), marginLayoutParams.rightMargin, marginLayoutParams.bottomMargin);
        button.setLayoutParams(new RelativeLayout.LayoutParams(marginLayoutParams));

        //添加Button
        ((RelativeLayout)this.getStretchView()).addView(button);

        //添加特效
        AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
        alphaAnimation.setDuration(1000);
        button.startAnimation(alphaAnimation);


        //添加点击删除功能
        button.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(final View v) {
                if (isLabelMoved == false){
                    Dialog dialog = new AlertDialog.Builder(DailyInfoPanel.this.getContext())
                            .setTitle("删除")
                            .setMessage("确定要删除吗？")
                            .setPositiveButton("确定",new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    removeLabel(v);
                                }
                            })
                            .setNegativeButton("取消",new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).create();
                    dialog.show();
                }
                isLabelMoved = false;
                return true;
            }
        });

        //添加标签移动功能
        button.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int X = (int) event.getRawX();
                final int Y = (int) event.getRawY();
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        Public.setIsChildCapture(true);
                        v.bringToFront();
                        RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
                        xDelta = X - lParams.leftMargin;
                        yDelta = Y - lParams.topMargin;
                        xStart = X;
                        yStart = Y;
                        break;
                    case MotionEvent.ACTION_UP:
                        Public.setIsChildCapture(false);
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        break;
                    case MotionEvent.ACTION_MOVE:

                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
                        layoutParams.leftMargin = X - xDelta;
                        layoutParams.topMargin = Y - yDelta;
                        layoutParams.rightMargin = layoutParams.width;
                        layoutParams.bottomMargin = layoutParams.height;
                        v.setLayoutParams(layoutParams);

                        if(Math.abs(xStart-X) <= 5 && (Math.abs(yStart - Y)) <= 5 )
                            ;
                        else
                            isLabelMoved = true;
                        break;
                }
                v.invalidate();
                return false;
            }
        });
    }

    /**
     * 初始化
     */
    private void initAddButton(){
        //dialog使用的view创建
        dialogView = LayoutInflater.from(DailyInfoPanel.this.getContext()).inflate(R.layout.dialog_dailyadd, null);
        final ListView listView = (ListView) dialogView.findViewById(R.id.listView);
        final EditText label = ((EditText) dialogView.findViewById(R.id.label));
        final EditText value = ((EditText) dialogView.findViewById(R.id.value));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                label.setText(((TextView) view).getText().toString().split(" ")[0]);
                listView.setFocusable(true);
                listView.requestFocus();
            }
        });

        //dialog创建
        dialog = new AlertDialog.Builder(DailyInfoPanel.this.getContext())
                .setTitle("设置标签")
                .setView(dialogView)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addLabel(label.getText().toString(), value.getText().toString());
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();



        this.findViewById(R.id.addButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialog视图的listview的初始化
                listView.setAdapter(new BaseAdapter() {
                    @Override
                    public int getCount() {
                        return DailyModel.getLabelCollection().getLabelInfoList().size();
                    }

                    @Override
                    public Object getItem(int position) {
                        return DailyModel.getLabelCollection().getLabelInfoList().get(position);
                    }

                    @Override
                    public long getItemId(int position) {
                        return position;
                    }

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        //if (convertView == null) {
                        {
                            TextView textView = new TextView(DailyInfoPanel.this.getContext());
                            textView.setText(DailyModel.getLabelCollection().getLabelInfoList().get(position).toString());
                            textView.setTextSize(16);
                            convertView = textView;
                        }
                        return convertView;
                    }
                });

                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

                label.setText("");
                value.setText("");

                label.clearFocus();
                value.clearFocus();
                listView.setFocusable(true);
                listView.requestFocus();

                dialog.show();
            }
        });
    }

    /**
     * 设置标题内容
     */
    private void setTitle(){
        TextView textView = (TextView)this.getContentView().findViewById(R.id.textViewDate);
        String text = this.dailyInfo.getDate();
        try {
            text +="(" + Public.getDayOfWeek(new SimpleDateFormat("yyyy-MM-dd").parse(this.dailyInfo.getDate())) + ")";
        }
        catch (Exception ce){
            Log.i("DailyInfoPanel", "-->>" + ce.toString());
        }
        text += "\n总计:" + this.dailyInfo.getCount();
        textView.setText(text);
    }
    //endregion

    //region class:DailyInfoLabelButton
    public class DailyInfoLabelButton extends Button{
        private DailyInfo.DailyInfoLabel dailyInfoLabel;

        public DailyInfoLabelButton(Context context,DailyInfo.DailyInfoLabel label) {
            super(context);
            this.dailyInfoLabel = label;
            this.setText(this.dailyInfoLabel.toString());
        }

        public DailyInfo.DailyInfoLabel getDailyInfoLabel() {
            return dailyInfoLabel;
        }
    }
    //endregion
}


