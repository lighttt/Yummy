package com.yummy.test.yummy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yummy.test.yummy.helper.Constants;
import com.yummy.test.yummy.parser.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by User on 3/20/2018.
 */

public class SignUpActivity extends AppCompatActivity {

    EditText mEmail,mName,mPhone,mAddress,mPassword;
    Button mRegister;
    String name,phone,email,password,address;
    ProgressDialog mRegistering;
    JSONObject jsonObject;
    JsonParser jsonParser = new JsonParser();
    int status = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mName = (EditText)findViewById(R.id.activity_signup_et_name);
        mEmail = (EditText)findViewById(R.id.activity_signup_et_email);
        mPassword = (EditText)findViewById(R.id.activity_signup_et_password);
        mAddress = (EditText)findViewById(R.id.activity_signup_et_address);
        mPhone = (EditText)findViewById(R.id.activity_signup_et_phone);
        mRegister = (Button)findViewById(R.id.activity_signup_btn_register);

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = mEmail.getText().toString();
                String password = mPassword.getText().toString();

                if(TextUtils.isEmpty(username)||TextUtils.isEmpty(password))
                {
                    Toast.makeText(SignUpActivity.this,"Email or Password is empty",Toast.LENGTH_SHORT).show();
                }

                else
                {
                    Toast.makeText(SignUpActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                    new PerformRegister().execute();
                }
            }
        });


    }
    class PerformRegister extends AsyncTask<String,String,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mRegistering = new ProgressDialog(SignUpActivity.this);
            mRegistering.setMessage("Logining in...");
            mRegistering.setCancelable(false);//loading hunda cancel garne ki nai
            mRegistering.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> hashMap = new HashMap<>();//hashmap ma key ra value dubai string ma
            hashMap.put("name",name);
            hashMap.put("email",email);//postman ma bhako key
            hashMap.put("password",password);
            hashMap.put("address",address);
            hashMap.put("phone",phone);

            String url = Constants.BASE_URL+ "api/login";

            jsonObject = jsonParser.performPostCI(url,hashMap);
            mRegistering.dismiss();//loading sakeko

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
                    Toast.makeText(SignUpActivity.this, ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(status==1)
            {
                Toast.makeText(SignUpActivity.this, "Registering failed", Toast.LENGTH_SHORT).show();
            }
            else if(status==2)
            {
                Toast.makeText(SignUpActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SignUpActivity.this,MainActivity.class));
            }
            else if(status==3)
            {
                Toast.makeText(SignUpActivity.this, "Wrong data", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
