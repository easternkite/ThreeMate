package org.techtown.ThreeMate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private SignInButton btn_google; // 구글 로그인 버튼
    private FirebaseAuth auth; // 파이어 베이스 인증 객체
    private FirebaseUser user;
    private ImageView iv_mark;
    private GoogleApiClient googleApiClient; // 구글 API 클라이언트 객체
    private static final int REQ_SIGN_GOOGLE = 100; // 구글 로그인 결과 코드
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha); //Context와 Animation xml파일
        Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.upward); //Context와 Animation xml파일



        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();
        iv_mark = findViewById(R.id.iv_mark);

        auth = FirebaseAuth.getInstance(); // 파이어베이스 인증 객체 초기화.
        user = auth.getCurrentUser();
        btn_google = findViewById(R.id.btn_google);
        btn_google.setOnClickListener(new View.OnClickListener() { // 구글 로그인 버튼을 클릭했을 때 이곳을 수행.
            @Override
            public void onClick(View view) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, REQ_SIGN_GOOGLE);
            }
        });
        animation.setFillEnabled(false);    //애니메이션 이 끝난곳에 고정할지 아닐지
        btn_google.startAnimation(animation);    //애니메이션 시작

        animation2.setFillEnabled(true);    //애니메이션 이 끝난곳에 고정할지 아닐지
        iv_mark.startAnimation(animation2);    //애니메이션 시작


        if (user != null){
            Intent intent = new Intent(getApplicationContext(), Result.class);
            intent.putExtra("nickName",user.getDisplayName());
            intent.putExtra("photoUrl",String.valueOf(user.getPhotoUrl()));

            startActivity(intent);

            finish();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) { // 구글 로그인 인증을 요청 했을 때 결과 값을 되돌려 받는 곳.
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQ_SIGN_GOOGLE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()) { // 인증결과가 성공적이면..
                GoogleSignInAccount account = result.getSignInAccount(); // account 라는 데이터는 구글로그인 정보를 담고있습니다. (닉네임,프로필사진Url,이메일주소...등)
                resultLogin(account); // 로그인 결과 값 출력 수행하라는 메소드
            }
        }

    }

    private void resultLogin(final GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) { // 로그인이 성공했으면...
                            Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), Result.class);
                            intent.putExtra("nickName",account.getGivenName());
                            intent.putExtra("photoUrl",String.valueOf(account.getPhotoUrl())); // String.valueOf() 특정 자료형을 String 형태로 변환.
                            startActivity(intent);
                            finish();
                        } else { // 로그인이 실패했으면..
                            Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
