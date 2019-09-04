package commands;

import driver.JShell;

/**
 * Helper functions to handle paths
 */
public class PathChanger {
  /*
   * Changes current dir to a path
   * 
   * @param String of a Path
   * @param JShell
   * @return Returns true if the path exists
   */
  public static boolean changeToFullPath(String path, JShell shell) {
    Command cmd = new cd();
    String[] parameter = {path};
    try {
      cmd.execute(parameter, shell);
      return true;
    } catch (CommandError e) {
      return false;
    }
  }

  /*
   * Traversing a path part way
   * 
   * @param String of a Path
   * @param JShell
   * @return Returns a string of the last element in the path
   * 
   * @exception When the path doesn't exist
   * 
   * @throws CommandError
   */
  public static String traversePartWay(String fullPath, JShell JShell)
      throws CommandError {
    Command cd = new cd();
    String last = "";
    // If its not a path just return it
    if (!fullPath.contains("/")) {
      return fullPath;
    }
    String shortPath = fullPath.substring(0, fullPath.lastIndexOf('/'));
    String[] path = {shortPath};
    if (fullPath.equals("/")) {
      return "/";
    }
    // If its a path of one thing go to parentDir
    if (shortPath.length() == 0) {
      JShell.setCurrentDir(JShell.getParentDir());
      return fullPath.substring(fullPath.lastIndexOf('/') + 1,
          fullPath.length());
    }
    // Otherwise try to change to the shortened path
    cd.execute(path, JShell);
    last = fullPath.substring(fullPath.lastIndexOf('/') + 1, fullPath.length());

    // Otherwise return the last segment of the path
    return last;
  }
}
