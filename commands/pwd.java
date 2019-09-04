package commands;

import driver.JShell;
import fileAndDirectory.Directory;

/**
 * Prints the path of the current working directory.
 *
 */
public class pwd extends Command{
  public pwd(){
    this.cmd = "pwd";
    this.par = 0;
    this.desc ="pwd \n"
        + "Print the current working directory path \n"
        + "/#pwd \n"
        + "/Users/Mickael/documents/lessons/csc207";

  }

  @Override
  /* Execution of PWD Command
   * @param String[]
   * @param JShell
   * @return String
   */
  public String execute(String[] parameters, JShell JShell) {
    Directory currentDir = JShell.getCurrentDir();
    String absPath = currentDir.getName();
    // Runs if not the root of the directory
    if (currentDir != JShell.getParentDir())
    {
      while(currentDir.getPrevDir() != JShell.getParentDir())
      {
        // Finds and creates path based off of previous directories
        absPath = currentDir.getPrevDir().getName() + "/" + absPath;
        currentDir = currentDir.getPrevDir(); 
 
      }
     
    }
    if (currentDir == JShell.getParentDir())
    {
      absPath = "";
    }
    
    return ("/" + absPath);
  }
    
  }

