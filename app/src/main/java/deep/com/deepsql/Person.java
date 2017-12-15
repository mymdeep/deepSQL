package deep.com.deepsql;

import java.io.Serializable;

/**
 * Created by wangfei on 2017/12/9.
 */

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
