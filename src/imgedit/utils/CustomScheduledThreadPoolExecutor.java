package imgedit.utils;

import java.util.concurrent.*;

public class CustomScheduledThreadPoolExecutor extends ScheduledThreadPoolExecutor {

    public CustomScheduledThreadPoolExecutor(int corePoolSize) {
        super(corePoolSize);
    }

    // Useful to log exceptions
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);

        if (t == null
                && r instanceof Future<?>
                && ((Future<?>)r).isDone()) {
            try {
                Object result = ((Future<?>) r).get();
            } catch (CancellationException ce) {
                t = ce;
            } catch (ExecutionException ee) {
                t = ee.getCause();
            } catch (InterruptedException ie) {
                // ignore/reset
                Thread.currentThread().interrupt();
            }
        }

        if (t != null)
            System.out.println(t);
    }
}
