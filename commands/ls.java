package commands;

import driver.JShell;
import fileAndDirectory.Directory;

/**
 * 
 * Unless a path is given, print the current directory's contents.
 * Otherwise, go to given path and print it's contents.
 * 
 */
public class ls extends Command{
  public ls(){
    this.cmd = "ls";
    this.par = 100;
    this.desc ="ls[PATH] \n"
        + "Prints the contents of the current directory if PATH is not given.\n"
        + "If it is given, prints the contents of the directory in that path.\n"
        + "/#ls \n "
        + "Applications    Downloads   Music \n"
        + "Desktop         Library \n "
        + "Documents       Music\n "
        + "/#ls /users/Mickael/documents/lessons/2ndyear\n"
        + "csc207  eco200  sta256\n"
        + "csc236  ec0202";

  }

  @Override
  /* Execution of LS Command
   * @param String[]
   * @param JShell
   * @return String
   * @throws CommandError
   */
  public String execute(String[] parameters, JShell JShell) throws CommandError{
    boolean recursiveCase = false;
    Directory previous = JShell.getCurrentDir();
    Command cd = new cd();
    String name;
    String result = "";
    String contents =JShell.getCurrentDir().toString();
    if (parameters.length != 0){
      for (String paths: parameters){
        if (paths.equalsIgnoreCase("-r")){
          recursiveCase = true;
          if (parameters.length == 1){
            String newContents = "-r "+ contents;
            String[] recParameters = newContents.split("\\s+");
            String dirName = previous.getName()+": ";
            return dirName +contents+"\n"+this.execute(recParameters,JShell);
          }
        }
        else{
          //If its given a path change directories up until the last part
          name = PathChanger.traversePartWay(paths, JShell);
          //Check if the name/last segment is a file or not
          if (JShell.getCurrentDir().getFile(name) != null){
            result += name +"\n";
          }
          else{//If its not a file change to the last segment of the path
            String[] lastPath = {name};
            try {//Try to change to the last section if it exists
              cd.execute(lastPath, JShell);
            } catch (CommandError e){
              JShell.setCurrentDir(previous);
              throw new CommandError("Directory " + name +" does not exist.");
            }
            contents = JShell.getCurrentDir().toString();
            //if it gets here its just printing the elements of a dir
            result += (JShell.getCurrentDir().getName() + ": " + 
                contents + "\n");
            if (recursiveCase && !contents.equals("")){
              contents = "-r " + contents;
              String[] recParameters = contents.split("\\s+");
              result+= this.execute(recParameters, JShell);
            }
          }
          JShell.setCurrentDir(previous);
        }
      }
    }
    else
    {
      result = (previous.getName() + ": " + previous.toString() + "\n");
    }
    return result;
  }


}
