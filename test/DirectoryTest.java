package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import fileAndDirectory.Directory;
import fileAndDirectory.File;

public class DirectoryTest {

  Directory directory;
  File file;
  String dirName;
  String fileName;

  @Before
  public void setUp() {
    dirName = "dir";
    fileName = "file";
    directory = new Directory(dirName);
    file = new File(fileName);

  }

  @Test
  public void testGetFile() {
    directory.add(file);
    String randomFile = "randomFile";
    assertEquals(null, directory.getFile(randomFile));
    assertEquals(file, directory.getFile(fileName));
  }

  @Test
  public void testExists() {
    directory.add(file);
    String randomFile = "randomFile";
    assertEquals(true, directory.exists(fileName));
    assertEquals(false, directory.exists(randomFile));
  }

  @Test
  public void testGetDirectory() {
    dirName = "babyDir";
    directory = new Directory(dirName);
    directory.add(directory);
    String randomDir = "stuff";
    assertEquals(directory, directory.getDirectory(dirName));
    assertEquals(null, directory.getDirectory(randomDir));

  }
}
