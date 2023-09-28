package data;

import java.io.Serializable;
import java.time.ZonedDateTime;

public class LabWork implements Comparable<LabWork>, Serializable {
    private long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private static long StaticId=0;
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Float minimalPoint; //Поле не может быть null, Значение поля должно быть больше 0
    private Difficulty difficulty; //Поле не может быть null
    private Person author; //Поле не может быть null
    public LabWork(String name, Coordinates coordinates, Float minimalPoint, Difficulty difficulty, Person author){
        id=StaticId;
        StaticId+=1;
        this.name=name;
        this.coordinates =coordinates;
        this.creationDate=ZonedDateTime.now();
        this.minimalPoint=minimalPoint;
        this.difficulty=difficulty;
        this.author=author;
    }

    public long getId() {return id;}

    public static void reduceId(){ StaticId-=1;}

    public String getName() {return name;}

    public Coordinates getCoordinates() {return coordinates;}

    public ZonedDateTime getCreationDate() {return this.creationDate;}

    public Float getMinimalPoint() {return minimalPoint;}

    public Difficulty getDifficulty() {return difficulty;}

    public Person getAuthor() {return author;}

    public void setId(long id){
        this.id=id;
    }

    public void setName(String name){
        this.name=name;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public void setMinimalPoint(Float minimalPoint) {
        this.minimalPoint = minimalPoint;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public void setAuthor(Person author) {
        this.author = author;
    }

    public String toString(){
        return  "Лаба:\n" +
                "Id: " + this.id + "\n" +
                "   Name: " +this.name+"\n" +
                "   Coordinates:\n" +
                "   X: "+ this.coordinates.getX()+"\n" +
                "   Y: " + this.coordinates.getY()+"\n" +
                "   Баллы за лабу: " + this.minimalPoint +"\n" +
                "   Сложность: " + this.difficulty + "\n" +
                "   Студент:\n" +
                "   Имя: "+ this.author.getName() + "\n" +
                "   Рост: "+ this.author.getHeight() + "\n" +
                "   Вес:"+ this.author.getWeight()+"\n";
    }
    @Override
    public int compareTo(LabWork labWork){
        //return (int)id;
        //int result = this.id.compareTo(labWork.id);
        //return result;
        int result = Long.compare( this.id, labWork.getId());
        return result;
    }
}
