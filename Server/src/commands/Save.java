package commands;

import data.LabWork;
import managers.CollectionManager;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class Save {
    private CollectionManager collectionManager;
    public Save(CollectionManager collectionManager){
        this.collectionManager=collectionManager;
    }


    public void execute(String args) {
        try {
            FileWriter csvWriter = new FileWriter(args);
            csvWriter.append("id,labname,x,y,date,points,difficult,studname,height,weight");
            csvWriter.append("\n");
            for (LabWork labWork:collectionManager.getCollection()){
                csvWriter.append(Long.toString(labWork.getId()));
                csvWriter.append(",");
                csvWriter.append(labWork.getName());
                csvWriter.append(",");
                csvWriter.append(Integer.toString(labWork.getCoordinates().getX()));
                csvWriter.append(",");
                csvWriter.append(labWork.getCoordinates().getY().toString());
                csvWriter.append(",");
                csvWriter.append(labWork.getCreationDate().toString());
                csvWriter.append(",");
                csvWriter.append(labWork.getMinimalPoint().toString());
                csvWriter.append(",");
                csvWriter.append(labWork.getDifficulty().toString());
                csvWriter.append(",");
                csvWriter.append(labWork.getAuthor().getName());
                csvWriter.append(",");
                csvWriter.append(Long.toString(labWork.getAuthor().getHeight()));
                csvWriter.append(",");
                csvWriter.append(labWork.getAuthor().getWeight().toString());
                csvWriter.append("\n");
            }
            csvWriter.flush();
            csvWriter.close();
        }
        catch (FileNotFoundException ex){
            System.out.println("Указан неверный путь");
        }
        catch (IOException ex) {
            System.out.println("*_*");
        }
    }
}
