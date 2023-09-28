package data;

import java.io.Serializable;

public class Person implements Serializable {
    private String name; //Поле не может быть null, Строка не может быть пустой
    //private java.time.LocalDate birthday; //Поле не может быть null
    private long height; //Значение поля должно быть больше 0
    private Integer weight; //Поле не может быть null, Значение поля должно быть больше 0
    public Person(String name, long height, Integer weight){
        this.name=name;
        //this.birthday=birthday;
        this.height=height;
        this.weight=weight;
    }

    public String getName() {
        return this.name;
    }

    public long getHeight() {
        return height;
    }

    public Integer getWeight() {
        return weight;
    }
}
