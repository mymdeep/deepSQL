# 序言
这个sql的辅助库主要是为了方便开发者使用数据库，之后还会拓展新的功能，欢迎大家提出宝贵意见。
# 初始化

```java
DeepSQL.getInstance().init(getApplication(),"demo.db",1);
```
第一个参数是Application
第二个参数为数据库名字
第三个参数为版本号。

# 建表
## 使用类建表
我们经常会将数据库内的数据转成一个modal类型，如果可以使用这个类来建表，岂不是很方便。
例如，我们有一个类：

```java
public class Person implements Serializable{
    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
```


那么根据这个类建表可以使用：

```java
 DeepSQL.getInstance().create(Person.class);
```

>其中表名会使用类名


## map建表
也有时我们需要根据一个map建表：

```java
HashMap<String,Object> map = new HashMap<String, Object>();
map.put("name","dog");
map.put("age",16);
DeepSQL.getInstance().create("animal",map);
```
其中第一个参数为表名
## 文件建表
如果以上方式都不需要，也可以使用asset中json建表的方式：
在assets文件夹中放一个json文件

```json
{
  "name1":"String",
  "name2":"int",
  "name3":"boolean",
  "name4":"float",
  "name5":"double",
  "name6":"long"
}
```

然后调用:

```java
  DeepSQL.getInstance().create(MainActivity.this, "names.json");
```

>表的名为会以json的文件名命名

## json建表
如果不习惯使用assets中的这种json建表方式，也可以直接使用json：

```java
  DeepSQL.getInstance().create(MainActivity.this, json);
```

# 插入
## json插入
```
  DeepSQL.getInstance().insert("person",jsonObject);
```
* 第一个参数为表名
* 第二个参数为插入的json

## map 插入

```
 DeepSQL.getInstance().insert("animal",map);
```
* 第一个参数为表名
* 第二个参数为插入的map

## 实例化插入

```
 Person person  = new Person();
person.setName("john");
person.setAge(age);
DeepSQL.getInstance().insert(person);
```

# 数据库查询
## 查询所有

```
ArrayList<Object> list =  DeepSQL.getInstance().selectObjects(Person.class,"person");
```
* 第一个参数为类
* 第二个参数为表名

## 根据条件返回Json

```
 JSONArray array =  DeepSQL.getInstance().selectJsonArryBySQL("select * from person where id = 5");
```
## 根据条件返回Object

```
  ArrayList<Object> list =  DeepSQL.getInstance().selectObjectsBySQL(Person.class,"select * from person where id = 5");

```

# 更新
## json更新

```
   DeepSQL.getInstance().update("person","id=?",new String[]{"5"},jsonObject);
```

## object更新

```
    DeepSQL.getInstance().update("person","id=?",new String[]{"5"},person);
```

# 删除
```
 DeepSQL.getInstance().del("person","id=?",new String[]{"6"});
```
# 删除表
```
  DeepSQL.getInstance().dropTable("person");
```
# 直接执行sql语句
## 非查询

```
DeepSQL.getInstance().exec("sql");
```
* 参数为SQL语句

### 查询

```
Cursor c =DeepSQL.getInstance().queryBySQL("sql");
```

* 参数为SQL语句
# 数据库升级或降级处理

```
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
```
>请注意该方法需要在init之前调用。


# 特别说明
如果有新的需求可以报给我补充，谢谢。


