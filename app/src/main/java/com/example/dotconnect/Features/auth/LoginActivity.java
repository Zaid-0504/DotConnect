package com.example.dotconnect.Features.auth;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.dotconnect.Features.Homepage.MainActivity;
import com.example.dotconnect.Model.AuthResponse;
import com.example.dotconnect.Model.Users;
import com.example.dotconnect.R;
import com.example.dotconnect.data.LoginViewModel;
import com.example.dotconnect.utils.ViewModelFactory;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.messaging.FirebaseMessaging;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    GoogleSignInOptions gso;
    GoogleSignInClient mGooglesignInclint;
    SignInButton SignIn_button;
    ActivityResultLauncher<Intent> signInLauncher;
    private LoginViewModel loginViewModel;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        String Default_web_client_id="359043626527-2npermfigrcspn9hbujengeqkcj9h6dk.apps.googleusercontent.com";
        gso= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(Default_web_client_id)
                .requestEmail()
                .build();
        mAuth=FirebaseAuth.getInstance();
        mGooglesignInclint= GoogleSignIn.getClient(this,gso);

        loginViewModel= new ViewModelProvider(this, (ViewModelProvider.Factory) new ViewModelFactory())
                .get(LoginViewModel.class);
        SignIn_button=findViewById(R.id.SignIn_button);
        progressBar=findViewById(R.id.login_progress);


         signInLauncher= registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){
                            Task<GoogleSignInAccount> task= GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                            try {
                                GoogleSignInAccount Google_account= task.getResult(ApiException.class);
                                FirebaseAuthWithGoogle(Google_account.getIdToken());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

        SignIn_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }
    private void signIn(){
        Intent signInIntent= mGooglesignInclint.getSignInIntent();
        signInLauncher.launch(signInIntent);
    }
    private void FirebaseAuthWithGoogle(String IdToken){
        AuthCredential credential= GoogleAuthProvider.getCredential(IdToken,null);
        progressBar.setVisibility(View.VISIBLE);
        SignIn_button.setVisibility(View.GONE);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseMessaging.getInstance().getToken()
                                    .addOnCompleteListener(new OnCompleteListener<String>() {
                                        @Override
                                        public void onComplete(@NonNull Task<String> task) {
                                            if(task.isSuccessful()){
                                                FirebaseUser user = mAuth.getCurrentUser();
                                                loginViewModel.login(new Users(user.getUid(),
                                                        user.getDisplayName(),
                                                        user.getEmail(),
                                                        user.getPhotoUrl().toString(),
                                                        "",
                                                        task.getResult()))
                                                        .observe(LoginActivity.this, new Observer<AuthResponse>() {
                                                            @Override
                                                            public void onChanged(AuthResponse authResponse) {
                                                                if(authResponse.getAuth()!=null){
                                                                    progressBar.setVisibility(View.GONE);
                                                                    updateUI(user);}
                                                                else{
                                                                    progressBar.setVisibility(View.GONE);
                                                                    SignIn_button.setVisibility(View.VISIBLE);
                                                                    FirebaseAuth.getInstance().signOut();
                                                                    mGooglesignInclint.signOut();
                                                                    updateUI(null);}
                                                            }
                                                        });
                                            }
                                        }
                                    });
                            progressBar.setVisibility(View.GONE);
                            SignIn_button.setVisibility(View.VISIBLE);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user){
        if(user != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        SignIn_button.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null)
            updateUI(currentUser);
    }
}