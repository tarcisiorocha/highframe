package comanche.fraclet;

import OpenCOM.IUnknown;

public interface IScheduler extends IUnknown{
	void schedule (Runnable task);
}