package deep.com.deepsql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import deep.com.deepsql.interfaces.SqlInterface;

/**
 * Created by wangfei on 2017/12/9.
 */

public class MySql extends SQLiteOpenHelper {
    /**当数据库不存在时，就会创建数据库，然后打开数据库，再调用onCreate 方法来执行创建表之类的操作。
     * 当数据库存在时，SQLiteOpenHelper 就不会调用onCreate方法了，
     * 若传入的版本号高于当前的，就会执行onUpgrade()方法来更新数据库和版本号。
     * @param context
     * @param name 表示数据库文件名（不包括文件路径,默认路径是/data/data/包名/databases），SQLiteOpenHelper类会根据这个文件名来创建数据库文件。
     * @param factory 数据库进行查询的时候会返回一个cursor，这个cursor就是actory中产生的，factory可以进行自定义
     * @param version 表示数据库的版本号。如果当前传入的数据库版本号比上一次创建的版本高，SQLiteOpenHelper就会调用onUpgrade()方法。
     */
    private SqlInterface sqlInterface;
    public MySql(Context context, String name,
                 CursorFactory factory, int version, SqlInterface sqlInterface) {
        super(context, name, factory, version);
        this.sqlInterface = sqlInterface;

    }

    /**当数据库首次创建时执行该方法，一般将创建表等初始化操作放在该方法中执行.
     *
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    /**当打开数据库时传入的版本号与当前的版本号不同时会调用该方法
     * @param sqLiteDatabase
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqlInterface.onUpgrade(sqLiteDatabase,oldVersion,newVersion);
    }
}
