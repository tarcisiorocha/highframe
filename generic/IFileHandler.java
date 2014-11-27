package comanche.fraclet;

public interface IFileHandler {
  Response handleRequest (Request r) throws java.io.IOException;
}
