package org.techtown.ThreeMate;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;
import org.techtown.ThreeMate.R;

import java.util.ArrayList;


public class DiaryFragment extends Fragment {
    private TextView resultText;
    RecyclerView recyclerView;
    FoodAdapter adapter2;
    public SQLiteManager sqLiteManager;
    private String idIndicator ="";
    private int sum=0;
    private ArrayList<String> foodkcal = new ArrayList<String>();
    private ArrayList<String> idIndicator2 = new ArrayList<String>();
    private ArrayList<String> matchfood = new ArrayList<String>();
    private ArrayList<String> matchdate = new ArrayList<String>();
    FragmentTransaction ft = getFragmentManager().beginTransaction();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_diary, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        sqLiteManager = new SQLiteManager(rootView.getContext(), "NewDiary.db", null, 1);
        GridLayoutManager layoutManager = new GridLayoutManager(rootView.getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);

        adapter2 = new FoodAdapter();
        adapter2.setOnItemClickListener(new OnFoodItemClickListener() {
            @Override
            public void onItemClick(FoodAdapter.ViewHolder holder, View view, final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        sqLiteManager.delete(idIndicator2.get(position));


                       ft.detach(DiaryFragment.this).attach(DiaryFragment.this).commit();
                        Toast.makeText(rootView.getContext(),"["+matchdate.get(position)+"]"+matchfood.get(position)+" 데이터가 삭제되었습니다.",Toast.LENGTH_LONG).show();
                    }
                });
                builder.setCancelable(true);
                builder.setNegativeButton("아니오",null);
                builder.setTitle("데이터 삭제");
                builder.setMessage("["+matchdate.get(position)+"]"+matchfood.get(position)+ " 데이터를 삭제하시겠습니까?");
                builder.show();



            }
        });

        return inflater.inflate(R.layout.fragment_diary, container, false);
    }
    private void updateList(){
        adapter2.removeItem();
        ArrayList<JSONObject> array = sqLiteManager.getResult("텍스트"); // DB의 내용을 배열단위로 모두 가져온다
        try{



            int length =  array.size(); // 배열의 길이
            for(int idx = 0; idx < length; idx++){  // 배열의 길이만큼 반복

                JSONObject object = array.get(idx);                // json의 idx번째 object를 가져와서,
                String id = object.getString("id");         // object 내용중 id를 가져와 저장.
                String name = object.getString("name");     // object 내용중 name를 가져와 저장.
                String kcal = object.getString("kcal");     // object 내용중 kcal를 가져와 저장.
                String carbs = object.getString("carbs");     // object 내용중 carbs를 가져와 저장.
                String protein = object.getString("protein");     // object 내용중 protein를 가져와 저장.
                String fat = object.getString("fat");     // object 내용중 fat를 가져와 저장.
                String date = object.getString("date");     // object 내용중 date를 가져와 저장.
                String Url = object.getString("url");     // object 내용중 date를 가져와 저장.
                // 저장한 내용을 토대로 ListView에 다시 그린다.

                adapter2.addItem(new FD(Url, "음식명 : " + name + "(" + date + ")" , "열량 : " + kcal + "kcal","탄수화물 : " + carbs+ "g", "단백질 : " + protein + "g","지방 : " +  fat+ "g"));

                recyclerView.setAdapter(adapter2);
                foodkcal.add(kcal);
                idIndicator2.add(id);
                matchfood.add(name);
                matchdate.add(date);


                idIndicator = id;
                sum += Double.parseDouble(kcal);
                resultText.setText(Double.toString(sum)+ " kcal");

            }
            Log.d("Lee", String.valueOf(sum + "kcal"));

        }
        catch (Exception e){
            Log.i("seo","error : " + e);

        }

    }
}