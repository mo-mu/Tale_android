//package com.momu.tale.activity;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ProgressBar;
//import android.widget.Toast;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.momu.tale.R;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//
///**
// * Created by songm on 2017-02-23.
// */
//
//public class SignInActivity extends AppCompatActivity {
//    @BindView(R.id.editId) EditText editId;
//    @BindView(R.id.editPwd) EditText editPwd;
//    @BindView(R.id.loadingProgress) ProgressBar loadingProgress;
//
//    private FirebaseAuth mAuth;
//    private FirebaseAuth.AuthStateListener mAuthListener;
//
//    Context mContext;
//
//    String TAG = "SignInActivity";
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_signin);
//        ButterKnife.bind(this);
//        mContext = this;
//
////        mAuth = FirebaseAuth.getInstance();
////        mAuthListener = new FirebaseAuth.AuthStateListener() {
////            @Override
////            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
////                FirebaseUser user = firebaseAuth.getCurrentUser();
////                if (user != null) {
////                    // User is signed in
////                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
////                } else {
////                    // User is signed out
////                    Log.d(TAG, "onAuthStateChanged:signed_out");
////                }
////                // ...
////            }
////        };
//    }
//
//    /**
//     * 로그인 버튼 클릭 이벤트
//     */
//    @OnClick(R.id.btSignIn)
//    void btnSignInClick() {
//        String email = editId.getText().toString();
//        String pwd = editPwd.getText().toString();
//
//        if (!email.contains("@") || pwd.trim().equals("")) {
//            Toast.makeText(mContext, "이메일 또는 비밀번호를 입력하지 않았습니다.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        loadingProgress.setVisibility(View.VISIBLE);
//        mAuth.signInWithEmailAndPassword(email, pwd)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        loadingProgress.setVisibility(View.GONE);
//                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
//
//                        // If sign in fails, display a message to the user. If sign in succeeds
//                        // the auth state listener will be notified and logic to handle the
//                        // signed in user can be handled in the listener.
//                        if (task.isSuccessful()) {
//                            Toast.makeText(SignInActivity.this, "로그인을 성공하였습니다.", Toast.LENGTH_SHORT).show();
//                            finish();
//
//                        } else {
//                            Log.w(TAG, "signInWithEmail:failed", task.getException());
//                            Toast.makeText(SignInActivity.this, "로그인을 실패하였습니다.", Toast.LENGTH_SHORT).show();
//                        }
//
//                        // ...
//                    }
//                });
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        mAuth.addAuthStateListener(mAuthListener);
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        if (mAuthListener != null) {
//            mAuth.removeAuthStateListener(mAuthListener);
//        }
//    }
//}
