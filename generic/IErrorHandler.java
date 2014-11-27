package comanche.fraclet;

public interface IErrorHandler {
  Response handleRequest (Request r) throws java.io.IOException;
}
