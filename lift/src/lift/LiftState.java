package lift;

public class LiftState {

    private int here;
    private int next;
    private int[] waitEntry;
    private int[] waitExit;
    private int load;

    private static LiftView view;
    private boolean goingUp;

    public LiftState(LiftView lv) {
        waitEntry = new int[7];
        waitExit = new int[7];
        view = lv;
        goingUp = true;
    }

    synchronized void travel(int from, int to) {
        try {

            waitEntry[from]++;
            view.drawLevel(from, waitEntry[from]);
            notifyAll();

            while (here != from || load > 3 || here != next) { //|| (from > to ^ !goingUp)) {
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
            while ((waitEntry[here] != 0 && load < 4) || waitExit[here] != 0 || (isQueueEmpty(waitEntry) && isQueueEmpty(waitExit))) {
                wait();
            }

            if (isUpperPartEmpty(waitEntry, here) && isUpperPartEmpty(waitExit, here)) {
                goingUp = false;
            } else if (isLowerPartEmpty(waitEntry, here) && isLowerPartEmpty(waitExit, here)) {
                goingUp = true;
            }

            if (goingUp) {
                next++;
                if (next == 6) {
                    goingUp = false;
                }
            } else {
                next--;
                if (next == 0) {
                    goingUp = true;
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    synchronized int getNextFloor() {
        return next;
    }

    private boolean isQueueEmpty(int[] queue) {
        for (int i : queue) {
            if (i != 0) {
                return false;
            }
        }
        return true;
    }

    private boolean isUpperPartEmpty(int[] queue, int p) {
        for (int i = p; i < queue.length; i++) {
            if (queue[i] != 0) {
                return false;
            }
        }
        return true;
    }

    private boolean isLowerPartEmpty(int[] queue, int p) {
        for (int i = p; i >= 0; i--) {
            if (queue[i] != 0) {
                return false;
            }
        }
        return true;
    }
}
