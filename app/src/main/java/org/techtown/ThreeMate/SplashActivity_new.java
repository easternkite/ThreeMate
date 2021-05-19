package org.techtown.ThreeMate;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class SplashActivity_new extends AppCompatActivity {
    ConstraintLayout Splash_View;
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_new);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            Splash_View = findViewById(R.id.Splash_View);
            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha); //Context와 Animation xml파일
            animation.setAnimationListener(new Animation.AnimationListener() {  //Animation Listener 순서대로 시작할때, 끝날때, 반복될때
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {

                    try{
                        Thread.sleep(800); //2.5초간 화면 표시
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }

                    Intent intent2 = new Intent(SplashActivity_new.this, MainActivity.class);
                    //로그인테스트 하려면 MainActivity -> LoginActivity 로 변경해주세요
                    startActivity(intent2);
                    //overridePendingTransition(0, 0); //액티비티 전환 모션 제거
                    finish();

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            animation.setFillEnabled(false);    //애니메이션 이 끝난곳에 고정할지 아닐지
            Splash_View.startAnimation(animation);    //애니메이션 시작



        }




    }


}