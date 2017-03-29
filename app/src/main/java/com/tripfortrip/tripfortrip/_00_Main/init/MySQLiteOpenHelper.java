package com.tripfortrip.tripfortrip._00_Main.init;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.tripfortrip.tripfortrip._00_Main.MainActivity;
import com.tripfortrip.tripfortrip._01_MyJourney.JourneyBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by wei-tzutseng on 2017/3/28.
 */

public class MySQLiteOpenHelper extends SQLiteOpenHelper {
    private  static final String DB_NAME = "TripForTrip";
    private  static final int DB_VERSION = 1;
    private  static final String TABLE_NAME ="MyJourney";
    private  static final String COL_id = "id";
    private  static final String COL_name = "journey_name";
    private  static final String COL_dateOfStart ="dateOfStart";
    private  static final String  COL_dateOfEnd = "dateOfEnd";
    private  static final String COL_picture="picture";
    Context context ;

    private static  final String TABLE_CREATE = "CREATE TABLE "+TABLE_NAME+"("+
            COL_id+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
            COL_name+" TEXT NOT NULL, "+
            COL_dateOfStart+" TEXT NOT NULL, "+
            COL_dateOfEnd+" TEXT NOT NULL, "+
            COL_picture+" INTEGER "+");";

    public MySQLiteOpenHelper(Context context) {
        super(context,DB_NAME, null, DB_VERSION);
//        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        db.execSQL(TABLE_CREATE);
        Log.d("TABLE_CREATE","成功");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TBLE IF EXISTS"+TABLE_NAME);
        onCreate(db);
    }

    //找全部
    public List<JourneyBean> getAllJourney(){
        SQLiteDatabase db = getReadableDatabase();
        String [] columes = {COL_id,COL_name,COL_dateOfStart,COL_dateOfEnd,COL_picture};
        Cursor cursor = db.query(TABLE_NAME,columes,null,null,null,null,null);
        List<JourneyBean> journeyList = new ArrayList<>();
        while(cursor.moveToNext() ){
            int id = cursor.getInt(0);
            String name =cursor.getString(1);
            String dateOfStart= cursor.getString(2);
            String dateOfEnd = cursor.getString(3);
            int picture = cursor.getInt(4);
            JourneyBean journeyBean = new JourneyBean(id,picture,name,dateOfStart,dateOfEnd);
            journeyList.add(journeyBean);
        }
        Log.d("journeyList",journeyList.toString());
        cursor.close();
        return journeyList;
    }

    //指定id
    public JourneyBean findById(int id){
        SQLiteDatabase db = getWritableDatabase();
        String [] columns = {COL_name,COL_dateOfStart,COL_dateOfEnd,COL_picture};
        String selection = COL_id+"=?;";
        String [] setectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(TABLE_NAME,columns,selection,setectionArgs,null,null,null);
        JourneyBean journeyBean = null;
        if(cursor.moveToNext()){
            String name =cursor.getString(1);
            String dateOfStart= cursor.getString(2);
            String dateOfEnd = cursor.getString(3);
            int picture = cursor.getInt(4);
            journeyBean = new JourneyBean(id,picture,name,dateOfStart,dateOfEnd);
        }
        cursor.close();
        return journeyBean;
    }

    public long insert(JourneyBean journeyBean){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_name,journeyBean.getTitle());
        values.put(COL_dateOfStart,journeyBean.getDateStart());
        values.put(COL_dateOfEnd,journeyBean.getDateEnd());
        values.put(COL_picture,journeyBean.getPic());
        return db.insert(TABLE_NAME,null,values);
    }



}
