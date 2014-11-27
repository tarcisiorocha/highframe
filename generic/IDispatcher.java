package comanche.fraclet;

public interface IDispatcher {
  Response handleRequest (Request r) throws java.io.IOException;
}
