package commands;

import driver.JShell;
import fileAndDirectory.Directory;
import fileAndDirectory.File;

public class cp extends Command {

  public cp() {
    this.cmd = "cp";
    this.par = 2;
    this.desc = " cp OLDPATH NEWPATH"
        + "Move item(s) from an old path, without removing it,\n"
        + " to a new path, absolute or relative.\n"
        + "/#cp /Users/Abbas/Stuff /Users/Abbas/Games";

  }

  private File copyFile(File file) {
    // Helper function used to make a copy of a file
    File newFile = new File(file.getName());
    newFile.write(file.getContents());
    return newFile;
  }

  @Override
  /* Execution of CP Command
   * @param filepath
   * @param JShell
   * @return None
   * @throws CommandError
   */
  public String execute(String[] parameters, JShell shell) throws CommandError {
    Directory currentDir = shell.getCurrentDir();
    // Determine if old Path is a file or directory
    String newItemName = PathChanger.traversePartWay(parameters[1], shell);
    Object newItem = shell.getCurrentDir().getItem(newItemName);
    shell.setCurrentDir(currentDir);
    if (newItem == null){
      throw new CommandError("File or director not found.");
    }
    // change paths to the old item that could be a dir or a file
    String oldPathItem = PathChanger.traversePartWay(parameters[0], shell);
    Object oldItem = shell.getCurrentDir().getItem(oldPathItem);
    shell.setCurrentDir(currentDir);
    
    // Base case, the path is only a file
    if (oldItem instanceof File) {
      // If its a file copy the file
      if (newItem instanceof File){
        ((File)newItem).write(((File) oldItem).getContents());
      }
      else{
        File newFile = this.copyFile((File) oldItem);
        ((Directory) newItem).add(newFile);
      }
      
    }
    
    if (oldItem == null) {
      throw new CommandError("File or directory not found.");
    }
    if (oldItem instanceof Directory) {
      if (newItem instanceof File){
        throw new CommandError ("Cannot move directory to file.");
      }
      Directory newDir = new Directory(oldPathItem);
      ((Directory) newItem).add(newDir);
      shell.setCurrentDir(currentDir);
      // We must loop through the old directories contents and recursively copy
      for (String contents : ((Directory) oldItem).toString().split(" ")) {
        // The old path is now the old path and the contents of the old dir
        // The new path is now the new path plus the newly made directory
        String[] listOfPaths =
            {parameters[0] + "/" + contents, parameters[1] + "/" + oldPathItem};
        this.execute(listOfPaths, shell); // Recursively copy the contents
      }
    }
    shell.setCurrentDir(currentDir);
    return null;
  }


}
