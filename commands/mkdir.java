package commands;

import driver.JShell;
import fileAndDirectory.Directory;

/*
 * Makes a directory in the current directory or a given path.
 */
public class mkdir extends Command{

  public mkdir(){
    this.par = 1;
    this.cmd = "mkdir";
    this.desc ="mkdir DIR \n"
        + "Create directories, each of which may be relative to the"
        + " current directory or may be a full path. \n/# mkdir Assignment \n"
        + "/# mkdir /Users/Mickael/documents/lessons/csc207";
  }


  @Override
  /* Execution of MKDIR Command
   * @param String[]
   * @param JShell
   * @return String
   * @exception when out of bounds or when directory exists
   * @throws CommandError
   * @throws ArrayIndexOutOfBoundsException
   */
  public String execute(String[] parameters, JShell JShell)throws CommandError{
    //Cannot make directory with name .. or .
    if (parameters[0].equals(".") || parameters[0].equals("..")){
      throw new CommandError("Invalid argument. Try Again.");
    }
    Directory currentDir = JShell.getCurrentDir();
    
    for (int parIndex =0 ; parIndex < parameters.length; parIndex++){
      //Take each parameter and save as possible path
      String totalPath = parameters[parIndex];
      String dirName = totalPath;
      //If parameter is a path we treat it differently
      dirName = PathChanger.traversePartWay(totalPath, JShell);
      if(!JShell.getCurrentDir().exists(dirName)){
        Directory newDir = new Directory(dirName);
        JShell.getCurrentDir().add(newDir);
//        newDir.setPrevDir(JShell.getCurrentDir());
      }
      else{
        JShell.setCurrentDir(currentDir);
        throw new CommandError("Name "+dirName+" already exists.");
      }
      //Change back to where the directory was originally
      JShell.setCurrentDir(currentDir);
    }
    return null;
  }

}
