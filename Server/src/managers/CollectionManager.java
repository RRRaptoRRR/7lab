package managers;

import data.LabWork;

import java.time.LocalDate;
import java.util.TreeSet;

public class CollectionManager {
    private TreeSet<LabWork> collection;
    private  LocalDate initDate;
    private static long currentId = 1;

    public CollectionManager(){
        this.collection=new TreeSet<>();
        this.initDate=LocalDate.now();
    }
    public TreeSet<LabWork> getCollection() {
        return collection;
    }
    public int getSize(){
        return collection.size();
    }

    public LocalDate getInitDate() {
        return initDate;
    }

    public long getCurrentId() {
        return currentId;
    }

    public void add(LabWork labWork){
        collection.add(labWork);
        currentId+=1;
    }
    /*public void update(float id){
        if (getSize()){
            for (LabWork labWork:getCollection()){
                if (labWork.getId()==id){

                }
            }
            //return true;
        }
        else{
            consoleManager.print("Коллекция пуста");
            return false;
        }
        //return true;
    }*/
    public void remove(LabWork labWork){
        this.collection.remove(labWork);
    }
    public void clear(){
        this.collection.clear();
    }
    public float getMinByPoints(){
        float min=Float.MAX_VALUE;
        for(LabWork labWork: this.collection){
            if (labWork.getMinimalPoint()<min){
                min = labWork.getMinimalPoint();
            }
        }
        return min;
    }
    public float getMaxByPoints(){
        float max=Float.MIN_VALUE;
        for(LabWork labWork: this.collection){
            if (labWork.getMinimalPoint()>max){
                max = labWork.getMinimalPoint();
            }
        }
        return max;
    }
}
