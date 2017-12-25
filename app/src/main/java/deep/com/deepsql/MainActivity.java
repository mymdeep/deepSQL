package deep.com.deepsql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import deep.com.deepsql.interfaces.SqlInterface;
import deep.com.deepsql.log.C;
import deep.com.deepsql.log.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DeepSQL.getInstance().sqlInterface = new SqlInterface() {
            @Override
            public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
                Logger.single(C.E,"onUpgrade myself");
            }

            @Override
            public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                Logger.single(C.E,"onUpgrade myself");
            }
        };
        DeepSQL.getInstance().init(getApplication(),"demo.db",1);
        findViewById(R.id.c_create).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                DeepSQL.getInstance().create(Person.class);

            }
        });
        findViewById(R.id.m_create).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String,Object> map = new HashMap<String, Object>();
                map.put("name","dog");
                map.put("age",16);
                DeepSQL.getInstance().create("animal",map);

            }
        });
        findViewById(R.id.j_create).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                DeepSQL.getInstance().create(MainActivity.this, "names.json");


            }
        });
        findViewById(R.id.j_insert).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject jsonObject = new JSONObject();
                try {
                    Random random = new Random();
                    int age = random.nextInt();
                    jsonObject.put("name","jim");
                    jsonObject.put("age",age);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                DeepSQL.getInstance().insert("person",jsonObject);
                DeepSQL.getInstance().selectAll("person");
            }
        });
        findViewById(R.id.m_insert).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {


                    Random random = new Random();
                    int age = random.nextInt();
                    HashMap map = new HashMap();
                    map.put("name","cat");
                    map.put("age",age);

                DeepSQL.getInstance().insert("animal",map);
                DeepSQL.getInstance().selectAll("animal");
            }
        });
        findViewById(R.id.c_insert).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                Random random = new Random();
                int age = random.nextInt(100);
               Person person  = new Person();
                person.setName("john");
                person.setAge(age);

                DeepSQL.getInstance().insert(person);


            }
        });
        findViewById(R.id.c_select).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Object> list =  DeepSQL.getInstance().selectObjectsBySQL(Person.class,"select * from person where id = 5");
                for (Object o:list){
                    Person p = (Person)o;
                    Logger.mutlInfo(C.E,p.getName(),p.getAge()+"");
                }

            }
        });
        findViewById(R.id.j_select).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                JSONArray array =  DeepSQL.getInstance().selectJsonArryBySQL("select * from person where id = 5");
                Logger.jsonArry(array);

            }
        });
        findViewById(R.id.j_update).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                JSONObject jsonObject = new JSONObject();
                Random random = new Random();
                int age = random.nextInt();
                try {
                    jsonObject.put("name","jim");
                    jsonObject.put("age",age);
                    DeepSQL.getInstance().update("person","id=?",new String[]{"5"},jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        findViewById(R.id.c_update).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Random random = new Random();
                int age = random.nextInt(100);
                Person person  = new Person();
                person.setName("john");
                person.setAge(age);

                DeepSQL.getInstance().update("person","id=?",new String[]{"5"},person);
            }
        });
        findViewById(R.id.s_update).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Random random = new Random();
                int age = random.nextInt(100);
                Person person  = new Person();
                person.setName("john");
                person.setAge(age);


                DeepSQL.getInstance().update(person,"name");
            }
        });
        findViewById(R.id.a_del).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                 DeepSQL.getInstance().del("person","id=?",new String[]{"6"});


            }
        });
        findViewById(R.id.c_del).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Person person  = new Person();
                person.setName("jim");

                DeepSQL.getInstance().del(person,"name");


            }
        });
        findViewById(R.id.a_deltable).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                DeepSQL.getInstance().dropTable("person");


            }
        });
    }
}
