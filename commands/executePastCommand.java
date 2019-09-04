package commands;
import java.util.ArrayList;
import driver.JShell;

/**
 * Redo a past command that is present in history
 * 
 */
public class executePastCommand extends Command{
  public executePastCommand() {
    this.par = 1;
    this.cmd = "executePastCommand";
    this.desc = "This command will recall any of previous history by its "
        + "number(>=1) preceded by an exclamation point(!)";
  }
  
  /* Execution of executePastCommand Command
   * @param String[]
   * @param JShell
   * @return String
   * @throws CommandError
   */
  public String execute(String[] parameters, JShell JShell) throws CommandError{
    try {
    //Converts the number to an int and gets the history list 
    int lastHistoryRedo = Integer.parseInt(parameters[0].substring(1));
    ArrayList<String> historyList = JShell.getHistoryList();
    
    //Checks if the number is valid and if the needed history redo will
    //be and endless loop
    if (lastHistoryRedo <= 0|| lastHistoryRedo > historyList.size()){
      throw new CommandError("Invalid history number entered");
    }
    String neededCommand = historyList.get(lastHistoryRedo - 1);
    if (neededCommand.startsWith("!")){
      throw new CommandError("Cant redo history on another redo history call");
    }
    //Removes the number from the front of history and return the string
    //for execution
    String [] removedHistoryNumber = neededCommand.split(" ", 2);
    return removedHistoryNumber[1];
    }
    catch(NumberFormatException e){
      throw new CommandError("Invalid Command: Non-Integer history inputed");
    }
    
  }
}
