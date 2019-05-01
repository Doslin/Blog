package thread;

import java.util.concurrent.*;

public class TestThreadPool {
    public static void main(String[] args){
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(5, 10,
                2, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(5));
        for (int i = 0; i < 15; i++) {
            MyTask myTask = new MyTask(i);
            poolExecutor.execute(myTask);
            System.out.println("�̳߳����߳���Ŀ "+poolExecutor.getPoolSize()+" " +
                    ",�����еȴ�ִ�е�������Ŀ��"+poolExecutor.getQueue().size()
            +" ���Ѿ�ִ����ɵ�������Ŀ��"+poolExecutor.getCompletedTaskCount());
        }
        poolExecutor.shutdown();

      Runnable task =   ()->{
            System.out.println("hello World!");
        };
      new Thread( ()->{
          System.out.println("hello World!");
      }).start();
    }
}
