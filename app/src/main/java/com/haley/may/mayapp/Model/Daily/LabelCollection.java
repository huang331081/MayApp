package com.haley.may.mayapp.Model.Daily;

import com.haley.may.mayapp.SQL.SQLiteHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2015/10/15.
 */
public class LabelCollection {

    private int userid;
    private List<LabelInfo> labelInfos = new ArrayList<LabelInfo>();
    private List<PacketInfo> packetInfos = new ArrayList<PacketInfo>();
    private List<LabelInfo> unsignedLables = new ArrayList<LabelInfo>();

    public LabelCollection(int userid){
        this.userid = userid;

        List<String[]> list;

        list = SQLiteHelper.getInstance().getLabel(userid);
        for (String[] strings : list) {
            LabelInfo labelInfo = new LabelInfo(strings[0], Integer.parseInt(strings[1]), Integer.parseInt(strings[2]));
            this.labelInfos.add(labelInfo);
            if (Integer.parseInt(strings[2]) == 0)
                unsignedLables.add(labelInfo);
        }

        list = SQLiteHelper.getInstance().getPacket(userid);
        for (String[] strings : list)
            this.packetInfos.add(new PacketInfo(Integer.parseInt(strings[0]),strings[1],labelInfos));

        SQLiteHelper.getInstance().addPacket(userid,"三餐");
    }

    public List<LabelInfo> getLabelInfoList() {
        return labelInfos;
    }

    public List<LabelInfo> getUnsignedLables() {
        return unsignedLables;
    }

    public List<PacketInfo> getPacketInfos() {
        return packetInfos;
    }

    public void updateLabel(String label,boolean isAdd){
        //存在问题，需要更新unsignedLables，同样导致需要更新界面
        LabelInfo temp = null;
        for (LabelInfo info : this.labelInfos){
            if (info.label.equals(label)){
                temp =info;
            }
        }

        if(temp == null && isAdd == true)
            this.labelInfos.add(new LabelInfo(label,1,0));

        if (temp != null){
            if(temp.count == 1 && isAdd == false){
                this.labelInfos.remove(temp);
            }
            else if (temp.count > 0){
                temp.count += isAdd == true ? 1: -1;
            }
        }
        SQLiteHelper.getInstance().updateLabel(this.userid,label,isAdd);
    }

    private void addLabel(String label,int count,int packetID){
        this.labelInfos.add(new LabelInfo(label,count,packetID));
    }

    public int getUserid() {
        return userid;
    }

    //region class:LabelInfo,PacketInfo
    public class LabelInfo{
        private String label;
        private int count;
        private int packetID;

        public LabelInfo(String label,int count,int packetID){
            this.label = label;
            this.count = count;
            this.packetID = packetID;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getCount() {
            return count;
        }

        @Override
        public String toString() {
            return this.label + " " + this.count;
        }
    }

    public class PacketInfo{
        private int id;
        private String packetName;
        private List<String> labels = new ArrayList<String>();

        public PacketInfo(int id,String name,List<LabelInfo> labelInfos){
            this.id = id;
            this.packetName = name;

            for (LabelInfo info : labelInfos){
                if (info.getLabel().equals(name))
                    labels.add(info.getLabel());
            }
        }

        public void addLabel(String label){
            this.labels.add(label);
        }

        public List<String> getLabels() {
            return labels;
        }
    }
    //endregion
}
