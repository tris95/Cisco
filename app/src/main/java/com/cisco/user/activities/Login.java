package com.cisco.user.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cisco.user.R;
import com.cisco.user.models.User;
import com.cisco.user.models.Value;
import com.cisco.user.services.APIServices;
import com.cisco.user.utils.Utilities;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity {
    EditText id, password;
    TextView signup;
    Button signin;
    ProgressDialog pDialog;
    String strId, strPassword, token;
    LinearLayout llsignin;
    //static String TAG = "SignUpActivity";
    boolean intPassword;
    Utilities util = new Utilities();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        id = findViewById(R.id.ediduser);
        password = findViewById(R.id.edpassword);
        signup = findViewById(R.id.edsignup);
        signin = findViewById(R.id.btnsignin);
        llsignin = findViewById(R.id.llsignin);
        //token = FirebaseInstanceId.getInstance().getToken();
//        while (token==null) {
//            if (token == null) {
//                token = FirebaseInstanceId.getInstance().getToken();
//            }
//        }

        requestFocus(id);

        llsignin.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                assert inputMethodManager != null;
                inputMethodManager.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), 0);
            }
        });
        password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //final int DRAWABLE_LEFT = 0;
                //final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                //final int DRAWABLE_BOTTOM = 3;

                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (motionEvent.getRawX() >= (password.getRight() - password.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        int selectionStart = password.getSelectionStart();
                        int selectionEnd = password.getSelectionEnd();
                        if (intPassword) {
                            password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            password.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off_black_24dp, 0);
                        } else {
                            password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            password.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_black_24dp, 0);
                        }
                        password.setSelection(selectionStart, selectionEnd);
                        intPassword = !intPassword;
                    }
                }
                return false;
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strId = id.getText().toString();
                strPassword = password.getText().toString();
                if (strId.equals("") || strPassword.equals("")) {
                    Snackbar.make(view, "ID atau Password masih kosong", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else signin();
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Registrasi.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            }
        });

    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public void signin() {
        pDialog = new ProgressDialog(Login.this);
        pDialog.setTitle("Loading...");
        pDialog.setIndeterminate(false);
        pDialog.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utilities.getBaseURLUser())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIServices apiService = retrofit.create(APIServices.class);
        Call<Value<User>> call = apiService.signin("b5e5ac98-f40d-44b5-a5a9-0b3a37382b05", strId, strPassword, token);
        call.enqueue(new Callback<Value<User>>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(@NonNull Call<Value<User>> call, @NonNull Response<Value<User>> response) {
                pDialog.dismiss();
                if (response.body() != null) {
                    int success = Objects.requireNonNull(response.body()).getSuccess();
                    if (success == 1) {
                        List<User> user = Objects.requireNonNull(response.body()).getData();
                        if (user.size() != 0) {
                            util.setUser(Login.this, user.get(0));
                            Utilities.setLogin(Login.this);
                            startActivity(new Intent(Login.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                            finish();
                        }
                    } else if (success == 2) {
                        Utilities.showAsToast(Login.this, "Password salah");
                    } else if (success == 3) {
                        Utilities.showAsToast(Login.this, "NIM tidak terdaftar");
                    }
                } else {
                    Utilities.showAsToast(Login.this, "Tidak ada data");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Value<User>> call, @NonNull Throwable t) {
                System.out.println("Retrofit Error:" + t.getMessage());
                Utilities.showAsToast(Login.this, "Tidak Terhubung Dengan Server. Silahkan Coba Lagi!");
                pDialog.dismiss();
            }
        });
    }
    @Override
    protected void onResume() {
        if (getIntent().getStringExtra("nim") == null) {
            id.setText("");
            requestFocus(id);
        }else {
            id.setText(getIntent().getStringExtra("nim"));
            requestFocus(password);
        }

        super.onResume();
    }
}
