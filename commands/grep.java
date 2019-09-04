package commands;
import java.util.Arrays;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import driver.JShell;
import fileAndDirectory.Directory;
import fileAndDirectory.File;

public class grep extends Command{
  public grep(){
    this.cmd = "grep";
    this.par = 2;
    this.desc = "grep [-R] REGEX PATH"
        +"If [-R] is given, traverse PATH, which is a directory, and print\n"
        + "the path to the file for all lines that contain REGEX\n"
        +"If [-R] is not given, print the lines containing REGEX in PATH,\n"
        + "which is a file\n";
  }

  
  @Override
  /* Execution of grep Command
   * @param file or directory and regex
   * @return String
   * @throws CommandError
   */
  public String execute(String[] parameters, JShell shell) throws CommandError {
    Iterator<String> parIterator = Arrays.asList(parameters).iterator();
    Directory currentDir = shell.getCurrentDir();
    String regex = parIterator.next();
    boolean rec = false;
    String result = "";
    if(regex.equalsIgnoreCase("-r"))
    {
      rec = true;
      regex = parIterator.next();
    }
    //remove the quotes from regex
    regex = regex.replaceAll("\"", "");
    Pattern regexPattern = null;
    try{
    regexPattern = Pattern.compile(regex);
    }
    catch(PatternSyntaxException e){
      throw new CommandError ("Invalid regex.");
    }
    while(parIterator.hasNext())
    {
      String path = parIterator.next();
      String name = PathChanger.traversePartWay(path, shell);
      Object item = shell.getCurrentDir().getItem(name);
      //If the item the path is pointing too doesnt exist its an error
      if (item == null){
        shell.setCurrentDir(currentDir);
        throw new CommandError("File or Directory not found.");
      }
      
      //If its a file we have to match the contents the regex
      if (item instanceof File){
        String[] contents = ((File) item).getContents().split("\n");
        for (String line: contents){
          Matcher patternMatcher = regexPattern.matcher(line);
          if (patternMatcher.find()){
            result+=path +": "+ line + "\n";
          }
        }
      }
      
      //If its a directory and -r is not there its an error
      //Otherwise we recursively call the function on the directories contents
      if (item instanceof Directory){
        if (!rec){
          shell.setCurrentDir(currentDir);
          throw new CommandError("Path must lead to file.");
        }
        shell.setCurrentDir((Directory)item);
        String[] newContents=("-r " + regex + " "+ 
            item.toString()).split("\\s+");
        result += this.execute(newContents, shell);
      }
      shell.setCurrentDir(currentDir);
    }
    return result;
  }

}
