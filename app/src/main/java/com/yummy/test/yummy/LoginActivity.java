package com.yummy.test.yummy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yummy.test.yummy.helper.Constants;
import com.yummy.test.yummy.parser.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by User on 3/19/2018.
 */

public class LoginActivity extends AppCompatActivity {

    EditText mEmail,mPassword;
    Button mLogin;
    TextView mForget,mRegister;
    String email,password;
    JsonParser jsonParser = new JsonParser();//include json parser
    JSONObject jsonObject;
    int status=0;
    ProgressDialog mLoading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = (EditText)findViewById(R.id.activity_login_et_email);
        mPassword = (EditText)findViewById(R.id.activity_login_et_password);
        mLogin = (Button) findViewById(R.id.activity_login_btn_login);
        mRegister = (TextView)findViewById(R.id.activity_login_tv_register);
        mForget = (TextView)findViewById(R.id.activity_login_tv_forget);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    public void login()
    {
        if(!validate())
        {
            Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show();
        }
        else
        {
           new PerformLogin().execute ();
        }
    }
    public boolean validate()
    {
        Boolean valid = true;
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            mEmail.setError("enter a valid email address");
            valid=false;
        }
        else
        {
            mEmail.setError(null);
        }
        if (password.isEmpty() || password.length() < 4 || password.length() > 10)
        {
            mPassword.setError("between 4 and 10 alphanumeric characters");
            valid=false;
        }
        else
        {
            mPassword.setError(null);
        }
        return valid;
    }

    class PerformLogin extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoading = new ProgressDialog(LoginActivity.this);
            mLoading.setMessage("Logining in...");
            mLoading.setCancelable(false);//loading hunda cancel garne ki nai
            mLoading.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> hashMap = new HashMap<>();//hashmap ma key ra value dubai string ma
            hashMap.put("email",email);//postman ma bhako key
            hashMap.put("password",password);

            String url = Constants.BASE_URL+ "api/login";

            jsonObject = jsonParser.performPostCI(url,hashMap);
            mLoading.dismiss();//loading sakeko

            if(jsonObject == null)
            {
                status=1;
            }
            else
            {
                try
                {
                    if(jsonObject.getString("status").equals("success"))
                    {
                        status=2;
                    }
                    else
                    {
                        status=3;
                    }
                }
                catch (JSONException e)
                {
                    Toast.makeText(LoginActivity.this, ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(status==1)
            {
                Toast.makeText(LoginActivity.this, "Logining failed", Toast.LENGTH_SHORT).show();
            }
            else if(status==2)
            {
                Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
            }
            else if(status==3)
            {
                Toast.makeText(LoginActivity.this, "Wrong data", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

