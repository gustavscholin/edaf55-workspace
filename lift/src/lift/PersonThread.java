package lift;

public class PersonThread extends Thread {
    private static LiftState ls;

    public PersonThread(LiftState ls) {
        this.ls = ls;
    }

    public void run() {
        while (true) {
            int start = 0;
            int dest = (int)(Math.random()*6 + 1);
            ls.travel(start, dest);

            int delay = 1000*((int)(Math.random()*46.0));
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            ls.travel(dest, start);

            delay = 1000*((int)(Math.random()*3.0 + 3.0));
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
