package commands;

import java.util.Stack;


import driver.JShell;
/**
 *
 * Saves the current directory into a stack for future reference.
 * Changes the current directory to the directory given at input.
 */

public class pushd extends Command{
  public static Stack<String> dirStack = new Stack<String>();
  public pushd(){
    this.cmd = "ls";
    this.par = 1;
    this.desc ="pwd DIR \n"
        + "Saves the current directory onto a stack and then changes the "
        + "current directory to DIR"
        +"/# mkdir a \n"
        +"/#mkdir a/b \n"
        +"/#pushd a/b \n"
        +"/#popd ";
    
  }

  @Override
  
  /* Execution of PUSHD Command
   * @param String[]
   * @param JShell
   * @return String
   * @throws CommandError
   */
  public String execute(String[] parameters, JShell JShell) throws CommandError{
    //Directory currentDir = JShell.getCurrentDir();
    Command pwd = new pwd();
    String[] empty = null;
    String currentDirPath = pwd.execute(empty, JShell);
    Command cd = new cd();
    cd.execute(parameters, JShell);
    dirStack.push(currentDirPath);
    return null;
  }

}
