package Model.Exceptions;

public class SlotOccupiedException extends Exception{

    public SlotOccupiedException(){
        super("The slot is occupied by a dome or another worker.");
    }
}