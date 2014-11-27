package comanche.fraclet;

import OpenCOM.IUnknown;

public interface IAnalyzer extends IUnknown {
  Response handleRequest (Request r) throws java.io.IOException;
}
