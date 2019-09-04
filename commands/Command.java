package commands;

import driver.JShell;

/**
 * returns the name, parameters and description
 */

public abstract class Command {
  String cmd;
  int par;
  String desc;
  
  
  /* 
   * @param null
   * @return String
   */
  public String getName()
  { return cmd; }
  
  /* 
   * @param null
   * @return int
   */
  public int getParameters()
  { return par; }
  
  /* 
   * @param null
   * @return String
   */
  public String toString()
  { return desc; }
  
  abstract public String execute(String[] parameters, JShell shell) throws 
  CommandError;
}

