package com.haley.may.mayapp.Model.Daily;

import android.util.Log;

import com.haley.may.mayapp.SQL.SQLiteHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2015/10/15.
 */
public class LabelCollection {

    //region variable
    private int userid;
    private List<LabelInfo> labelInfos = new ArrayList<LabelInfo>();
    private List<PacketInfo> packetInfos = new ArrayList<PacketInfo>();
    private List<LabelInfo> unsignedLables = new ArrayList<LabelInfo>();
    //endregion

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
        for (String[] strings : list) {
            PacketInfo packetInfo = new PacketInfo(Integer.parseInt(strings[0]), strings[1], labelInfos);
            this.packetInfos.add(packetInfo);

            for (LabelInfo info : this.labelInfos){
                if (info.packetID == packetInfo.id)
                    packetInfo.addLabel(info.label);
            }
        }



        //this.signLabel("晚饭", this.packetInfos.get(0));
        //this.signLabel("中饭",this.packetInfos.get(0));
        //this.signLabel("早饭",this.packetInfos.get(0));
        //this.signLabel("充话费",this.packetInfos.get(0));
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

    public boolean addPacket(String label){
        if (label.equals(""))
            return false;

        for (PacketInfo packetInfo : this.packetInfos){
            if (packetInfo.packetName.equals(label))
                return false;
        }

        this.packetInfos.add(new PacketInfo(SQLiteHelper.getInstance().addPacket(userid, label), label, this.labelInfos));
        return true;
    }

    public void deletePacket(PacketInfo packetInfo){
        this.packetInfos.remove(packetInfo);
        for (LabelInfo labelInfo : this.labelInfos){
            if (labelInfo.packetID == packetInfo.id){
                labelInfo.packetID = 0;
                this.unsignedLables.add(labelInfo);
            }
        }

        SQLiteHelper.getInstance().deletePacket(userid,packetInfo.id);
    }

    public void signLabel(String label,PacketInfo beforePacketInfo,PacketInfo packetInfo){
        if (beforePacketInfo != null)
            beforePacketInfo.labels.remove(label);

        for (LabelInfo info : this.labelInfos){
            if (label.equals(info.getLabel())){
                packetInfo.addLabel(label);
                info.packetID = packetInfo.id;
                if (this.unsignedLables.contains(info)) {
                    this.unsignedLables.remove(info);
                }

                SQLiteHelper.getInstance().updatePacketOfLabel(this.userid,label,packetInfo.id);
                break;
            }
        }
    }

    public void unsignedLabel(String label,PacketInfo packetInfo){
        for (LabelInfo info : this.labelInfos){
            if (label.equals(info.getLabel())){
                packetInfo.getLabels().remove(label);
                info.packetID = 0;
                if (!this.unsignedLables.contains(info))
                    this.unsignedLables.add(info);

                SQLiteHelper.getInstance().updatePacketOfLabel(this.userid,label,0);
                break;
            }
        }
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
        SQLiteHelper.getInstance().updateLabel(this.userid, label, isAdd);
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

        public String getLabel() {
            return label;
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

        public String getPacketName() {
            return packetName;
        }
    }
    //endregion
}
