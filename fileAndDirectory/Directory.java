package fileAndDirectory;

import java.util.ArrayList;

public class Directory {

  String name;
  Directory prevDir = null;
  String contents = "";
  //Store files and directories in separate ArrayLists
  ArrayList<File> files = new ArrayList<File>();

  ArrayList<Directory> directories = new ArrayList<Directory>();
  public ArrayList<File> getFileList(){
    return files;
  }
  
  public ArrayList<Directory> getDirectoryList(){
    return directories;
  }

  public Directory(String na){
    name = na;
  }

  /*
   * @param None
   * @return String
   */
  public String getName(){
    return name;
  }
  
  /*
   * @param None
   * @return Directory
   */
  public Directory getPrevDir(){
    return prevDir;
  }
  
  /*
   * @param Directory
   * @return None
   */
  public void setPrevDir(Directory dir){
    prevDir = dir;
  }
  
  /*
   * @param String
   * @return Boolean
   */
  public Boolean exists(String name){
    String[] contentList = contents.split(" ");
    boolean result = false;
    for (int item = 0; item < contentList.length; item++){
      if (contentList[item].equals(name)){
        result = true;
      }
    }
    return result;
  }

  /*
   * @param String
   * @return File
   * @description Finds and returns file if it exists
   */
  public File getFile(String n){
    for (File f: files){
      if (f.getName().equals(n)){
        return f;
      }
    }
    return null;
  }

  /*
   * @param String
   * @return Directory
   * @description Finds and returns Directory if it exists
   */
  public Directory getDirectory(String n){
    for (Directory d: directories){
      if (d.getName().equals(n)){
        return d;
      }
    }
    return null;
  }
  
  public Object getItem(String name){
    Directory dir =this.getDirectory(name);
    File file = this.getFile(name);
    if (dir != null){
      return dir;
    }
    if (file != null){
      return file;
    }
    return null;
  }

  public String toString(){
    return contents;
  }
  
  public void setContents(String newContents) {
    contents = newContents;
    
  }


  private void addFile(File file){
    contents+= file.getName() + " ";
    files.add(file);
  }

  private void addDirectory(Directory dir){
    contents+= dir.getName() + " ";
    dir.setPrevDir(this);
    directories.add(dir);
  }

  //This function is used to add files or directories 
  public void add(Object o){
    if (o instanceof File){
      this.addFile((File) o); 
    }
    else if( o instanceof Directory){
      this.addDirectory((Directory) o);
    }
  }

}
