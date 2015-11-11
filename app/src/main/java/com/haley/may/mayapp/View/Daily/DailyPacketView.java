package com.haley.may.mayapp.View.Daily;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.haley.may.mayapp.Model.Daily.DailyModel;
import com.haley.may.mayapp.Model.Daily.LabelCollection;
import com.haley.may.mayapp.R;
import com.haley.may.mayapp.System.Public;
import com.haley.may.mayapp.View.Base.MayListView;
import com.haley.may.mayapp.View.Base.StretchPanel;


/**
 * Created by lenovo on 2015/10/29.
 */
public class DailyPacketView extends RelativeLayout {

    //region variable
    private LabelButton movingView = null;
    private PacketGridView movingPacketView = null;
    private ListView listViewPacket,listViewLabel;
    private BaseAdapter listViewPacketAdapter,listViewLabelAdapter;
    //endregion

    //region structure
    public DailyPacketView(final Context context){
        super(context);
        LayoutInflater.from(context).inflate(R.layout.layout_dialypacket, this);

        this.init();

    }
    //endregion

    //region private function
    private void init(){

        //已分配的类别
        listViewPacket = (ListView)this.findViewById(R.id.listViewPacket);
        listViewPacket.setAdapter(listViewPacketAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return DailyModel.getLabelCollection().getPacketInfos().size()+1;
            }

            @Override
            public Object getItem(int position) {
                Log.i("DailyClassView","getItem-->>1");
                if (position < DailyModel.getLabelCollection().getPacketInfos().size())
                    return DailyModel.getLabelCollection().getPacketInfos().get(position);
                else
                    return null;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                LinearLayout linearLayout = null;
                //if (convertView == null) {
                {
                    linearLayout = new LinearLayout(getContext());
                    linearLayout.setLayoutParams(new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, ListView.LayoutParams.WRAP_CONTENT));
                    convertView = linearLayout;
                    //convertView = new PacketView(getContext(),DailyModel.getLabelCollection().getPacketInfos().get(position));//new PacketGridView(getContext(),DailyModel.getLabelCollection().getPacketInfos().get(position));//
                }

                linearLayout.removeAllViews();

                if (position < DailyModel.getLabelCollection().getPacketInfos().size()) {
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                    Button button = new Button(getContext());
                    button.setText(DailyModel.getLabelCollection().getPacketInfos().get(position).getPacketName());
                    linearLayout.addView(button);
                    linearLayout.addView(new PacketGridView(getContext(),DailyModel.getLabelCollection().getPacketInfos().get(position)));

                    button.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });

                    button.setOnLongClickListener(new OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            Dialog dialog = new AlertDialog.Builder(getContext())
                                    .setTitle("删除")
                                    .setMessage("确定要删除吗？")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            DailyModel.getLabelCollection().deletePacket(DailyModel.getLabelCollection().getPacketInfos().get(position));
                                            listViewPacket.setAdapter(listViewPacketAdapter);

                                            listViewLabel.setAdapter(listViewLabelAdapter);
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
                            return false;
                        }
                    });
                }
                else {
                    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                    final EditText editText = new EditText(getContext());editText.setSingleLine(true); editText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                    Button button = new Button(getContext());
                    button.setText("+");
                    button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 3));
                    linearLayout.addView(editText);
                    linearLayout.addView(button);


                    button.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (DailyModel.getLabelCollection().addPacket(editText.getText().toString()))
                                listViewPacket.setAdapter(listViewPacketAdapter);
                        }
                    });
                }

                return convertView;
            }
        });

        //未分配的label
        listViewLabel = (ListView)this.findViewById(R.id.listViewLabel);
        listViewLabel.setAdapter(listViewLabelAdapter = new BaseAdapter() {
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
            public View getView(int position, View convertView, ViewGroup parent) {
                //if (convertView == null) {
                {
                    LabelButton button = new LabelButton(getContext(), DailyModel.getLabelCollection().getUnsignedLables().get(position).getLabel(), false);
                    convertView = button;
                }
                return convertView;
            }
        });

        listViewLabel.setOnDragListener(new OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                if (event.getAction() == DragEvent.ACTION_DRAG_LOCATION)
                    ;
                else if (event.getAction() == DragEvent.ACTION_DROP) {
                    if (movingPacketView != null) {
                        DailyModel.getLabelCollection().unsignedLabel(movingView.getLabel(), movingPacketView.packetInfo);
                        movingPacketView.setAdapter();
                        listViewLabel.setAdapter(listViewLabelAdapter);
                    }
                }
                return true;
            }
        });
    }
    //endregion

    //region class:PacketGridView,LabelButton
    private class PacketGridView extends GridView{
        private BaseAdapter gridViewAdapter;
        private LabelCollection.PacketInfo packetInfo;

        public PacketGridView(Context context, LabelCollection.PacketInfo packet){
            super(context, null);

            this.packetInfo = packet;

            this.setBackgroundColor(0x00000000);
            //this.setLayoutParams(new ListView.LayoutParams(900, 500));
            this.setNumColumns(3);

            this.setAdapter(gridViewAdapter = new BaseAdapter() {
                @Override
                public int getCount() {
                    return packetInfo.getLabels().size();
                }

                @Override
                public Object getItem(int p) {
                    return packetInfo.getLabels().get(p);
                }

                @Override
                public long getItemId(int p) {
                    return p;
                }

                @Override
                public View getView(int p, View convertView, ViewGroup parent) {
                    //if (convertView == null) {
                    {
                        convertView = new LabelButton(getContext(), packetInfo.getLabels().get(p), true);
                    }
                    return convertView;
                }
            });

            this.setOnDragListener(new OnDragListener() {
                @Override
                public boolean onDrag(View v, DragEvent event) {
                    if (event.getAction() == DragEvent.ACTION_DRAG_LOCATION)
                        ;
                    else if (event.getAction() == DragEvent.ACTION_DROP) {
                        if (movingPacketView == null) {
                            DailyModel.getLabelCollection().signLabel(movingView.getLabel(),null, packetInfo);
                            setAdapter(gridViewAdapter);
                            listViewLabel.setAdapter(listViewLabelAdapter);
                        }
                        else{
                            DailyModel.getLabelCollection().signLabel(movingView.getLabel(),movingPacketView.packetInfo, packetInfo);
                            setAdapter(gridViewAdapter);
                            movingPacketView.setAdapter();
                        }
                    }
                    else if (event.getAction() == DragEvent.ACTION_DRAG_ENDED) {
                        //movingView.setAlpha(1.0f);
                        movingView = null;
                        movingPacketView = null;
                    }
                    return true;
                }
            });
        }

        @Override
        public void setAdapter(ListAdapter adapter) {
            int count = (packetInfo.getLabels().size()-1) / 3+1;
            count = count < 1 ? 1 : count;

            this.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 145 * count));

            super.setAdapter(adapter);
        }

        public void setAdapter(){
            this.setAdapter(this.gridViewAdapter);
        }
    }

    private class LabelButton extends Button{

        private String label = null;
        private boolean isSigned;

        public String getLabel() {
            return label;
        }

        public LabelButton(Context context,String label,final boolean isSigned){
            super(context);

            this.isSigned = isSigned;
            this.label = label;
            this.setText(label);

           this.setOnLongClickListener(new OnLongClickListener() {
               @Override
               public boolean onLongClick(View v) {
                   setAlpha(0.3f);
                   movingView = LabelButton.this;
                   movingPacketView = (PacketGridView)(isSigned ? getParent() : null);

                   startDrag(null, new DragShadowBuilder(LabelButton.this), null, 0);
                   return true;
               }
           });
        }
    }
    //endregion

//    private class PacketView extends StretchPanel{
//        public PacketView(Context context, LabelCollection.PacketInfo packet){
//            super(context);
//
//            this.setLayoutParams(new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT,ListView.LayoutParams.WRAP_CONTENT));
//
//            LinearLayout contentView = new LinearLayout(context);
//            contentView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 200));
//            contentView.setBackgroundColor(0xffdddddd);
//
//            LinearLayout stretchView = new LinearLayout(context);
//            stretchView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//            stretchView.setBackgroundColor(0xffa0a0a0);
//
//            PacketGridView packetGridView = new PacketGridView(context, packet);
//            stretchView.addView(packetGridView);
//
//            this.setContentView(contentView);
//            this.setStretchView(stretchView);
//
//            openStretchView();
//
//            contentView.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (isStretchViewOpened() == true)
//                        closeStretchView();
//                    else
//                        openStretchView();
//                }
//            });
//        }
//    }
}


