package commands;

public class CommandError extends Exception {
  public CommandError(String message) {
    super(message);
  }
}
