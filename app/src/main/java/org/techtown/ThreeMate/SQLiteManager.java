package org.techtown.ThreeMate;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import org.json.JSONObject;
import java.util.ArrayList;

public class SQLiteManager extends SQLiteOpenHelper {

    private String tableName = "FOOD";
    private String tableNameUser = "User";
    public SQLiteManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // DB를 새로 생성할 때 호출되는 함수
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + tableName + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, kcal TEXT, carbs TEXT, protein TEXT, fat TEXT, date TEXT, url TEXT, time TEXT);");
        db.execSQL("CREATE TABLE " + tableNameUser + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, userName TEXT,userProfile TEXT,bornDate TEXT, gender TEXT, bodyLength TEXT, bodyWeight TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // 테이블에 row 추가
    public void insert(String name, String kcal, String carbs, String protein, String fat, String date, String url, String time) {
        SQLiteDatabase db = getWritableDatabase();

        String query = "INSERT INTO " + tableName + " VALUES(null, '" + name + "', '" + kcal + "', '" + carbs + "', '" + protein + "', '" + fat + "', '" + date + "', '" + url + "', '" + time + "');";
        db.execSQL(query);

    }
    public void insert2(String name, String kcal, String carbs, String protein, String fat, String date, String url, String time) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "INSERT INTO " + tableName + " SELECT null, '" + name + "','" + kcal + "', '" + carbs + "','" + protein + "','" + fat + "','" + date + "','" + url + "','" + time + "'  WHERE NOT EXISTS (SELECT * FROM " +  tableName + " WHERE date = '" + date + "' AND time = '" + time + "')";
        //String query = "INSERT INTO " + tableName + " VALUES(null,'" + userName + "', '" + title + "', '" + contents+ "', '" + profile+ "', '" + date + "', '" + time + "', '" + address + "') WHERE NOT EXISTS (SELECT * FROM " + tableName + " WHERE date = '" + date + "' AND time = '" + time + ");";
        db.execSQL(query);
    }




    public void insertUser(String userName,String userProfile, String bornDate, String gender, String bodyLength, String bodyWeight) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "INSERT INTO " + tableNameUser + " SELECT null, '" + userName + "','" + userProfile + "', '" + bornDate + "','" + gender + "','" + bodyLength + "','" + bodyWeight + "'  WHERE NOT EXISTS (SELECT * FROM " +  tableNameUser + " WHERE userName = '" + userName + "')";
        //String query = "INSERT INTO " + tableName + " VALUES(null,'" + userName + "', '" + title + "', '" + contents+ "', '" + profile+ "', '" + date + "', '" + time + "', '" + address + "') WHERE NOT EXISTS (SELECT * FROM " + tableName + " WHERE date = '" + date + "' AND time = '" + time + ");";
        db.execSQL(query);
    }



    // id 값에 맞는 DB row 업데이트
    public void update(String name, String kcal, String carbs, String protein, String fat, String date, String url, String time) {
        SQLiteDatabase db = getWritableDatabase();

        String query = "UPDATE " + tableName +" SET name='" + name + "', kcal='" + kcal + "', carbs='" + carbs + "', protein='" + protein + "', fat='" + fat + "', date='" + date + "', url='" + url +"', time='" + time + "'+ WHERE id="  + ";";
        db.execSQL(query);

    }

    // id에 맞는 DB row 삭제
    public void delete(String id) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "DELETE FROM " + tableName + " WHERE id='" + id + "';";
        db.delete(tableName,"_id= '" + id + "';", null);

    }

    // table 내용 전부 삭제
    public void clear(String date){
        SQLiteDatabase db = getWritableDatabase();


        db.delete(tableName,"date= '" + date + "';",null);


    }

    public void deleteAll() {
        SQLiteDatabase db = getWritableDatabase();
        String query = "DELETE FROM " + tableName + "';";
        db.delete(tableName,"", null);
        db.delete(tableNameUser,"", null);
    }

    public int getTotalExpenses(String kcal)
    {
        int total = 0;
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM("+ kcal + ") FROM " + tableName, null);
        if (cursor.moveToFirst())
        {
            total = cursor.getInt(0);
        }
        while (cursor.moveToNext());
        return total;
    }
    /**
     * getResult : table에 있는 모든 내용을 조회해서, jsonObject 단위의 배열을 리턴한다.
     * @return ArrayList<jsonArray>
     */
    public ArrayList<JSONObject> getResult(String date) {
        ArrayList<JSONObject> array = new ArrayList<JSONObject>();
        try{
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM " + tableName + " WHERE date='" + date + "';", null);
            while (cursor.moveToNext()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id",cursor.getString(0));
                jsonObject.put("name",cursor.getString(1));
                jsonObject.put("kcal",cursor.getString(2));
                jsonObject.put("carbs",cursor.getString(3));
                jsonObject.put("protein",cursor.getString(4));
                jsonObject.put("fat",cursor.getString(5));
                jsonObject.put("date",cursor.getString(6));
                jsonObject.put("url",cursor.getString(7));
                jsonObject.put("time",cursor.getString(8));
                /**
                 * ex
                 * jsonObject 변수에 들어간 포맷양식
                 * {
                 *      "id" : "1",
                 *      "name" : "수박",
                 *      "kcal" : "385",
                 *      "date" : "2020-09-11"
                 * }
                 *
                 * ex2
                 * jsonObject Array 포맷양식
                 * [
                 *      {
                 *          "id" : "1",
                 *          "name" : "수박",
                 *          "kcal" : "385",
                 *          "date" : "2020-09-11"
                 *      },
                 *      {
                 *          "id" : "2",
                 *          "name" : "오렌지",
                 *          "kcal" : "120",
                 *          "date" : "2020-09-11"
                 *      },
                 *      ...
                 *
                 * ]
                 */
                array.add(jsonObject);
            }
        }
        catch (Exception e){
            Log.i("Lee","error : " + e);
        }

        return array;
    }

    public ArrayList<JSONObject> getResultUser() {
        ArrayList<JSONObject> array = new ArrayList<JSONObject>();
        try{
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM " + tableNameUser  + "';'", null);
            while (cursor.moveToNext()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id",cursor.getString(0));
                jsonObject.put("userName",cursor.getString(1));
                jsonObject.put("userProfile",cursor.getString(2));
                jsonObject.put("bornDate",cursor.getString(3));
                jsonObject.put("gender",cursor.getString(4));
                jsonObject.put("bodyLength",cursor.getString(5));
                jsonObject.put("bodyWeight",cursor.getString(6));
                array.add(jsonObject);
            }
        }
        catch (Exception e){
            Log.i("Lee","error : " + e);
        }

        return array;
    }




    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.disableWriteAheadLogging();
    }
}

