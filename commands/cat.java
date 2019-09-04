package commands;

import driver.JShell;
import fileAndDirectory.Directory;
import fileAndDirectory.File;
/**
 * Display the contents of the file in the shell
 * 
 */
public class cat extends Command {

  public cat() {
    this.par = 1;
    this.cmd = "cat";
    this.desc = "cat FILE \n" 
        + "Display the contents of FILE in the shell."
        + "/#echo \"fish\" > animals\n"
        + "/#cat animals\n"
        + "\"fish\"";
  }

  /* Execution of Cat Command
   * @param filepath
   * @param JShell
   * @return String
   * @throws CommandError
   */
  public String execute(String[] parameters, JShell JShell){

    // Save the position of the currentDir
    Directory currentDir = JShell.getCurrentDir();
    String result = "";
    for (String parameter: parameters){
      String filePath = parameter;
      // If given a path we must change to the path
      try {
        String fileName = PathChanger.traversePartWay(filePath, JShell);
        // Try and retrieve the file from the currentDir
        File fileToPrint = JShell.getCurrentDir().getFile(fileName);
        // Change the directory back if its changed
        JShell.setCurrentDir(currentDir);
        // If the file exists we add to the result
        if (fileToPrint != null) {
          result+= (fileToPrint.getContents()+"\n");
        } else {//Otherwise we must state that the file does not exist
          result += ("File "+fileName+" not found\n");
        }
      } catch (CommandError e) {
        // If parameters is not a path then its just the filename
        result +="Path "+parameter+" not found\n";
      }
    }
    return result;
  }
}

