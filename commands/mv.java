package commands;

import driver.JShell;
import fileAndDirectory.Directory;
import fileAndDirectory.File;

public class mv extends Command {

  public mv(){
    this.cmd = "mv";
    this.par = 2;
    this.desc =  " mv OLDPATH NEWPATH"
        + "Move item(s) from an old to a new path, absolute or relative. "
        + "mv /Users/Abbas/Stuff /Users/Abbas/Games";

  }

  @Override
  public String execute(String[] parameters, JShell shell) throws CommandError {
    Directory currentDir = shell.getCurrentDir();
    String oldPath = parameters[0];
    String newPath = parameters[1];

    if(newPath.contains(oldPath)){
      throw new CommandError("Cannot move into its own subdirectory.");
    }
    // Check new path if it is a file or directory
    String newSeg = PathChanger.traversePartWay(newPath, shell);
    Directory newPathDir = shell.getCurrentDir();
    Object newPathItem = shell.getCurrentDir().getItem(newSeg);
    shell.setCurrentDir(currentDir);
    if (newPath.equals("/"))
    {
      newPathItem = shell.getParentDir();
    }

    // Find out if old path contains a file or directory
    String oldSeg = PathChanger.traversePartWay(oldPath, shell);
    Directory oldPathDir = shell.getCurrentDir();
    Object oldPathItem = shell.getCurrentDir().getItem(oldSeg);

    if (newPathItem == null || oldPathItem == null)
    {
      throw new CommandError("Path not found.");
    }

    if(newPathItem instanceof Directory)
    {
      // Delete file from path
      delete.del(oldSeg, shell);

      if (((Directory)newPathItem).toString().contains(oldSeg)){
        shell.setCurrentDir((Directory) newPathItem);
        delete.del(oldSeg,shell);
        shell.setCurrentDir(oldPathDir);
      }
      // Move file into new directory
      ((Directory) newPathItem).add(oldPathItem);

    }

    if(newPathItem instanceof File){
      if (oldPathItem instanceof File){
        String newContents = oldPathDir.toString().replaceFirst(oldSeg, newSeg);
        
        if (newPathDir.toString().contains(oldSeg)){
          shell.setCurrentDir(newPathDir);
          delete.del(oldSeg,shell);
          shell.setCurrentDir(oldPathDir);
        }
        
        oldPathDir.setContents(newContents);
        ((File)oldPathItem).setName(newSeg);
      }
      else{
        throw new CommandError("Cannot move a directory into file.");
      }
    }
    shell.setCurrentDir(currentDir);

    return null;

  }


}
