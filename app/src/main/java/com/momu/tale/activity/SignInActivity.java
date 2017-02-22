package com.momu.tale.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.momu.tale.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by songm on 2017-02-23.
 */

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.editId) EditText editId;
    @BindView(R.id.editPwd) EditText editPwd;
    @BindView(R.id.btSignIn) Button btSignIn;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    String TAG = "Firebase";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        ButterKnife.bind(this);

        btSignIn.setOnClickListener(this);


        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btSignIn){
            String email = editId.getText().toString();
            String pwd =  editPwd.getText().toString();
            mAuth.signInWithEmailAndPassword(email, pwd)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "signInWithEmail:failed", task.getException());
                                Toast.makeText(SignInActivity.this, "로그인 실패",
                                        Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(SignInActivity.this, "로그인 성공",
                                        Toast.LENGTH_SHORT).show();
                                finish();
                            }

                            // ...
                        }
                    });

        }
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
