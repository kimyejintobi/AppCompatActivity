package kr.hs.emirim.petto99.dbex;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

    public class MainActivity extends AppCompatActivity {
        EditText editname, editCount,editResultName,editResultCount;
        Button butInit, butInsert,butSelect,butupdate,butdelete;
        MyDBHelper myHelper;
        SQLiteDatabase sqlDb;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            editname = (EditText) findViewById(R.id.edit_group_name);
            editCount = (EditText) findViewById(R.id.edit_group_count);
            editResultName = (EditText) findViewById(R.id.edit_result_name);
            editResultCount = (EditText) findViewById(R.id.edit_result_count);
            butInit = (Button) findViewById(R.id.but_init);
            butInsert = (Button) findViewById(R.id.but_insert);
            butSelect = (Button) findViewById(R.id.but_select);
            butupdate= (Button) findViewById(R.id.but_update);
            butdelete= (Button) findViewById(R.id.but_delete);


            //DB 생성
            myHelper=new MyDBHelper(this);
            //기존의 테이블이 존재하면 삭제하고 테이블을 새로 생성한다.
            butInit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sqlDb=myHelper.getWritableDatabase();
                    myHelper.onUpgrade(sqlDb,1,2);
                    sqlDb.close();
                }
            });
            butInsert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sqlDb = myHelper.getWritableDatabase();
                    String sql="insert into idolTable values('"+editname.getText()+"',"+editCount.getText()+")";
                    sqlDb.execSQL(sql);
                    sqlDb.close();
                    Toast.makeText(MainActivity.this,"저장됨",Toast.LENGTH_LONG).show();
                }
            });
            butSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sqlDb=myHelper.getReadableDatabase();
                    String sql = "select * from idolTable";
                    Cursor cursor = sqlDb.rawQuery(sql, null);
                    String names = " Idol 이름 "+"\r\n"+"============"+"\r\n";
                    String counts = " Idol 인원수 "+"\r\n"+"============"+"\r\n";
                    while(cursor.moveToNext()){
                        names += cursor.getString(0)+"\r\n";
                        counts += cursor.getString(1)+"\r\n";
                    }
                    editResultName.setText(names);
                    editResultCount.setText(counts);
                    cursor.close();
                    sqlDb.close();

                }
            });
            butupdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sqlDb = myHelper.getWritableDatabase();
                    String sql="update idolTable set idolCount"+editCount.getText()+"where idolName'"+editname.getText()+"'";
                    sqlDb.execSQL(sql);
                    sqlDb.close();
                    Toast.makeText(MainActivity.this,"수정됨",Toast.LENGTH_LONG).show();
                }
            });
            butdelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sqlDb = myHelper.getWritableDatabase();
                    String sql = "delete from idoltable where idolName ='" + editname.getText() + "'";
                    sqlDb.execSQL(sql);
                    sqlDb.close();
                    Toast.makeText(MainActivity.this,"삭제됨",Toast.LENGTH_LONG).show();
                }
            });

        }

        class MyDBHelper extends SQLiteOpenHelper {
            //idolDB라는 이름의 데이터베이스가 생성된다.
            public MyDBHelper(Context context){
                super(context, "idolDB", null,1);
            }
            //idolTable이라는 이름의 테이블 생성
            @Override
            public void onCreate(SQLiteDatabase db) {
                String sql ="create table idolTable(idolName text not null primary key, idolCount integer)";
                db.execSQL(sql);
            }
            //이미 idolTable이 존재한다면 기존의 테이블을 삭제하고 새로 테이블 만들 때 호출
            @Override
            public void onUpgrade(SQLiteDatabase db, int i, int i1) {
                String sql ="drop table if exists idolTable";
                db.execSQL(sql);
                onCreate(db);
            }
        }

    }
