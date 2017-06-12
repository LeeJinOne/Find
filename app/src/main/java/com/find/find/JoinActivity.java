package com.find.find;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class JoinActivity extends AppCompatActivity {
    private Context context;

    DataHandler handler;

    EditText editID, editPWD, editPWD2, editName, editPho;
    TextView checkID, checkPWD;
    Button idCheckBtn, joinBtn, cancelBtn;

    Boolean canID, canPWD = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        handler = new DataHandler();

        context = this;

        editID = (EditText)findViewById(R.id.editID);
        editPWD = (EditText)findViewById(R.id.editPWD);
        editPWD2 = (EditText)findViewById(R.id.editPWD2);
        editName = (EditText)findViewById(R.id.editName);
        editPho = (EditText)findViewById(R.id.editPho);

        checkID = (TextView)findViewById(R.id.checkID);
        checkPWD = (TextView)findViewById(R.id.checkPWD);

        idCheckBtn = (Button)findViewById(R.id.idCheckBtn);
        joinBtn = (Button)findViewById(R.id.joinBtn);
        cancelBtn = (Button)findViewById(R.id.cancelBtn);

        editPWD.addTextChangedListener(textWatcher);
        editPWD2.addTextChangedListener(textWatcher);

        idCheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new JinoneHttpTask(handler).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, ".php",
                        "id="+editID.getText().toString());
            }
        });

        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new JinoneHttpTask(handler).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "d_join_do.php",
                        "id="+editID.getText().toString()+"&pwd="+editPWD.getText().toString()
                        +"&name="+editName.getText().toString()+"&number="+editPho.getText().toString());
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, LoginActivity.class));
            }
        });
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String pwd = editPWD.getText().toString();
            String pwd2 = editPWD2.getText().toString();

            if(pwd.equals(pwd2)){
                checkPWD.setText("you can use this password.");
                checkPWD.setTextColor(Color.GREEN);
                canPWD = true;
            } else {
                checkPWD.setText("please, check your password");
                checkPWD.setTextColor(Color.RED);
                canPWD = false;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    class DataHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            String result = msg.obj.toString().trim();
            Log.e("", "result =" + result);

            String[] strs = result.split("\\|"); // 결과값을 split 으로 나눈다.
            for (String str : strs) {
                Log.e("!!!!!", "str = " +str); //str 배열에는 값이 2개
            }

            switch (strs[0]) {
                case "d_id_check_do.php":
                    if(strs[1].equals("fail")){
                        checkID.setText("사용가능한 아이디 입니다.");
                        checkID.setTextColor(Color.GREEN);
                        canID = true;
                    } else {
                        checkID.setText("중복된 아이디 입니다.");
                        checkID.setTextColor(Color.RED);
                        canID = false;
                    }
                    break;
                case "d_join_do.php":
                    /*if(!canID) { //TODO: 여기서 오류가 남 boolean 값 오류라는데?
                        Toast.makeText(context, "아이디가 중복됩니다.", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (!canPWD) {
                        Toast.makeText(context, "비밀번호가 동일하지 않습니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }*/
                    switch (strs[1]) {
                        case "fail":
                            Toast.makeText(context, "가입에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                            break;
                        case "success":
                            Toast.makeText(context, "가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(context, LoginActivity.class));
                            break;
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
