package commands;


import driver.JShell;
import fileAndDirectory.File;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;


/**
 *Takes a URL and retrieve the file from it and then adds it to your current
 *working directory.
 */


public class get extends Command{
  public get(){
    this.par = 1;
    this.cmd = "get";
    this.desc =
        "get URL \n"
            + "Retreive the file from the URL and adds it to the current"
            + " working directory.\n"
            + "/#get http://www.cs.cmu.edu/~spok/grimmtmp/073.txt\n"
            + "/#get http://www.ub.edu/gilcub/SIMPLE/simple.txt";
    
  }
  /* Execution of get Command
   * @param URL
   * @param JShell
   * @return Null
   */

  @Override
  public String execute(String[] parameters, JShell JShell) throws CommandError{
    // TODO Auto-generated method stub
    //checks if we only give 1 parameter
      try {
        //create a new URL
        URL url = new URL(parameters[0]);
        //open the Stream from the URL and put it into BufferedReader
        BufferedReader br = new BufferedReader(new InputStreamReader
            (url.openStream()));
        //create a string
        String inputLine;
        //take the filename from the URL
        String name = url.getFile();
        //split the filename on every "/"
        String [] parts = name.split("/");
        //create a string from the last word 
        String fileName = parts[parts.length-1];
        File newFile = JShell.getCurrentDir().getFile(fileName);
        //checks if the file exists
        if (newFile == null) {
          //create a new file if it does not exist
          newFile = new File(fileName);
        }
        //remove the null from the file
        newFile.write("");
        //traverse through the content of the URL and appends it to the file
        while ((inputLine = br.readLine()) != null)
          newFile.rewrite(inputLine+"\n");
        //close the BufferedReader
        br.close();
        //add to the directory
        JShell.getCurrentDir().add(newFile);
      }
      catch (MalformedURLException e) {
        throw new CommandError (" Malformed URL. Try Again.");
      }
      catch (IOException e) {
        throw new CommandError (" File not found. Try Again.");
      }
      catch(Exception e){
        throw new CommandError ("Something went wrong");
      }
    return null;
  }
}
