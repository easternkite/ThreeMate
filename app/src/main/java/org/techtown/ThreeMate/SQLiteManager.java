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
    public SQLiteManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // DB를 새로 생성할 때 호출되는 함수
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + tableName + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, kcal TEXT, carbs TEXT, protein TEXT, fat TEXT, date TEXT, url TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // 테이블에 row 추가
    public void insert(String name, String kcal, String carbs, String protein, String fat, String date, String url) {
        SQLiteDatabase db = getWritableDatabase();

        String query = "INSERT INTO " + tableName + " VALUES(null, '" + name + "', '" + kcal + "', '" + carbs + "', '" + protein + "', '" + fat + "', '" + date + "', '" + url + "');";
        db.execSQL(query);

    }

    // id 값에 맞는 DB row 업데이트
    public void update(String name, String kcal, String carbs, String protein, String fat, String date, String url) {
        SQLiteDatabase db = getWritableDatabase();

        String query = "UPDATE " + tableName +" SET name='" + name + "', kcal='" + kcal + "', carbs='" + carbs + "', protein='" + protein + "', fat='" + fat + "', date='" + date + "', url='" + url + "' WHERE id="  + ";";
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
            Log.i("seo","error : " + e);
        }

        return array;
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.disableWriteAheadLogging();
    }
}

