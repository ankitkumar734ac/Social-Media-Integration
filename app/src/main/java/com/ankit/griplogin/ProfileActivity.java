package com.ankit.griplogin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    ImageView prof;
    Button logout;
    TextView name,id,email,emailField;
    ProfilePictureView profile;
    JSONObject jObj;

    ImageView profileImage;
    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions gso;

    int f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
         f=intent.getIntExtra("N",0);
        profile = (ProfilePictureView) findViewById(R.id.picture);
        name = findViewById(R.id.name);
        id = findViewById(R.id.uId);
        profileImage=findViewById(R.id.profile);
        email=findViewById(R.id.emailLeft);
        emailField=findViewById(R.id.emailRight);
//=================================SingOut Button Code Start=========================================
        logout=findViewById(R.id.singout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(f==1) {    // FaceBook SignOut
                    Intent i = new Intent(ProfileActivity.this, MainActivity.class);
                    LoginManager.getInstance().logOut();
                    Toast.makeText(getApplicationContext(), "SignOut", Toast.LENGTH_LONG).show();
                    startActivity(i);
                    finish();
                }else if(f==2) {    //Google SignOut
                    Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                            new ResultCallback<Status>() {
                                @Override
                                public void onResult(Status status) {
                                    if (status.isSuccess()) {
                                        Toast.makeText(getApplicationContext(), "SignOut", Toast.LENGTH_LONG).show();
                                        gotoMainActivity();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Session not close", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }else if(f==3){

                }else if(f==4){

                }else if(f==5){

                }
 //---------------------------------------------------------------------------------------------
            }
        });
//=================================SingOut Button Code End=========================================


 //=================================FaceBook Code Start=========================================
        if(f==1) {
            String jsonString = intent.getStringExtra("json");
            System.out.println("Profile:=====> "+jsonString);
            try {
                //jObj = new JSONObject(jsonString);
                //System.out.println("ProfileActivity"+jObj);
                //profile.setProfileId(jObj.getString("id"));
                //name.setText(jObj.getString("name"));
                //id.setText(jObj.getString("id"));
                // jObj.getString("email");
                //jObj.getString("link"); //Profile
                Bean bean=Bean.getBean();
                profile.setVisibility(View.VISIBLE);//----------------->
                profile.setProfileId(bean.getId());
                name.setText(bean.getName());
                id.setText(bean.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
//=================================FaceBook Code End================================================
//=================================Google Code Start=========================================
        }else if(f==2){
            System.out.println("==========================Google Clicked==============");
            gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();

            googleApiClient=new GoogleApiClient.Builder(this)
                    .enableAutoManage(this,this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                    .build();

//=================================Google Code End=========================================
        }else{
            System.out.println("==========================Nothing Clicked==============");
        }

    }//----------->>>>>End of onCreate Method

    @Override
    protected void onStart() {
        super.onStart();
//=================================Google Code Start=========================================
        if(f==2) {
            OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
            if (opr.isDone()) {
                GoogleSignInResult result = opr.get();
                handleSignInResult(result);
            } else {
                opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                    @Override
                    public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                        handleSignInResult(googleSignInResult);
                    }
                });
            }
        }
//=================================Google Code End=========================================
    }  //----->End of onStart Method


//=================================Google Code Start=========================================
    private void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
            GoogleSignInAccount account=result.getSignInAccount();
            name.setText(account.getDisplayName());
            emailField.setVisibility(View.VISIBLE);
            email.setVisibility(View.VISIBLE);
            email.setText(account.getEmail());
            id.setText(account.getId());
            profileImage.setVisibility(View.VISIBLE);
            try{
                Glide.with(this).load(account.getPhotoUrl()).into(profileImage);
            }catch (NullPointerException e){
                Toast.makeText(getApplicationContext(),"image not found",Toast.LENGTH_LONG).show();
            }

        }else{
            gotoMainActivity();
        }
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    private void gotoMainActivity(){
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

//=================================Google Code Start=========================================
}//----------End of all ProfileActivity