package com.momu.tale.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.momu.tale.R;
import com.momu.tale.database.User;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 회원 가입 페이지
 * Created by songm on 2017-02-22.
 */

public class SignUpActivity extends AppCompatActivity {
    @BindView(R.id.editId) EditText editId;
    @BindView(R.id.editNick) EditText editNick;
    @BindView(R.id.editPwd) EditText editPwd;
    @BindView(R.id.btSignUp) Button btSignUp;
    @BindView(R.id.btMoveToSignIn) TextView btSignIn;
    @BindView(R.id.loadingProgress) ProgressBar loadingProgress;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    String TAG = "SignUpActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in uid : " + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        btSignIn.setText(Html.fromHtml("<u>이미 tale 회원이신가요?</u>"));   //Underbar 넣기 위해 html 태그 사용
    }

    /**
     * 회원가입 버튼 클릭 이벤트
     */
    @OnClick(R.id.btSignUp)
    void btnSignUpClick() {
        final String email = editId.getText().toString();
        final String pwd = editPwd.getText().toString();
        final String nick = editNick.getText().toString();
        Log.e("email : ", email + " , pwd : " + pwd);
        if (!email.contains("@") || pwd.trim().equals("") || nick.trim().equals("")) {
            Toast.makeText(getApplicationContext(), "모든 항목을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        loadingProgress.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        loadingProgress.setVisibility(View.GONE);
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "회원 가입을 실패하였습니다.",
                                    Toast.LENGTH_SHORT).show();
                        } else {

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference().child("user");

                            User user = new User(email, nick);
                            myRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);


                            Toast.makeText(SignUpActivity.this, "회원 가입을 성공하였습니다.", Toast.LENGTH_SHORT).show();
                            finish();
                        }


                        // ...
                    }
                });
    }

    /**
     * 로그인 버튼 클릭 이벤트
     */
    @OnClick(R.id.btMoveToSignIn)
    void btnSignInClick() {
        startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
