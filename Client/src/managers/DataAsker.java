package managers;

import data.Difficulty;
import exceptions.InvalidInputException;
import exceptions.InvalidInputRangeException;

public class DataAsker {

    private ConsoleManager consoleManager;
    public DataAsker(ConsoleManager consoleManager){
        this.consoleManager=consoleManager;
    }

    public String AskLabName(){
        String LabName="";
        while (true){
            try {
                LabName = consoleManager.input("Введите название лабораторной:");
                if (LabName==null){
                    throw new InvalidInputException("Название лабораторной не может быть пустым");
                    //consoleManager.print("Название лабораторной не может быть пустым");
                }
                else break;
            }
            catch (InvalidInputException ex){
                consoleManager.print(ex.getMessage());
            }

        }
        return LabName;
    }
    public int AskX(){
        int X;
        while (true){
            try {
                String x= consoleManager.input("Введите X:");
                if (x==null){
                    throw new InvalidInputException("X не может быть пустым");
                }
                else {
                    X = Integer.parseInt(x);
                    break;
                }
            }
            catch (InvalidInputException ex){
                consoleManager.print(ex.getMessage());
            }
            catch (NumberFormatException ex) {
                consoleManager.print("Неправильные формат данных");
            }
        }
        return X;
    }
    public Integer AskY(){
        Integer Y;
        while (true){
            try {
                String y= consoleManager.input("Введите Y:");
                if (y==null){
                    throw new InvalidInputException("Y не может быть пустым");
                }
                else {
                    Y = Integer.parseInt(y);
                    break;
                }
            }
            catch (InvalidInputException ex){
                consoleManager.print(ex.getMessage());
            }

            catch (NumberFormatException ex) {
                consoleManager.print("Неправильные формат данных");
            }
        }
        return Y;
    }
    public float AskPoint(){
        float Point;
        while (true){
            try {

                String point= consoleManager.input("Введите Кол-во баллов:");
                if (point==null){
                    throw new InvalidInputException("Point не может быть пустым");
                }
                else {
                    Point = Float.parseFloat(point);
                    if(Point<=0){
                        throw new InvalidInputRangeException("Кол-во баллов не может быть меньше или ровно 0");
                        //consoleManager.print("Кол-во баллов не может быть меньше 0");
                    }
                    else break;
                }

            }
            catch (InvalidInputException ex){
                consoleManager.print(ex.getMessage());
            }
            catch (InvalidInputRangeException ex){
                consoleManager.print(ex.getMessage());
            }
            catch (NumberFormatException ex) {
                consoleManager.print("Неправильный формат данных");
            }
        }
        return Point;
    }
    public Difficulty AskDifficult(){
        Difficulty Difficult = Difficulty.HARD;
        while (true){
            try {
                consoleManager.print("Введите сложность данной лабы:");
                String difficult = consoleManager.input("VERY_EASY, HARD, IMPOSSIBLE, TERRIBLE: ");
                if (difficult==null){
                    throw new InvalidInputException("Сложность не может быть пустой");
                    //consoleManager.print("Сложность не может быть пустой");
                }
                else {
                    boolean flag=true;
                    difficult=difficult.toUpperCase();
                    switch (difficult){
                        case "VERY_EASY": Difficult = Difficulty.VERY_EASY; break;
                        case "HARD": Difficult = Difficulty.HARD; break;
                        case "IMPOSSIBLE": Difficult = Difficulty.IMPOSSIBLE; break;
                        case "TERRIBLE": Difficult = Difficulty.TERRIBLE; break;
                        default:

                            //consoleManager.print("Введена неверная сложность");
                            flag=false;
                            throw new InvalidInputException("Введена неверная сложность");
                            //break;
                    }
                    if (flag) break;
                }
            }
            catch (InvalidInputException ex){
                consoleManager.print(ex.getMessage());
            }

        }
        return Difficult;
    }
    public String AskPersonName(){
        String PersonName="";
        while (true){
            try {
                PersonName = consoleManager.input("Введите имя студента");
                if (PersonName==null){
                    throw new InvalidInputException("Имя не может быть пустым");
                    //consoleManager.print("Имя не может быть пустым");
                }
                else break;
            }
            catch (InvalidInputException ex){
                consoleManager.print(ex.getMessage());
            }

        }
        return PersonName;
    }
    public long AskHeight(){
        long Height;
        while (true){
            try {
                String height= consoleManager.input("Введите рост студента:");
                if (height==null){
                    throw new InvalidInputException("Рост не может быть пустым");
                }
                Height = Long.parseLong(height);
                if(Height<=0){
                    throw new InvalidInputRangeException("Рост не может быть меньше или ровно  0");
                    //consoleManager.print("Рост не может быть меньше 0");
                }
                else break;
            }
            catch (InvalidInputRangeException ex){
                consoleManager.print(ex.getMessage());
            }
            catch (InvalidInputException ex){
                consoleManager.print(ex.getMessage());
            }
            catch (NumberFormatException ex) {
                consoleManager.print("Неправильный формат данных");
            }
        }
        return Height;
    }
    public Integer AskWeight(){
        Integer Weight;
        while (true){
            try {
                String weight = consoleManager.input("Введите вес студента:");
                if (weight==null){
                    throw new InvalidInputException("Вес не может быть пустым");
                }
                Weight = Integer.parseInt(weight);
                if (Weight <= 0) {
                    throw new InvalidInputRangeException("Вес не может быть меньше или ровно 0");
                    //consoleManager.print("Вес не может быть меньше 0");
                } else break;
            }
            catch (InvalidInputRangeException ex){
                consoleManager.print(ex.getMessage());
            }
            catch (InvalidInputException ex){
                consoleManager.print(ex.getMessage());
            }
            catch (NumberFormatException ex) {
                consoleManager.print("Неправильный формат данных");
            }

        }
        return Weight;
    }
}
