package fileAndDirectory;

/**
 * Create a file.
 * Return the name and the content of the file.
 * Write or rewrite on the file.
 */


public class File {

  private String name;
  //Public so Echo can edit contents
  public String contents;
  
  
  public File(String na){
    name = na;
  }
  /* 
   * @param null
   * @return String
   */
  public String getName(){
    return name;
  }
  public void setName(String newName){
    this.name = newName;
  }
  /* 
   * @param null
   * @return String
   */
  public String getContents(){
    return contents;
  }
  
  /* 
   * @param String
   * @return null
   */
  public void write(String newString){
    //overwrite newString in the file
    contents = newString;
  }
  
  /* 
   * @param String
   * @return null
   */
  public void rewrite(String newString){
    //append newString in the file
    contents += newString;
  }
  
}

