package commands;

import java.util.Map;

import driver.CommandMap;
import driver.JShell;

/*
 * Prints the description of a given command.
 */

public class man extends Command {
  public man() {
    this.par = 1;
    this.cmd = "man";
    this.desc = "man CMD \n" + "Print documentation for CMD.";
    
  }
  
  /* Execution of Man Command
   * @param String[]
   * @param JShell
   * @return String
   * @throws commandError
   * @exception when a command does not exist
   */

  public String execute(String[] parameters, JShell JShell)throws CommandError {
    //Checks if the parameter given is a command and populates a hash map
    //to get the description of the parameter command
    if (parameters[0]
        .matches("mkdir|cd|ls|pwd|pushd|popd|history|cat|echo|man|get")) {
      Map<String, Command> commands;
      Command cmd;
      CommandMap mapOfCommands = new CommandMap();
      commands = mapOfCommands.getCommandMap();
      cmd = (Command) commands.get(parameters[0]);
      return cmd.toString();
    }
    //Throw an error if parameter given is not a command
    else {
      throw new CommandError("Invalid Command type inserted.");
    }
  }
}
