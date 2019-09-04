package commands;

import driver.JShell;


/**
 *
 * Removes the top entry from the stack of directories.
 * Changes the current directory to the recently removed top entry.
 */
public class popd extends Command{
  public popd(){
    this.cmd = "ls";
    this.par = 1;
    this.desc ="pwd DIR \n"
        +"Changes the current directory to the first directory on the stack.\n"
        +"/# mkdir a \n"
        +"/#mkdir a/b \n"
        +"/#pushd a/b \n"
        +"/#popd ";
    
  }

  @Override
  /* Execution of POPD Command
   * @param String[]
   * @param JShell
   * @return String
   * @exception when stack is empty
   * @throws EmptyStackException
   */
  public String execute(String[] parameters, JShell JShell) throws CommandError{
    String popDir = pushd.dirStack.pop();
    Command cd = new cd();
    String[] list = {popDir};
    cd.execute(list, JShell);
    return null;
//    Directory prevDir = dir.getPrevDir();
//    Command cd = new cd();
//    String prevDirContents = prevDir.toString();
//    String[] stuffInDir = prevDirContents.split(" ");
//    //String[] dirSplit = dir.getName().split("/");
//    int counter = 0;
//    Boolean result = false;
//    while (counter < stuffInDir.length)
//    {
//      if (stuffInDir[counter] == dir.getName())
//      {
//        result = true;
//      }
//    }
//    
//    if (result == true)
//    {
//      JShell.setCurrentDir(dir);
//      return null;
//    }
//    else
//    {
//      return "File and/or Directory no longer exists";
//    }
    //JShell.setCurrentDir(dir);
  }

}
