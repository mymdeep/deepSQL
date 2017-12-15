package deep.com.deepsql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import deep.com.deepsql.interfaces.SqlInterface;
import deep.com.deepsql.log.C;
import deep.com.deepsql.utils.DBConstant;
import deep.com.deepsql.utils.DBUtils;
import deep.com.deepsql.log.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by wangfei on 2017/12/9.
 */

public class DeepSQL {
    MySql mySql;
    SqlInterface sqlInterface;
    private static class LazyHolder {
        private static final DeepSQL INSTANCE = new DeepSQL();
    }
    private DeepSQL(){}
    public static final DeepSQL getInstance() {
        return LazyHolder.INSTANCE;
    }

    public void setSqlInterface(SqlInterface sqlInterface) {
        this.sqlInterface = sqlInterface;
    }

    public void init(Context context, String name, int version){
        if (sqlInterface ==null){
            sqlInterface = new SqlInterface() {
                @Override
                public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
                    Logger.single(C.E,"onUpgrade no SqlInterface");
                }

                @Override
                public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                    Logger.single(C.E,"onDowngrade no SqlInterface");
                }
            };
        }
        mySql = new MySql(context,name,null,version,sqlInterface);

    }
    public void init(Context context, String name, CursorFactory factory, int version){
        if (sqlInterface ==null){
            sqlInterface = new SqlInterface() {
                @Override
                public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
                    Logger.single(C.E,"onUpgrade no SqlInterface");
                }

                @Override
                public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                    Logger.single(C.E,"onDowngrade no SqlInterface");
                }
            };
        }
        mySql = new MySql(context,name,factory,version,sqlInterface);
    }
    public void create(Class<?> clazz){
        StringBuilder sb = new StringBuilder();
        String tabName = DBUtils.getTableName(clazz);
        sb.append(DBConstant.CREATE_TABLE).append(tabName).append(DBConstant.CREATE_BEGIN);
        Field[] fields = clazz.getDeclaredFields();
        for (Field fd : fields) {
            String fieldName = fd.getName();
            String fieldType = fd.getType().getName();
            if (!fieldName.equalsIgnoreCase(DBConstant._ID) &&! fieldName.equalsIgnoreCase(DBConstant.ID)) {
                sb.append(fieldName).append(DBUtils.getColumnType(fieldType)).append(", ");
            }
        }
       createStrBuild(sb);
    }
    private void createStrBuild(StringBuilder sb){
        int len = sb.length();
        sb.replace(len - 2, len, ")");
        Logger.single(C.E,sb.toString());
        exec(getW(),sb.toString());
    }
    public void create(String name, HashMap<String,Object> map){
        StringBuilder sb = new StringBuilder();
        sb.append(DBConstant.CREATE_TABLE).append(name).append(DBConstant.CREATE_BEGIN);
        for (String key:map.keySet()){
            if (!key.equalsIgnoreCase(DBConstant._ID) &&!key.equalsIgnoreCase(DBConstant.ID)) {
                Logger.single(C.E,map.get(key).getClass().getSimpleName());
                sb.append(key).append(DBUtils.getColumnType(map.get(key).getClass().getSimpleName())).append(", ");
            }
        }
        createStrBuild(sb);
    }
    public void create(Context context,String name){
        StringBuilder sb = new StringBuilder();
        String tabName = name.replace(".json","");
        sb.append(DBConstant.CREATE_TABLE).append(tabName).append(DBConstant.CREATE_BEGIN);
        String l = DBUtils.readAssetsJson(context,name);
        try {
            JSONObject jsonObject = new JSONObject(l);
            Iterator iterator = jsonObject.keys();
            while(iterator.hasNext()){
                String key = (String) iterator.next();
                String value = jsonObject.getString(key);
                if (!key.equalsIgnoreCase(DBConstant._ID) &&!key.equalsIgnoreCase(DBConstant.ID)) {
                    sb.append(key).append(DBUtils.getColumnType(value)).append(", ");
                }
            }
        } catch (JSONException e) {
          Logger.error("json create:"+e.getMessage());
        }

        createStrBuild(sb);
    }
    public void create(String name,JSONObject jsonObject){
        StringBuilder sb = new StringBuilder();
        sb.append(DBConstant.CREATE_TABLE).append(name).append(DBConstant.CREATE_BEGIN);
        try {

            Iterator iterator = jsonObject.keys();
            while(iterator.hasNext()){
                String key = (String) iterator.next();
                String value = jsonObject.getString(key);
                if (!key.equalsIgnoreCase(DBConstant._ID) &&!key.equalsIgnoreCase(DBConstant.ID)) {
                    sb.append(key).append(DBUtils.getColumnType(jsonObject.get(key).getClass().getSimpleName())).append(", ");
                }
            }
        } catch (JSONException e) {
            Logger.error("json create:"+e.getMessage());
        }

        createStrBuild(sb);
    }
    public void insert(String name, ContentValues values){
           insert(name,null,values);
    }
    public void insert(String name, String nullcol,ContentValues values){
        if (getW()!=null){
            getW().insert(name,nullcol,values);
        }
    }
    private StringBuilder createInsertBegin(String name){
        StringBuilder sql = new StringBuilder(DBConstant.INSERT_BEGIN);
        sql.append(name).append("(");
        return sql;
    }
    private void createInsertvalue(StringBuilder sql,ArrayList<Object> objects){
        for (Object o:objects){
            sql.append("?,");
        }
        sql.deleteCharAt(sql.length()-1);
        sql.append(")");
        if (getW() != null) {
            Logger.single(C.E,"insert sql:"+sql.toString());
            getW().execSQL(sql.toString(),objects.toArray());
        }
    }
    public void insert(String name, JSONObject jsonObject){
        Iterator iterator = jsonObject.keys();
        StringBuilder sql =createInsertBegin(name);

        ArrayList<Object> objects = new ArrayList<Object>();
        while(iterator.hasNext()){
            String key = iterator.next().toString();
            sql.append(key);
            if (iterator.hasNext()){
                sql.append(",");
            }else {
                sql.append(")");
            }
            try {
                objects.add( jsonObject.get(key));
            } catch (JSONException e) {
               Logger.error("insert ="+e.getMessage());
            }
        }
        sql.append(" values (");
        createInsertvalue(sql,objects);
    }
    public void insert(String name, HashMap<String,Object> map){
        StringBuilder sql = createInsertBegin(name);
        ArrayList<Object> objects = new ArrayList<Object>();
        for (String key:map.keySet()){
            sql.append(key).append(",");
            objects.add(map.get(key));
        }
        sql.deleteCharAt(sql.length()-1);
        sql.append(") values (");
        createInsertvalue(sql,objects);
    }
    public void insert(Serializable serializable){
        String tabName = DBUtils.getTableName(serializable.getClass());
        StringBuilder sql = createInsertBegin(tabName);
        ArrayList<Object> objects = new ArrayList<Object>();
        Field[] fields = serializable.getClass().getDeclaredFields();

        for (Field fd : fields) {
            String fieldName = fd.getName();
            String fieldType = fd.getType().getName();
            sql.append(fieldName).append(",");
            fd.setAccessible(true);
            try {
                Object object =fd.get(serializable);
                objects.add(object);

            } catch (IllegalAccessException e) {
                Logger.error("insert class:"+e.getMessage());
            }
        }

        sql.deleteCharAt(sql.length()-1);
        sql.append(") values (");
        createInsertvalue(sql,objects);

    }
    public void selectAll(String name){
        if (getR()!=null){
            Cursor cursor = getR().rawQuery("select * from "+name, null);
            while (cursor.moveToNext()) {
                String[] ss = new String[cursor.getColumnCount()];
                for (int i= 0 ;i<cursor.getColumnCount();i++){
                    ss[i] = cursor.getColumnName(i)+"  :   "+cursor.getString(i);
                }
               Logger.mutlInfo(C.E,ss);
            }
            cursor.close();
        }
    }
    public ArrayList<Object> selectObjectsBySQL(Class<?> clazz ,String sql){
        ArrayList<Object> objects = new ArrayList<Object>();
        if (getR()!=null){
            Cursor cursor = getR().rawQuery(sql, null);
            while (cursor.moveToNext()) {
                try {
                    Object object = clazz.newInstance();
                    Field[] fields = clazz.getDeclaredFields();
                    for (Field fd : fields) {
                        String fieldType = fd.getType().getName();
                        fd.setAccessible(true);
                        Logger.single(C.W,fd.getName());
                        DBUtils.setField(fd,object,fieldType,cursor);
                    }
                    objects.add(object);
                } catch (InstantiationException | IllegalAccessException e) {
                    Logger.error("selectObjects:"+e.getMessage());
                }

            }
            cursor.close();
        }

        return objects;
    }
    public JSONArray selectJsonArry( String name){
       return selectJsonArryBySQL("select * from "+name);
    }
    public JSONArray selectJsonArryBySQL(String sql){
        Logger.single(C.E,sql);
        JSONArray arry = new JSONArray();
        if (getR()!=null){
            Cursor cursor = getR().rawQuery(sql, null);
            while (cursor.moveToNext()) {
                try {
                    JSONObject jsonObject = new JSONObject();
                    for (int i= 0 ;i<cursor.getColumnCount();i++){
                        jsonObject.put(cursor.getColumnName(i),cursor.getString(i));
                    }
                    arry.put(jsonObject);
                }  catch (JSONException e) {
                    Logger.error("selectObjects:"+e.getMessage());
                }

            }
            cursor.close();
        }

        return arry;
    }
    public ArrayList<Object> selectObjects(Class<?> clazz ,String name){
        return selectObjectsBySQL(clazz,"select * from "+name);
    }
    public void update(String name,String sql,String[] whereArgs,JSONObject j){
        if (getR()!=null){
            Iterator iterator = j.keys();
            ContentValues contentValues = new ContentValues();
            while(iterator.hasNext()){
                String key = iterator.next().toString();
                DBUtils.setContentValue(contentValues,j.opt(key),key);
            }
            Logger.single(C.E,"contentValues ="+contentValues.size());
            getR().update(name, contentValues, sql, whereArgs);
        }
    }
    public void update(String name,String sql,String[] whereArgs,Serializable serializable){
        if (getR()!=null){
            ContentValues contentValues = new ContentValues();
            Field[] fields = serializable.getClass().getDeclaredFields();
            for (Field fd : fields) {
                String fieldName = fd.getName();
                String fieldType = fd.getType().getName();
                fd.setAccessible(true);
                try {
                    Object object =fd.get(serializable);
                    DBUtils.setContentValue(contentValues,object,fieldName);
                    getR().update(name, contentValues, sql, whereArgs);
                } catch (IllegalAccessException e) {
                    Logger.error("insert class:"+e.getMessage());
                }
            }

        }
    }
    public void del(String name,String sql,String[] whereArgs){
        if (getW()!=null){
            getW().delete(name,sql, whereArgs);
        }
    }
    public void dropTable(String name){
        if (getW()!=null){
            String sql ="DROP TABLE "+name;
            //执行SQL
            getW().execSQL(sql);
        }
    }
    private SQLiteDatabase getW(){
        if (mySql == null){
            return null;
        }
        return mySql.getWritableDatabase();
    }
    private SQLiteDatabase getR(){
        if (mySql == null){
            return null;
        }
        return mySql.getReadableDatabase();
    }
    private void exec(SQLiteDatabase sqLiteDatabase,String sql){
        if (sqLiteDatabase!=null){
            sqLiteDatabase.execSQL(sql);
        }
    }
    public void exec(String sql){
        if (getW()!=null){
            getW().execSQL(sql);
        }
    }
    public Cursor queryBySQL(String sql){
        if (getR()!=null){
            Cursor cursor = getR().rawQuery(sql, null);
            return cursor;
        }
        return null;
    }
    public void destory(){
        mySql.close();
    }

}
