package deep.com.deepsql.interfaces;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by wangfei on 2017/12/10.
 */

public interface SqlInterface {
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion);
}
