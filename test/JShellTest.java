package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.Field;

import driver.JShell;
import fileAndDirectory.Directory;
import fileAndDirectory.File;


public class JShellTest {
  Directory directory;
  File file;
  String fileName;
  String dirName;
  JShell shell;
  // private Singleton s;


  @Before
  public void SetUp() {
    shell = JShell.createJShell();
    dirName = "directory";
    fileName = "file";
    directory = new Directory(dirName);
    file = new File(fileName);
    // shell=Singleton.getInstance();
  }

  @After
  public void tearDown() throws Exception {
    Field field = (shell.getClass()).getDeclaredField("parentDir");
    field.setAccessible(true);
    field.set(null, null); // setting the ref parameter to null
  }

  // Testing the cat command
  @Test
  public void testCat() {
    // Basic test to see if works for new files
    shell.run("echo \"hello\" >> hello.txt");
    assertEquals("hello\n", shell.run("cat hello.txt"));
    // Tests to see if it will catch error of file not existing
    assertEquals("File bye.txt not found\n", shell.run("cat bye.txt"));
    shell.run("mkdir stuff");
    // Tests to see if it will catch error of inputting directory
    assertEquals("File stuff not found\n", shell.run("cat stuff"));

    // Tests involving A2b additions
    shell.run("echo \"test\" >> stuff/cya.txt");
    // Test with paths
    assertEquals("test\n", shell.run("cat /stuff/cya.txt"));
    shell.run("mkdir stuff/things");
    shell.run("echo \"magic\" > /stuff/things/file");
    // Test with multiple arguments
    String result = "hello\ntest\nmagic\n";
    String input = "cat /hello.txt /stuff/cya.txt /stuff/things/file";
    assertEquals(result, shell.run(input));
    // Test with invalid parameters
    result = "hello\nFile error not found\n";
    input = "cat /hello.txt /error";
    // Test with non existent file
    assertEquals(result, shell.run(input));

    // non existing parameters with multiple parameters
    result = "File error not found\ntest\nPath /test/error not found\n";
    input = "cat /error /stuff/cya.txt /test/error";
    assertEquals(result, shell.run(input));
  }

  @Test
  public void testCd() {
    shell.run("mkdir a");
    shell.run("cd a");
    // Prints pwd to see if changed to proper directory
    assertEquals("/a", shell.run("pwd"));
    // Goes back to previous directory
    shell.run("cd ..");
    assertEquals("/", shell.run("pwd"));
    // Tests exception when no more previous directories to change into
    assertEquals("Cannot go back any more.", shell.run("cd .."));
    // See if prompts work to change to current directory
    shell.run("cd /a");
    shell.run("cd .");
    assertEquals("/a", shell.run("pwd"));


  }

  @Test
  public void testEcho() {
    // Tests with creation of new text file
    shell.run("mkdir testEcho");
    shell.run("cd testEcho");
    shell.run("echo \"hello\" >> hello.txt");
    // Checks if file is added into directory
    assertEquals("testEcho: hello.txt \n", shell.run("ls"));
    // Checks if contents added into outfile
    assertEquals("hello\n", shell.run("cat hello.txt"));
    // Checks if it will print to shell when no outfile is inputted
    assertEquals("hello", shell.run("echo \"hello\""));

  }

  @Test
  public void testLs() {
    // Tests normal ls prompt for current directory
    shell.run("mkdir hello");
    assertEquals("/: hello \n", shell.run("ls"));
    shell.run("echo \"hello\" >> hello.txt");
    // Tests for when given a file name
    assertEquals("hello.txt\n", shell.run("ls hello.txt"));
    // Tests for when given a path
    shell.run("cd /hello");
    assertEquals("hello: \n", shell.run("ls /hello"));
    shell.run("cd ..");
    shell.run("mkdir a b c d");
    shell.run("mkdir a/1 b/2 c/3 d/4");
    // Testing multiple directories
    assertEquals("1: \n2: \n3: \n4: \n", shell.run("ls a/1 b/2 c/3 d/4"));
    shell.run("cd a/1");
    // Using absolute paths
    assertEquals("b: 2 \n", shell.run("ls /b"));
    shell.run("cd /a/1");
    shell.run("echo \"hello\" >> file");
    assertEquals("file\n", shell.run("ls /a/1/file"));

  }

  @Test
  public void testMan() {
    // Tests for normal commands
    assertEquals("man CMD \n" + "Print documentation for CMD.",
        shell.run("man man"));
    // Tests for commands that don't exist
    assertEquals("Invalid Command type inserted.", shell.run("man bye"));

  }

  @Test
  public void testMkdir() {
    // create a directory
    shell.run("mkdir hello");
    // test if the directory exists
    assertEquals("/: hello \n", shell.run("ls"));
    // create a directory in a directory
    shell.run("mkdir hello/goodbye");
    // change directory
    shell.run("cd hello");
    // test if we are in the good directory
    assertEquals("hello: goodbye \n", shell.run("ls"));
    // Test absolute paths
    shell.run("mkdir /a");
    // go back a directory
    shell.run("cd ..");
    assertEquals("/: hello a \n", shell.run("ls"));
    // make more than 1 directory
    shell.run("mkdir 2nd year assigments at UOFT");
    // test if there are more than 1 directory
    assertEquals("/: hello a 2nd year assigments at UOFT \n", shell.run("ls"));
    // check if it will return an error if directory still exists
    assertEquals("Name UOFT already exists.", shell.run("mkdir UOFT"));
    assertEquals("Invalid argument. Try Again.", shell.run("mkdir .."));


  }

  @Test
  public void testPopd() {
    // tested popd with an empty stack
    assertEquals("Stack is empty.", shell.run("popd"));
    shell.run("mkdir a");
    shell.run("mkdir a/b");
    shell.run("pushd a/b");
    shell.run("popd");
    // tested popd with a non empty stack
    assertEquals("/: a \n", shell.run("ls"));
  }

  @Test
  public void testPushd() {
    // tested pushd with nothing to push
    assertEquals("Invalid argument. Try Again.", shell.run("pushd"));
    shell.run("mkdir a");
    shell.run("mkdir a/b");
    shell.run("pushd a/b");
    shell.run("popd");
    // tested pushd with a non empty stack
    assertEquals("/: a \n", shell.run("ls"));
  }

  @Test
  public void testHistory() {
    // tests when nothing was used before
    assertEquals("1 history", shell.run("history"));
    shell.run("mkdir animal");
    shell.run("cd animal");
    shell.run("pwd");
    // test the history with no parameter
    assertEquals(
        "1 history\n2 mkdir animal\n3 cd animal\n4 pwd\n5 " + "history",
        shell.run("history"));
    shell.run("ls");
    shell.run("mkdir tiger");
    // test the history with a parameter
    assertEquals("7 mkdir tiger\n8 history 2", shell.run("history 2"));
    // test when non integer is used
    assertEquals("Invalid Command: Non-Integer history inputted",
        shell.run("history a"));
  }

  @Test
  public void testPwd() {
    // test the function with no directories
    assertEquals("/", shell.run("pwd"));
    // created a directory
    shell.run("mkdir animal");
    // changed directory
    shell.run("cd animal");
    // tested if we where in the correct path
    assertEquals("/animal", shell.run("pwd"));
    shell.run("mkdir horse");
    shell.run("cd horse");
    assertEquals("/animal/horse", shell.run("pwd"));
    shell.run("cd ..");
    shell.run("mkdir horse/head");
    shell.run("cd horse");
    shell.run("cd head");
    assertEquals("/animal/horse/head", shell.run("pwd"));

  }

  @Test
  public void testget() {
    // checks if function returns an error when page is not found
    assertEquals(" File not found. Try Again.",
        shell.run("get http://www.ub.edu/gilcub/SIMPLE/simple.txt"));
    // checks if function returns an error when given 2 parameters
    assertEquals("Invalid argument. Try Again.", shell.run("get cat fish"));
  }

  @Test
  public void testgrep(){
    shell.run("mkdir a b");
    shell.run("echo \"hello the\" >> b/file3");
    shell.run("echo \"Magical Fairy\" >> b/file2");
    shell.run("echo \"DUCK SAUCE e\" >> a/file1");
    shell.run("echo \"A second line\" >> a/file1");
    shell.run("echo \"A THIRD LINE\" >> a/file1");
    //Tests with invalid regex
    assertEquals("Invalid regex.", shell.run("grep [a[ a/file1"));
    //Tests with invalid paths
    assertEquals("Path must lead to file.", shell.run("grep \"M\" b"));
    //Testing 1 file non recursive
    assertEquals("b/file2: Magical Fairy\n",shell.run("grep \"[a]\" b/file2"));
    String result = "file3: hello the\n";
    //Test recursive case
    assertEquals(result, shell.run("grep -r \"[e]\" b"));
  }
  
  @Test
  public void testcp(){
    assertEquals("Invalid argument. Try Again.", shell.run("cp"));
    shell.run("mkdir a b");
    shell.run("echo \"chelsea\" > champions.txt");
    shell.run("echo \"manchester\" > relegation.txt");
    //copied a file to file
    shell.run("cp champions.txt relegation.txt");
    assertEquals("/: a b champions.txt relegation.txt \n", shell.run("ls"));
    assertEquals("chelsea\n", shell.run("cat champions.txt"));
    assertEquals("chelsea\n", shell.run("cat relegation.txt"));
    //copied a file to a directory
    shell.run("cp champions.txt a");
    assertEquals("/: a b champions.txt relegation.txt \n", shell.run("ls"));
    shell.run("cd a");
    assertEquals("chelsea\n", shell.run("cat champions.txt"));
    //changed the new version of the file
    shell.run("echo \"Lyon\" >> champions.txt");
    shell.run("cd ..");
    //checking that the initial file was not changed
    assertEquals("chelsea\n", shell.run("cat champions.txt"));
    //making sure that we cannot copy a directory inside a file
    assertEquals("Cannot move directory to file.", 
        shell.run("cp a champions.txt"));
    shell.run("mkdir c");
    //copy a directory in a directory
    shell.run("cp a c");
    assertEquals("/: a b champions.txt relegation.txt c \n", shell.run("ls"));
    shell.run("cd c");
    shell.run("cd a");
    assertEquals("a: champions.txt \n", shell.run("ls"));
    shell.run("cd ..");
    shell.run("cd ..");
    //tests the ls -R function
    assertEquals("/: a b champions.txt relegation.txt c \na: champions.txt "
        + "\nchampions.txt\nb: \nchampions.txt\nrelegation.txt\nc: a \na: "
        + "champions.txt \nchampions.txt\n", shell.run("ls -R"));
  }
  
  @Test
  public void testmv(){
    shell.run("mkdir a b");
    shell.run("echo \"chelsea\" > champions.txt");
    shell.run("echo \"manchester\" > relegation.txt");
    //move a file to a file
    
    //making sure that we cannot move a directory inside a file
    assertEquals("Cannot move into its own subdirectory.", 
        shell.run("mv a champions.txt"));
    //move a file to a directory
    shell.run("mv champions.txt a");
    assertEquals("/: a b relegation.txt \n", shell.run("ls"));
    shell.run("cd a");
    assertEquals("chelsea\n", shell.run("cat champions.txt"));
    shell.run("cd ..");
    shell.run("mkdir c");
    //move a directory in a directory
    shell.run("mv a c");
    assertEquals("/: b relegation.txt c \n", shell.run("ls"));
    shell.run("cd c");
    shell.run("cd a");
    assertEquals("a: champions.txt \n", shell.run("ls"));
  }
  

}
