package commands;

import java.util.ArrayList;

import driver.JShell;

/**
 *
 *         Will print out the most recent commands given the number of input The
 *         numbers must be greater than 0. It is separated into two, where the
 *         number represents the most recent command and then the most recently
 *         prompted command follows.
 */
public class history extends Command {
  public history() {
    this.par = 1;
    this.cmd = "history";
    this.desc = "history [number] \n" + "Prints out recent commands. If number "
        + "n given, it will show the last n commands.\n" + "/# history\n"
        + "1 ls\n" + "2 cd..\n" + "3 ld\n" + "4 ls\n" + "5 history\n"
        + "/# history\n" + "1 history\n" + "2 history 2";
  }

  /*
   * @param String[]
   * 
   * @return String
   * @param JShell
   * @exception when input is less than 0
   * @throws CommandError
   */
  public String execute(String[] parameters, JShell JShell) throws CommandError{
    ArrayList<String> historyList = JShell.getHistoryList();
    String result = "";
    // Add all history to result String if no parameters given
    if (parameters.length == 0) {
      for (String p : historyList)
        result += p + "\n";
    } else {
      try {
        int lastCommands = Integer.parseInt(parameters[0]);
        // Add all history to result String if input parameter is greater than
        // the size of historyList
        if (lastCommands > historyList.size()) {
          for (String p : historyList)
            result += p + "\n";
        } else if (Integer.parseInt(parameters[0]) < 0) {
          // throw error for negative integer parameter given
          throw new CommandError("Invalid Command: Negative history inputted");
        } else {
          // Add all the needed history into the result string
          //until the last given parameter point
          for (int i = (historyList.size() - lastCommands); i < historyList
              .size(); i++) {
            result += (historyList.get(i)) + "\n";;
          }
        }
      } catch (NumberFormatException e) {
        // throw error for non integer parameter passed
        throw new CommandError("Invalid Command: Non-Integer history inputted");

      }
    }
    return result.trim();
  }
}
