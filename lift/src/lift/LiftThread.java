package lift;

public class LiftThread extends Thread {

    private static LiftState ls;
    private static LiftView lv;

    public LiftThread(LiftState ls, LiftView lv) {
        this.ls = ls;
        this.lv = lv;
    }

    public void run() {
        int from = 0;
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (true) {
            ls.awaitPersonsWalkingOnOrOff();
            int to = ls.getNextFloor();
            lv.moveLift(from, to);
            from = to;
        }
    }
}
