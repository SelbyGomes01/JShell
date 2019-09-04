package commands;
import java.util.Arrays;
import java.util.Map;
import driver.CommandMap;
import driver.JShell;

/**
 * Outputs the execution of a command to a file in the system
 * 
 */
public class commandOutputToFile extends Command{
  public commandOutputToFile() {
    this.par = 0;
    this.cmd = "commandOutputToFile";
    this.desc = "Outputs the execution of a command to a file in the"
        + "system";
  }
  
  /* Execution of executePastCommand Command
   * @param String[]
   * @param JShell
   * @return String
   * @throws CommandError
   */
  public String execute(String[] input, JShell JShell) throws CommandError{
    // check if the output dashes are n the valid spot
    if (!input[input.length-2].equals(">") &&
        !input[input.length-2].equals(">>")){
      throw new CommandError("Invalid Input: Please only specify one OUTFILE");
    }
    //Create variables for output from command, get the command map, and a new 
    //array to hold the changed parameters for commands that will be executed
    String commandOutput;
    CommandMap mapOfCommands = new CommandMap();
    Map<String, Command> commands = mapOfCommands.getCommandMap();
    String [] newParameters;
    
    //get output for !number command
    if (input[0].startsWith("!")){
      Command cmd = commands.get("!");
      newParameters = Arrays.copyOf(input, input.length-2);
      commandOutput = cmd.execute(newParameters , JShell);
    }
    //get output for any other commands
    else {
      Command cmd = (Command) commands.get(input[0]);
      newParameters = Arrays.copyOfRange(input, 1, input.length-2);
      commandOutput = cmd.execute(newParameters , JShell);
    }
    //return null if command output is null
    if (commandOutput == null){
      return null;
    }
    //Use echo command to write output to file
    else{
      echo writeToFile = new echo();
      String[] echoParameters = new String[3];
      echoParameters[0] = "\"" + commandOutput.trim() + "\"";
      echoParameters[1] = input[input.length-2];
      echoParameters[2] = input[input.length-1];
      return writeToFile.execute(echoParameters, JShell);
    }
  }
}