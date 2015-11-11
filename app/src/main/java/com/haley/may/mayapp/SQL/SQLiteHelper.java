package com.haley.may.mayapp.SQL;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.haley.may.mayapp.Model.Daily.DailyModel;
import com.haley.may.mayapp.Model.Daily.LabelCollection;
import com.haley.may.mayapp.Model.Record.RecordModel;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lenovo on 2015/10/8.
 */
public class SQLiteHelper {
    private final String createTableDaily = "CREATE TABLE IF NOT EXISTS Daily (id INTEGER NOT NULL PRIMARY KEY,user_id INTEGER NOT NULL,date INTEGER,breakfast TEXT,lunch TEXT,dinner TEXT,other TEXT);";
    private final String createTableLabel = "CREATE TABLE IF NOT EXISTS Label (id INTEGER NOT NULL PRIMARY KEY,user_id INTEGER NOT NULL,label TEXT,count INTEGER,packet_id INTEGER);";
    private final String createTableRecord = "CREATE TABLE IF NOT EXISTS Record (id INTEGER NOT NULL PRIMARY KEY,user_id INTEGER NOT NULL,date INTEGER,title TEXT,text TEXT);";
    private final String createTablePacket = "CREATE TABLE IF NOT EXISTS Packet (id INTEGER NOT NULL PRIMARY KEY,user_id INTEGER NOT NULL,packet TEXT);";

    private final String dataBasePath = "/data/data/com.haley.may.mayapp/database/";
    private final String dataBaseName = "daily.db";

    private final static SQLiteHelper instance = new SQLiteHelper();

    public static SQLiteHelper getInstance(){
        return SQLiteHelper.instance;
    }

    private SQLiteHelper(){
        File dir = new File(dataBasePath);
        if(dir.exists() == false){
            dir.mkdirs();
        }

        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(this.dataBasePath+this.dataBaseName,null);

//        db.execSQL("DROP TABLE Daily;");
//        db.execSQL("DROP TABLE Label;");
//        db.execSQL("DROP TABLE Record;");
        db.execSQL(this.createTableDaily);
        db.execSQL(this.createTableLabel);
        db.execSQL(this.createTableRecord);
        db.execSQL(this.createTablePacket);

        db.close();
    }

    //region Daily
    public void addDaily(int userid,String date){
        SQLiteDatabase db;
        Cursor cursor;
        int count = 0;

        db = SQLiteDatabase.openOrCreateDatabase(this.dataBasePath+this.dataBaseName, null);
        cursor = db.rawQuery("select count(id) as count from Daily where user_id=? and date=(select strftime('%s',?))", new String[]{Integer.toString(userid), date});
        if(cursor.moveToFirst()) {
            count = Integer.parseInt(cursor.getString(0));
        }

        if(count == 1)
            return;

        db.execSQL("INSERT INTO Daily(user_id,date) VALUES(?,(select strftime('%s',?)))", new Object[]{userid, date});

        db.close();

        Log.i("SQLite", "-->>");
    }

    public Date getStartDaily(int userid){
        SQLiteDatabase db;
        Cursor cursor;
        Date date = new Date(System.currentTimeMillis());

        db = SQLiteDatabase.openOrCreateDatabase(this.dataBasePath + this.dataBaseName, null);
        cursor = db.rawQuery("select date((select datetime(date,'unixepoch'))) as datetime from Daily where user_id=? order by datetime limit 1", new String[]{Integer.toString(userid)});

        try {
            if (cursor.getCount() != 0 && cursor.moveToFirst())
                date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(cursor.getString(0)+" 00:00:00");
        }
        catch (Exception ce){
            Log.i("SQLite", "getStartDaily-->>" + ce.toString());
        }

        Log.i("SQLite", "getStartDaily-->>" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
        db.close();
        return date;
    }

    public List<String[]> getDaily(int userid,String start,String end){
        SQLiteDatabase db;
        Cursor cursor;
        List<String[]> list = new ArrayList<String[]>();
        int count = 0;

        Log.i("SQLite", "DailyCount-->>" + start +"    " + end);

        db = SQLiteDatabase.openOrCreateDatabase(this.dataBasePath + this.dataBaseName, null);
        cursor = db.rawQuery("select id, date((select datetime(date,'unixepoch'))) as date,breakfast,lunch,dinner,other from Daily where user_id=? and date>=(select strftime('%s',?)) and date<=(select strftime('%s',?)) order by date desc", new String[]{Integer.toString(userid), start, end});
        count = cursor.getCount();

        if ( cursor != null ) {
            if ( cursor.moveToFirst() ){
                do {
                    list.add(new String[]{cursor.getString(cursor.getColumnIndex("id")),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5)});
                    //model.AddDailyInfo(cursor.getString(cursor.getColumnIndex("id")),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5));
                }while ( cursor.moveToNext() );
            }
        }

        Log.i("SQLite", "DailyCount-->>" + Integer.toString(count));

        db.close();

        return list;
    }

    public void UpdateDaily(String id,String field,String value){
        SQLiteDatabase db;
        db = SQLiteDatabase.openOrCreateDatabase(this.dataBasePath + this.dataBaseName, null);
        db.execSQL("update Daily set " + field + "=? where id =?", new Object[]{value, id});

        db.close();
    }

    public List<String[]> getLabel(int userid){
        SQLiteDatabase db;
        Cursor cursor;
        List<String[]> list = new ArrayList<String[]>();
        int count = 0;

        db = SQLiteDatabase.openOrCreateDatabase(this.dataBasePath + this.dataBaseName, null);
        cursor = db.rawQuery("select id, label,count,packet_id from Label where user_id=? order by count desc", new String[]{Integer.toString(userid)});
        count = cursor.getCount();

        if ( cursor != null ) {
            if ( cursor.moveToFirst() ){
                do {
                    list.add(new String[]{cursor.getString(1), cursor.getString(2),cursor.getString(3) == null ? "0" : cursor.getString(3)});
                }while ( cursor.moveToNext() );
            }
        }

        db.close();

        Log.i("SQLite", "LabelCount-->>" + Integer.toString(count));

        return list;
    }

    public void updateLabel(int userid,String label,boolean isAdd){
        SQLiteDatabase db;
        Cursor cursor;
        int count = 0;

        db = SQLiteDatabase.openOrCreateDatabase(this.dataBasePath + this.dataBaseName, null);
        cursor = db.rawQuery("select count from Label where user_id=? and label=?", new String[]{Integer.toString(userid), label});
        count = cursor.getCount();

        if (count == 0 && isAdd == true){
            db.execSQL("INSERT INTO Label(user_id,label,count) values(?,?,?)",new Object[]{userid,label,1});
        }
        else if (count > 0){
            cursor.moveToLast();
            int labelcount = cursor.getInt(0);

            if(labelcount == 1 && isAdd == false)
                db.execSQL("delete from Label where user_id=? and label=?",new Object[]{userid,label});
            else if(labelcount > 0) {
                labelcount += isAdd == true ? 1 : -1;
                Log.i("SQLite", "labelitemcount-->>" + Integer.toString(labelcount));
                db.execSQL("update Label set count=? where user_id=? and label=?", new Object[]{Integer.toString(labelcount),Integer.toString( userid), label});
            }
        }

        db.close();
    }
    //endregion

    //region Record
    public void addRecord(int userid,@NonNull String date,@NonNull String title,@NonNull String text){
        SQLiteDatabase db;
        Cursor cursor;

        db = SQLiteDatabase.openOrCreateDatabase(this.dataBasePath+this.dataBaseName, null);
        db.execSQL("INSERT INTO Record(user_id,date,title,text) VALUES(?,(select strftime('%s',?)),?,?)", new Object[]{userid, date, title, text});

        db.close();

        Log.i("SQLite", "addRecord-->>"+date+title+text);
    }

    public void deleteRecord(int userid,int id){
        SQLiteDatabase db;
        Cursor cursor;

        db = SQLiteDatabase.openOrCreateDatabase(this.dataBasePath+this.dataBaseName, null);
        db.execSQL("delete from Record where id=?",new Object[]{id});
    }

    public List<String[]> getRecord(int userid){
        SQLiteDatabase db;
        Cursor cursor;
        List<String[]> list = new ArrayList<String[]>();
        int count = 0;

        db = SQLiteDatabase.openOrCreateDatabase(this.dataBasePath + this.dataBaseName, null);
        cursor = db.rawQuery("select id, date((select datetime(date,'unixepoch'))) as date,title,text from Record where user_id=? order by date", new String[]{Integer.toString(userid)});
        count = cursor.getCount();



        if ( cursor != null ) {
            if ( cursor.moveToFirst() ){
                do {
                    list.add(new String[]{cursor.getString(0), cursor.getString(1), cursor.getString(2),cursor.getString(3)});
                }while ( cursor.moveToNext() );
            }
        }

        Log.i("SQLite", "RecordCount-->>" + Integer.toString(count));

        db.close();

        return list;
    }
    //endregion

    //region Packet
    public int addPacket(int userid,String packet){
        SQLiteDatabase db;
        Cursor cursor;
        int count = 0;

        db = SQLiteDatabase.openOrCreateDatabase(this.dataBasePath+this.dataBaseName, null);
        cursor = db.rawQuery("select count(id),id as count from Packet where user_id=? and packet=?", new String[]{Integer.toString(userid), packet});
        if(cursor.moveToFirst()) {
            count = Integer.parseInt(cursor.getString(0));
        }

        if(count == 1)
            return cursor.getInt(1);

        db.execSQL("INSERT INTO Packet(user_id,packet) VALUES(?,?)", new Object[]{userid, packet});

        cursor = db.rawQuery("select id from Packet where user_id=? and packet=?", new String[]{Integer.toString(userid),packet});
        if(cursor.moveToFirst()) {
            count = Integer.parseInt(cursor.getString(0));
        }

        db.close();

        return count;
    }

    public void updatePacketName(int userid,int packetID,int name){
        SQLiteDatabase db;
        Cursor cursor;

        db = SQLiteDatabase.openOrCreateDatabase(this.dataBasePath + this.dataBaseName, null);
        db.execSQL("update Packet set packet=? where id=? and user_id=?", new Object[]{name,Integer.toString(packetID), Integer.toString(userid)});
    }

    public void deletePacket(int userid,int packetID){
        SQLiteDatabase db;
        Cursor cursor;

        db = SQLiteDatabase.openOrCreateDatabase(this.dataBasePath+this.dataBaseName, null);
        db.execSQL("update Label set packet_id=? where user_id=? and packet_id=?", new Object[]{0, Integer.toString(userid), Integer.toString(packetID)});

        db.execSQL("delete from Packet where id=?", new Object[]{Integer.toString(packetID)});
    }

    public List<String[]> getPacket(int userid){
        SQLiteDatabase db;
        Cursor cursor;
        int count = 0;
        List<String[]> list = new ArrayList<String[]>();

        db = SQLiteDatabase.openOrCreateDatabase(this.dataBasePath + this.dataBaseName, null);
        cursor = db.rawQuery("select id,packet from Packet where user_id=? order by id", new String[]{Integer.toString(userid)});
        count = cursor.getCount();

        if ( cursor != null ) {
            if ( cursor.moveToFirst() ){
                do {
                    list.add(new String[]{cursor.getString(0), cursor.getString(1)});
                }while ( cursor.moveToNext() );
            }
        }


        db.close();
        Log.i("SQLite", "PacketCount-->>" + Integer.toString(count));
        return list;
    }

    public void updatePacketOfLabel(int userid,String label,int packetID){
        SQLiteDatabase db;
        Cursor cursor;

        db = SQLiteDatabase.openOrCreateDatabase(this.dataBasePath + this.dataBaseName, null);
        db.execSQL("update Label set packet_id=? where user_id=? and label=?", new Object[]{Integer.toString(packetID), Integer.toString(userid), label});

    }
    //endregion
}
