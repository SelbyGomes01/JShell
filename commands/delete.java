package commands;

import driver.JShell;
import fileAndDirectory.Directory;
import fileAndDirectory.File;

public class delete{
  /**
   * @param path, shell
   * @throws CommandError exception
   * Deletes an item from a given directory and its contents
   */
  public static void del(String path, JShell JShell) throws CommandError{
    // Remove item from contents
    String name = PathChanger.traversePartWay(path, JShell);
    String contents = JShell.getCurrentDir().toString();
    String[] contentsList = contents.split(" ");
    String newList = "";
    int i = 0;
    while (i < contentsList.length)
    {
      if (!contentsList[i].equals(name))
      {
        newList += contentsList[i] + " ";
      }
      i++;
    }
    JShell.getCurrentDir().setContents(newList);
    
    // Remove file completely
    File file = JShell.getCurrentDir().getFile(name);
    JShell.getCurrentDir().getFileList().remove(file);
    
    // Remove directory completely
    Directory directory = JShell.getCurrentDir().getDirectory(name);
    JShell.getCurrentDir().getDirectoryList().remove(directory);
  }
}
