package managers;

import java.io.Serializable;

public class Result implements Serializable {

    private String message;

    private boolean result;

    //private boolean

    public Result(String message, Boolean result){
        this.message = message;
        this.result = result;
    }
    public String getMessage(){
        return this.message;
    }

    public boolean getResult(){ return  this.result;}
}
