package com.haley.may.mayapp.View.Daily;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haley.may.mayapp.Model.Daily.DailyModel;
import com.haley.may.mayapp.Model.Daily.LabelCollection;
import com.haley.may.mayapp.R;
import com.haley.may.mayapp.System.Public;
import com.haley.may.mayapp.View.Base.MayListView;


/**
 * Created by lenovo on 2015/10/29.
 */
public class DailyClassView extends RelativeLayout {

    private View movingView = null;

    public DailyClassView(final Context context){
        super(context);
        LayoutInflater.from(context).inflate(R.layout.layout_dialypacket, this);

        this.init();

//
//        GridView gridView = new GridView(getContext());
//        this.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//        gridView.setLayoutParams(new RelativeLayout.LayoutParams(this.getMeasuredWidth(), LayoutParams.WRAP_CONTENT));
//        gridView.setNumColumns(3);
//        gridView.setBackgroundColor(0xffaaaaaa);
//        gridView.setAdapter(new BaseAdapter() {
//            @Override
//            public int getCount() {
//                return names.size();
//            }
//
//            @Override
//            public Object getItem(int position) {
//                return names.get(position);
//            }
//
//            @Override
//            public long getItemId(int position) {
//                return position;
//            }
//
//            @Override
//            public View getView(int position, View convertView, ViewGroup parent) {
//                if (convertView == null) {
//                    Button button = new Button(getContext());
//                    button.setText(names.get(position));
//                    convertView = button;
//                }
//                return convertView;
//            }
//        });
//        gridView.setScaleX(0.3f);
//        gridView.setScaleY(0.3f);
//        this.addView(gridView);

        //this.initPackets();
        //this.initLabels();
    }

    public void init(){

        ListView listViewPacket = (ListView)this.findViewById(R.id.listViewPacket);
        final BaseAdapter listViewAdapter;
        listViewPacket.setAdapter(listViewAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return DailyModel.getLabelCollection().getPacketInfos().size();
            }

            @Override
            public Object getItem(int position) {
                return DailyModel.getLabelCollection().getPacketInfos().get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    final GridView gridView = new GridView(getContext());
                    final BaseAdapter gridViewAdapter;
                    gridView.setLayoutParams(new ListView.LayoutParams(900, 500));
                    gridView.setNumColumns(3);

                    gridView.setBackgroundColor(0xffaaaaaa);

                    gridView.setAdapter(gridViewAdapter = new BaseAdapter() {
                        @Override
                        public int getCount() {
                            Log.i("", "labels-->>" + DailyModel.getLabelCollection().getPacketInfos().get(position).getLabels().size());
                            return DailyModel.getLabelCollection().getPacketInfos().get(position).getLabels().size();
                        }

                        @Override
                        public Object getItem(int p) {
                            return DailyModel.getLabelCollection().getPacketInfos().get(position).getLabels().get(p);
                        }

                        @Override
                        public long getItemId(int p) {
                            return p;
                        }

                        @Override
                        public View getView(int p, View convertView, ViewGroup parent) {
                            if (convertView == null) {
                                Button button = new Button(getContext());
                                button.setText(DailyModel.getLabelCollection().getPacketInfos().get(position).getLabels().get(p));
                                convertView = button;
                                Log.i("", "labels-->>" + p);
                            }
                            return convertView;
                        }
                    });

                    gridView.setOnDragListener(new OnDragListener() {
                        @Override
                        public boolean onDrag(View v, DragEvent event) {
                            if (event.getAction() == DragEvent.ACTION_DRAG_LOCATION)
                                ;
                            else if (event.getAction() == DragEvent.ACTION_DROP) {
                                DailyModel.getLabelCollection().getPacketInfos().get(position).addLabel(((Button) movingView).getText().toString());
                                gridView.setAdapter(gridViewAdapter);
                            }
                            return true;
                        }
                    });
                    convertView = gridView;
                }
                return convertView;
            }
        });


        ListView listViewLabel = (ListView)this.findViewById(R.id.listViewLabel);
        listViewLabel.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return DailyModel.getLabelCollection().getUnsignedLables().size();
            }

            @Override
            public Object getItem(int position) {
                return DailyModel.getLabelCollection().getUnsignedLables().get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                if (convertView == null){
                    final Button button = new Button(getContext());
                    button.setText(DailyModel.getLabelCollection().getUnsignedLables().get(position).getLabel());
                    convertView = button;

                    button.setOnLongClickListener(new OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            button.setAlpha(0.3f);
                            movingView = button;
                            startDrag(null, new DragShadowBuilder(button), null, 0);

                            return true;
                        }
                    });
                }
                return convertView;
            }
        });
    }

//    private void initPackets(){
//        MayListView listView = new MayListView(this.getContext());
//        listView.setLayoutParams(new RelativeLayout.LayoutParams(300, RelativeLayout.LayoutParams.MATCH_PARENT));
//        listView.setAdapter(new BaseAdapter() {
//            @Override
//            public int getCount() {
//                return names.size();
//            }
//
//            @Override
//            public Object getItem(int position) {
//                return names.get(position);
//            }
//
//            @Override
//            public long getItemId(int position) {
//                return position;
//            }
//
//            @Override
//            public View getView(int position, View convertView, ViewGroup parent) {
//                if (convertView == null) {
//
//
//                    final RelativeLayout relativeLayout = new RelativeLayout(getContext());
//                    TextView textView = new TextView(getContext());
//                    textView.setText(names.get(position));
//
//                    relativeLayout.setBackgroundColor(0xffdddddd);
//                    relativeLayout.setLayoutParams(new ListView.LayoutParams(300, 300));
//
//                    relativeLayout.addView(textView);
//                    convertView = relativeLayout;
//
//
//                    relativeLayout.setOnDragListener(new OnDragListener() {
//                        @Override
//                        public boolean onDrag(final View v, DragEvent event) {
////                            Log.i("","setOnDragListener-->>"+v.toString());
////                            Log.i("setOnDragListener", "-->>" + event.getX() + " " + event.getY());
//                            if (event.getAction() == DragEvent.ACTION_DRAG_LOCATION){
//                                relativeLayout.setBackgroundColor(Color.WHITE);
//                                relativeLayout.invalidate();
//                            }
//                            else if (event.getAction() == DragEvent.ACTION_DRAG_EXITED){
//                                relativeLayout.setBackgroundColor(0xffdddddd);
//                                relativeLayout.invalidate();
//                            }
//                            else if (event.getAction() == DragEvent.ACTION_DROP){
//                                Log.i("", "setOnDragListener-->>" + event.getAction());
//                                Log.i("setOnDragListener", "-->>" + event.getX() + " " + event.getY());
//
//                                RelativeLayout.LayoutParams layout = (RelativeLayout.LayoutParams)movingView.getLayoutParams();
//                                layout.leftMargin = (int)event.getX()-movingView.getWidth()/2;
//                                layout.topMargin = (int)event.getY()-movingView.getHeight()/2;
//                                movingView.setLayoutParams(layout);
//                                relativeLayout.addView(movingView);
//
//                                Animation animation = new Animation() {
//                                    @Override
//                                    protected void applyTransformation(float interpolatedTime, Transformation t) {
//                                        movingView.setScaleX((float) (1.0 - 0.8 * interpolatedTime));
//                                        movingView.setScaleY((float) (1.0 - 0.8 * interpolatedTime));
//                                        super.applyTransformation(interpolatedTime, t);
//                                    }
//                                };
//                                animation.setDuration(1000);
//                                movingView.startAnimation(animation);
//                            }
//                            else if (event.getAction() == DragEvent.ACTION_DRAG_ENDED){
//                                Log.i("setOnDragListener","-->>"+ event.getX() + " "+ event.getY());
////                                MarginLayoutParams marginLayoutParams = new MarginLayoutParams(movingView.getLayoutParams());
////                                marginLayoutParams.setMargins(0, 15, marginLayoutParams.rightMargin, marginLayoutParams.bottomMargin);
////                                movingView.setLayoutParams(new RelativeLayout.LayoutParams(marginLayoutParams));
////                                relativeLayout.addView(movingView);
////
////                                Animation animation = new Animation() {
////                                    @Override
////                                    protected void applyTransformation(float interpolatedTime, Transformation t) {
////                                        movingView.setScaleX((float) (1.0 - 0.8 * interpolatedTime));
////                                        movingView.setScaleY((float) (1.0 - 0.8 * interpolatedTime));
////                                        super.applyTransformation(interpolatedTime, t);
////                                    }
////                                };
////                                animation.setDuration(1000);
////                                movingView.startAnimation(animation);
//
//
//                            }
//                            return true ;
//                        }
//                    });
//                }
//                return convertView;
//            }
//        });
//        this.addView(listView);
//    }
//
//    private void initLabels(){
//        //设置初始位置，随机
//        final Button button  = new Button(getContext());
//        button.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//        button.setText("one");
//        this.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//        MarginLayoutParams marginLayoutParams = new MarginLayoutParams(button.getLayoutParams());
//        marginLayoutParams.setMargins((int) (Math.random() * (this.getMeasuredWidth() - 350) + 300), (int) (Math.random() * (this.getMeasuredHeight() - 350) + 300), marginLayoutParams.rightMargin, marginLayoutParams.bottomMargin);
//        button.setLayoutParams(new RelativeLayout.LayoutParams(marginLayoutParams));
//
//        this.addView(button);
//
//        button.setOnLongClickListener(new OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                movingView = button;
//                Log.i("OnLongClickListener", "-->>" + xDelta + " " + yDelta);
//                startDrag(null, new DragShadowBuilder(button), null, 0);
//                removeView(button);
//                return true;
//            }
//        });
//
//        //添加标签移动功能
////        button.setOnTouchListener(new View.OnTouchListener() {
////            @Override
////            public boolean onTouch(View v, MotionEvent event) {
////                final int X = (int) event.getRawX();
////                final int Y = (int) event.getRawY();
////                switch (event.getAction() & MotionEvent.ACTION_MASK) {
////                    case MotionEvent.ACTION_DOWN:
////                        Public.setIsChildCapture(true);
////                        v.bringToFront();
////                        RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
////                        xDelta = X - lParams.leftMargin;
////                        yDelta = Y - lParams.topMargin;
////                        xStart = X;
////                        yStart = Y;
////                        break;
////                    case MotionEvent.ACTION_UP:
////                        Public.setIsChildCapture(false);
////                        break;
////                    case MotionEvent.ACTION_POINTER_DOWN:
////                        break;
////                    case MotionEvent.ACTION_POINTER_UP:
////                        break;
////                    case MotionEvent.ACTION_MOVE:
////
////                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
////                        layoutParams.leftMargin = X - xDelta;
////                        layoutParams.topMargin = Y - yDelta;
////                        layoutParams.rightMargin = layoutParams.width;
////                        layoutParams.bottomMargin = layoutParams.height;
////                        v.setLayoutParams(layoutParams);
////                        break;
////                }
////                v.invalidate();
////                return false;
////            }
////        });
//    }
}
