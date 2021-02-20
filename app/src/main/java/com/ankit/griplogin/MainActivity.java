package com.ankit.griplogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

//=================================Google Code Start=========================================
    Button GoogleBtn;
    //SignInButton signInButton;
    private GoogleApiClient googleApiClient;
    TextView textView;
    private static final int RC_SIGN_IN = 1;
//=================================Google Code End=========================================
//=================================FaceBook Code Start=========================================
    Button FacebookBtn;
    String fn,ln,pic,id;
    private TextView info;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    int f=0;
    private static final String EMAIL = "email";
 //=================================FaceBook Code End=========================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//=================================Google Code Start=====================================================
        GoogleSignInOptions gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        GoogleBtn=findViewById(R.id.googleBtn);
        GoogleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent,RC_SIGN_IN);
            }
        });
//=================================Google Code End=========================================


 //=================================FaceBook Code Start=========================================
        FacebookBtn = findViewById(R.id.facebookBtn);
        FacebookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("===============>FaceBook Button Clicked   !");
                f=1;
                loginButton.performClick();
               // if (v == FacebookBtn) {
                    // loginButton.performClick();
                //}
            }
        });
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        //AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(EMAIL));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                if(AccessToken.getCurrentAccessToken() != null){

                    GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted( JSONObject object, GraphResponse response) {
                                    // Application code
                                    JSONObject json = response.getJSONObject();
                                    System.out.println(json);
                                    Bean bean=Bean.getBean();
                                    try {
                                        bean.setId(json.getString("id"));
                                        bean.setName(json.getString("name"));
                                    }catch (JSONException e){e.printStackTrace();}
                                    Intent i=new Intent(MainActivity.this,ProfileActivity.class);
                                    f=1;
                                    i.putExtra("N",f);
                                    startActivity(i);
                                    finish();
                                }
                            });
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id,name,link");
                    request.setParameters(parameters);
                    request.executeAsync();
                }
            }
            @Override
            public void onCancel() {  }
            @Override
            public void onError(FacebookException e) { }
        });
 //=================================FaceBook Code End=========================================




//-------------------------------------------------------------------------------------------------------------
     }       //End of onCreate Method
//-------------------------------------------------------------------------------------------------------------


//=================================Public Method====================================================
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//=================================FaceBook Code Start=========================================
        callbackManager.onActivityResult(requestCode, resultCode, data);
//=================================FaceBook Code End=========================================
        super.onActivityResult(requestCode, resultCode, data);
        //System.out.println("===============>FaceBook Ankit !");
//=================================Google Code Start=========================================
        if(requestCode==RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
//=================================Google Code Start=========================================


    }//------>End of onActivityResult Method
//=================================Public Method End=========================================


//-------------------------------------------------------------------------------------------------------------------------

//=================================Google Code Start=========================================
   private void handleSignInResult(GoogleSignInResult result){
      if(result.isSuccess()){
        gotoProfile();
      }else{
        Toast.makeText(getApplicationContext(),"Sign in cancel",Toast.LENGTH_LONG).show();
    }
   }
    private void gotoProfile(){
        Intent i=new Intent(MainActivity.this, ProfileActivity.class);
        f=2;
        i.putExtra("N",f);
        startActivity(i);
        finish();
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
//=================================Google Code End=========================================


}    //End of All MainActivity