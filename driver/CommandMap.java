package driver;

import java.util.HashMap;
import java.util.Map;

import commands.*;

public class CommandMap {
 Map<String, Command> commands;
 public CommandMap() {
   commands = new HashMap<String, Command>();
   commands.put("mkdir", new mkdir());
   commands.put("cd", new cd());
   commands.put("man", new man());
   commands.put("cat", new cat());
   commands.put("ls", new ls());
   commands.put("echo", new echo());
   commands.put("pwd", new pwd());
   commands.put("pushd", new pushd());
   commands.put("popd", new popd());
   commands.put("history", new history());
   commands.put("!", new executePastCommand());
   commands.put("OutputCommandToFile", new commandOutputToFile());
   commands.put("get", new get());
   commands.put("grep", new grep());
   commands.put("mv", new mv());
   commands.put("cp", new cp());
 }
 public Map<String, Command> getCommandMap(){
   return commands;
 }
}
