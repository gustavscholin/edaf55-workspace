package lift;

public class LiftState {

    int here;
    int next;
    int[] waitEntry;
    int[] waitExit;
    int load;

    private static LiftView view;

    public LiftState(LiftView lv) {
        waitEntry = new int[7];
        waitExit = new int[7];
        view = lv;
    }

    synchronized void travel(int from, int to) {
        try {

            waitEntry[from]++;
            view.drawLevel(from, waitEntry[from]);

            while (here != from || load > 3 || here != next) {
                wait();
            }

            waitEntry[from]--;
            view.drawLevel(from, waitEntry[from]);
            waitExit[to]++;
            load++;
            view.drawLift(from, load);
            notifyAll();

            while (here != to || here != next) {
                wait();
            }

            waitExit[to]--;
            load--;
            view.drawLift(to, load);
            notifyAll();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    synchronized void awaitPersonsWalkingOnOrOff() {
        try {
            here = next;
            notifyAll();
            while (waitEntry[here] != 0 || waitExit[here] != 0) {
                wait();
            }
            next = (next + 1) % 7;

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int getNextFloor() {
        return next;
    }
}
