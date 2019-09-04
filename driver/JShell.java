package driver;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.Map;
import java.util.Scanner;

import commands.*;
import fileAndDirectory.Directory;

public class JShell {
  // We need a variable for all directories (parentDir) and current (currentDir)
  private static JShell shell;
  private static Directory parentDir = null;
  private Directory currentDir;
  private static ArrayList<String> historyList;

  CommandMap commandMap;
  Map<String, Command> commands;
  int historyCounter;

  public ArrayList<String> getHistoryList() {
    return historyList;
  }

  private JShell() {
    currentDir = parentDir;
    historyCounter = 0;
    commandMap = new CommandMap();
    commands = commandMap.getCommandMap();
  }

  // factory method to create a JShells with a single parentDir
  public static JShell createJShell() {
    if (parentDir == null) {
      parentDir = new Directory("/");
      historyList = new ArrayList<String>();
      shell = new JShell();
    }
    return shell;
  }

  public Directory getParentDir() {
    return parentDir;
  }
  
  public Directory getCurrentDir(){
    return currentDir;
  }
  
  public void setCurrentDir(Directory d){
    currentDir = d;
  }

  // This command will allow the JShell to run a given command input
  public String run(String input) {
    String result = null;
    Command cmd;

    // if there is an input
    if (!input.trim().isEmpty()) {
      historyCounter++;
      historyList.add(historyCounter + " " + input);
      // split the input string into an array

      String[] commandAndArgs = input.trim().split("\\s+");

      // Only run commands if the user doesn't type exit
      if (!commandAndArgs[0].equals("exit")) {
        try {
          if(commandAndArgs[0].startsWith("!")){
            cmd = commands.get("!");
            result = cmd.execute(commandAndArgs,this);
            commandAndArgs = result.trim().split("\\s+");
            cmd = commands.get(commandAndArgs[0]);
            result = null;
          }
          else{
          cmd = commands.get(commandAndArgs[0]);
          }
          // if the command given isnt in the commands map theres an error
          if (cmd == null) {
            throw new ArrayIndexOutOfBoundsException();
          }
          // We have to take out the command name before passing to the commands
          String[] parameters =
              Arrays.copyOfRange(commandAndArgs, 1, commandAndArgs.length);

          if (!commandAndArgs[0].equals("echo")
              && (input.contains(">") || input.contains(">>"))) {
              cmd = commands.get("OutputCommandToFile");
              cmd.execute(commandAndArgs,this);
          }
          // Special case for echo since echo handles parameters on its own
          else if (commandAndArgs[0].equals("echo")) {
            result = cmd.execute(parameters, this);
          }
          // Check if the command is given the correct # of parameters
          else if (cmd.getName().matches("mkdir|cat|grep")&&commandAndArgs.length>1){
            // Mkdir and cat can take many parameters
            result = cmd.execute(parameters, this);
          } else if (commandAndArgs.length - 1 > cmd.getParameters()) {
            // Check if what we are given exceeds the number of parameters
            throw new ArrayIndexOutOfBoundsException();
          } else {
            // Everything checks out then execute the command
            result = cmd.execute(parameters, this);
          }

          // Catch all possible exceptions
        } catch (ArrayIndexOutOfBoundsException e) {
          return "Invalid argument. Try Again.";
        } catch (NullPointerException e) {
          return e.getMessage();
        } catch (EmptyStackException e) {
          return "Stack is empty.";
        } catch (CommandError e) {
          return e.getMessage();
        }
      }
    }
    // return whatever result the commands bring
    return result;
  }

  public static void main(String[] args) {
    JShell shell = JShell.createJShell();
    Scanner reader = new Scanner(System.in);
    String input = "";
    String result;
    while (!input.equals("exit")) {
      System.out.print("/#");
      input = reader.nextLine();
      result = shell.run(input);
      if (result != null) {
        System.out.println(result);
      }
    }
    reader.close();
  }
}
