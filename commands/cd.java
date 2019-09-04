package commands;

import driver.JShell;
import fileAndDirectory.Directory;

/**
 * Change directory to DIR, which may be relative to the current directory or 
 * may be a full path.
 */

public class cd extends Command{

  public cd(){
    this.cmd = "cd";
    this.par = 1;
    this.desc =  " cd DIR \n "
        + "Change directory to DIR, which may be relative to the current "
        + "directory or may be a full path.\n"
        + "/#cd documents\n"
        + "cd /Users/Mickael/documents/lessons/csc207";

  }

  @Override
  /* Execution of CD Command
   * @param filepath
   * @param JShell
   * @return None
   * @exception when cannot go back by any directories
   * @throws CommandError
   */
  public String execute(String[] parameters, JShell JShell) throws CommandError{
    String dir = parameters[0];
    String[] path;
    Directory currentDir = JShell.getCurrentDir();
    //we need a boolean to track if the path exists
    boolean exists = true;
    //If its just a slash go to parent dir
    if (dir.equals("/")){
      currentDir = JShell.getParentDir();
    }
    else{//Otherwise we must try and traverse the path
      //If it is a absolute dir we start from the parent dir
      if (dir.indexOf('/') == 0){
        path = dir.substring(1, dir.length()).split("/");
        currentDir = JShell.getParentDir();
      }
      else{//if the path is relative split up the path names in current dir
        path = dir.split("/");
      }
      //loop through a parts of the path trying to go to each one
      for (String part: path){
        //If the user types .. we go back
        if (part.equals("..")){
          if (currentDir.getPrevDir() !=null){
            currentDir = (currentDir.getPrevDir());
          }else{throw new CommandError("Cannot go back any more.");}
        }
        else if(part.equals(".")){
          //If the user types . it remains in the current directory
        }
        else{//Otherwise we try and go to the new directory
          Directory newDir = currentDir.getDirectory(part);
          if (newDir != null && exists){
            currentDir = newDir;
          }
          else{
            exists = false;
          }
        }
      }
    }
    if (exists){
      JShell.setCurrentDir(currentDir);
    }
    else{
      throw new CommandError("Directory " + dir +" does not exist.");
    }


    return null;

  }

}
