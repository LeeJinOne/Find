package com.find.find;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DBActivity extends AppCompatActivity {
    EditText editText,editText2;
    EditText addpk,addindex,addppl,order;
    TextView textView;
    Button button,button2,button3,button4;
    SQLiteDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db);

        editText = (EditText)findViewById(R.id.customedit);
        textView = (TextView)findViewById(R.id.logtext);
        button= (Button)findViewById(R.id.dbopenbtn);
        editText2 = (EditText)findViewById(R.id.tbledit);
        button2= (Button)findViewById(R.id.createtbl);
        addpk = (EditText)findViewById(R.id.addpk);
        addindex =(EditText)findViewById(R.id.addindex);
        addppl= (EditText)findViewById(R.id.addppl);
        button3=(Button)findViewById(R.id.addinforbtn);
        button4=(Button)findViewById(R.id.show);
        order=(EditText)findViewById(R.id.selectedit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String databaseName =  editText.getText().toString();
                openDatabase(databaseName);
            }


        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tableName =  editText2.getText().toString();
                createTable(tableName);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pk =  addpk.getText().toString().trim();
                String indexstr =  addindex.getText().toString().trim();
                String ppl =  addppl.getText().toString().trim();
                int index = -1;
                try {
                    Integer.parseInt(indexstr);
                    setDB();
                }catch (Exception e){}
                addinfor(pk,index,ppl);
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectData();
            }
        });

    }

    private void setDB(){
        SharedPreferences pref = getSharedPreferences("pref",MODE_PRIVATE);
        SharedPreferences.Editor editor=pref.edit();
        editor.putString("pk",addpk.getText().toString());
        editor.putString("index",addindex.getText().toString());
        editor.putString("ppl",addppl.getText().toString());
        editor.commit();
    }
    public void openDatabase(String databaseName){
        printin("openDatabase call in");
//   database= openOrCreateDatabase(databaseName, MODE_PRIVATE,null);
//    if(database!=null){
//        printin("database open");

        DatabaseHelper helper = new DatabaseHelper(this,databaseName,null,1);
        database= helper.getWritableDatabase();


    }




    public void createTable(String tableName){
        printin("createTable call in");

        if(database !=null){
            String sql = "create table "+tableName+ "(_id integer PRIMARY KEY autoincrement,pk text, seat integer, ppl text) ";
            database.execSQL(sql);
            printin("create table");
        }else{

            printin("once open database");
        }

    }
    public void addinfor(String pk,int seat,String ppl){
        printin("addinfor call in");
        String tableName  =  editText2.getText().toString();
        if(database!=null){
            String sql="insert into " +tableName+"(pk,seat,ppl) values(?,?,?)";
            Object[] params = {pk,seat,ppl};

            database.execSQL(sql,params);
            printin("insert infor success");
            setDB();
        }

    }
    public void selectData(){
        if(database!=null){
            String orderit =order.getText().toString();;
            Cursor cursor= database.rawQuery(orderit,null);
            printin("select data amount"+cursor.getCount());
            for(int i=0;i<cursor.getCount();i++){
                cursor.moveToNext();
                String pk=  cursor.getString(0);
                String seat =cursor.getString(1);
                String ppl = cursor.getString(2);
                printin("#"+ i +"->"+ pk +","+ seat +"," + ppl);
            }
            cursor.close();
        }

    }

    public  void printin(String data){
        textView.append(data +"\n");
    }
}

class DatabaseHelper extends SQLiteOpenHelper {


    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String tableName ="customer";

        String sql = "create table if not exists "+tableName+ "(_id integer PRIMARY KEY autoincrement,pk text, seat integer, ppl text) ";
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if(newVersion > 1){
            String tableName ="customer";
            db.execSQL("alter table if exists " +tableName);

        }

    }


}
