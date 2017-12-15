package deep.com.deepsql.utils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import deep.com.deepsql.log.C;
import deep.com.deepsql.log.Logger;

import static android.R.attr.name;

/**
 * Created by wangfei on 2017/12/9.
 */

public class DBUtils {
    public static String getTableName(Class<?> clazz){
            return clazz.getSimpleName();
    }
    public static String getColumnType(String type) {

        type = type.toLowerCase();
        String value = " text ";
        if (type.contains("string")) {
            value = " text ";
        } else if (type.contains("int")) {
            value = " integer ";
        } else if (type.contains("integer")) {
            value = " integer ";
        } else if (type.contains("boolean")) {
            value = " boolean ";
        } else if (type.contains("float")) {
            value = " float ";
        } else if (type.contains("double")) {
            value = " double ";
        } else if (type.contains("char")) {
            value = " varchar ";
        } else if (type.contains("long")) {
            value = " long ";
        }
        return value;
    }
    public static void setField(Field fd, Object object, String type, Cursor c) throws IllegalAccessException {
        if (object == null){
            return;
        }
        String name = fd.getName();
        type = type.toLowerCase();
        String value = " text ";
        if (type.contains("string")) {
           fd.set(object,c.getString(c.getColumnIndex(name)));
        } else if (type.contains("int")) {
            fd.set(object,c.getInt(c.getColumnIndex(name)));
        } else if (type.contains("integer")) {
            fd.set(object,c.getInt(c.getColumnIndex(name)));
        } else if (type.contains("boolean")) {
            fd.set(object,c.getString(c.getColumnIndex(name)));
        } else if (type.contains("float")) {
            fd.set(object,c.getFloat(c.getColumnIndex(name)));
        } else if (type.contains("double")) {
            fd.set(object,c.getDouble(c.getColumnIndex(name)));
        } else if (type.contains("char")) {
            fd.set(object,c.getString(c.getColumnIndex(name)));
        } else if (type.contains("long")) {
            fd.set(object,c.getLong(c.getColumnIndex(name)));
        }

    }
    public static void setContentValue(ContentValues values,Object object,String key)  {
        if (object == null){
            return;
        }
        String type = object.getClass().getSimpleName();
        String value = " text ";
        if (type.contains("string")) {
           values.put(key,(String)object);
        } else if (type.contains("int")) {
            values.put(key,(Integer)object);
        } else if (type.contains("integer")) {
            values.put(key,(Integer)object);
        } else if (type.contains("boolean")) {
            values.put(key,(Boolean)object);
        } else if (type.contains("float")) {
            values.put(key,(Float)object);
        } else if (type.contains("double")) {
            values.put(key,(Double)object);
        } else if (type.contains("char")) {
            values.put(key,(String)object);
        } else if (type.contains("long")) {
            values.put(key,(Long)object);
        }

    }
    public static String readAssetsJson(Context context, String fileName){
        try {
            //Return an AssetManager instance for your application's package
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            // Read the entire asset into a local byte buffer.
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            // Convert the buffer into a string.
            String text = new String(buffer, "utf-8");
            // Finally stick the string into the text view.
            return text;
        } catch (Throwable e) {
            Logger.single(C.E,"readAssetsJson error:"+e.getMessage());
        }
        return "读取错误，请检查文件名";
    }
}
