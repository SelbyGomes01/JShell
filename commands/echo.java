package commands;

import java.util.ArrayList;
import driver.JShell;
import fileAndDirectory.Directory;
import fileAndDirectory.File;

/**
 * Prints the STRING when OUFILE is not given. When given, it creates the file
 * if it does not exist and appends the STRING in the new file. If it exists,
 * checks if at index 1, there is a > or a >>. If it is >, it overwrites and 
 * erase the old content to the new STRING. If it is >>, it appends STRING in
 * OUTFILE without touching the old content
 */

public class echo extends Command {
  public echo() {
    this.par = 3;
    this.cmd = "echo";
    this.desc =
        "echo STRING [>OUTFILE] \n"
            + "If OUTFILE is not provided, prints STRING,"
            + " Otherwise, put STRING into file OUTFILE.\n"
            + "> creates a new file if OUTFILE does not exists and erases "
            + "the old contents if OUTFILE already exists.\n"
            +">> appends the STRING in OUTFILE and keeps the old content\n"
            + "/#echo \"cat\"\n"
            + "\"cat|'\n"
            + "/#echo \"cat\" > animals\n"
            + "/#cat animals\n"
            + "\"cat\"\n"
            + "/#echo \"fish\" > animals\n"
            + "/#cat animals\n"
            + "\"fish\"\n"
            + "/#echo \"zebra\" >> animals\n"
            + "/#cat animals\n"
            + "\"fish\" \"zebra\"";


  }
  /* Execution of echo Command
   * @param String[]
   * @param JShell
   * @return Null or String if no OUTFILE
   */

  @Override
  public String execute(String[] parameters, JShell JShell) throws CommandError{
    // TODO Auto-generated method stub
    Directory currentDir = JShell.getCurrentDir();
    boolean inString = false;
    String str = "";
    ArrayList<String> para = new ArrayList<String>();
    //Takes the parameters and sets it so that anything in quotes is 1 index
    for (String par : parameters) {
      if (par.startsWith("\"") && par.endsWith("\"") && par.length() > 2) {
        str += par;
        para.add(par);
      } 
      else if (par.startsWith("\"") && !inString) {
        str += par + " ";
        inString = true;
      } 
      else if (par.endsWith("\"") && inString) {
        str += par;
        inString = false;
        para.add(str);
      }

      else if (!inString) {
        para.add(par);
      } 
      else 
      {
        str += par + " ";
      }

    }

    // checks if it has 2 parameters and String is surrounded by quotation marks
    if (para.size() == 1 && para.get(0).indexOf('"') == 0
        && para.get(0).lastIndexOf('"') == (para.get(0).length() - 1)) {
      return (para.get(0).replaceAll("\"", ""));
    }
    // checks if it has 4 parameters and String is surrounded by quotation marks
    else if (para.size() == 3 && para.get(0).startsWith("\"")) {
      Command cd = new cd();
      
      String name;
      if (para.get(2).contains("/")){
       
        String fullPath = para.get(2);
        String[] path = {fullPath.substring(0,fullPath.lastIndexOf('/'))};
        if (path[0].length() != 0){
          cd.execute(path, JShell);
        }
        //Since its possible to have a File at the end we must save that
        name = fullPath.substring(fullPath.lastIndexOf('/')+1,
            fullPath.length());
      }
      else{
        name = para.get(2);
      }
      
      File file = JShell.getCurrentDir().getFile(name);
      // if the file exist
      if (file != null) {
        // checks if the second parameter is >
        if (para.get(1).equals(">")) {
          // overwrites the String in the file
          file.write(para.get(0).replaceAll("\"", ""));
        }
        // checks if the second parameter is >>
        else if (para.get(1).equals(">>")) {
          // appends the string in the file
          file.rewrite(System.getProperty("line.separator"));
          file.rewrite(para.get(0).replaceAll("\"", ""));
        } 
        else 
        {//Throw exception if the 2nd parameter isnt > or >>
          JShell.setCurrentDir(currentDir);
          throw new CommandError("Invalid parameters.");
        }

      }
      // if the file does not exist
      else {
        if (JShell.getCurrentDir().exists(name)){
          JShell.setCurrentDir(currentDir);
          throw new CommandError("Name already in use.");
        }
        // checks if the second parameter is >
        if (para.get(1).equals(">") || para.get(1).equals(">>")) {
          // create a new file
          File newFile = new File(name);
          // append the String
          newFile.write(para.get(0).replaceAll("\"", ""));
          // add to the directory
          JShell.getCurrentDir().add(newFile);
          //System.out.println(newFile.getContents());
        } 
        else {//Throw exception if the 2nd parameter isnt > or >>
          JShell.setCurrentDir(currentDir);
          throw new CommandError("Invalid parameters.");
        }
      }
    }
    else{//itll get here if the quotes around the string wasnt closed properly
      JShell.setCurrentDir(currentDir);
      throw new CommandError("Invalid parameters.");
    }
    JShell.setCurrentDir(currentDir);
    return null;
  }
}
