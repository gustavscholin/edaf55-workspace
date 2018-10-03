package lift;

public class LiftThread extends Thread {

    public void run() {
        while (true) {
            s.awaitPersonsWalkingOnOrOff();
            int to = s.getNextFloor();
            view.moveLift(from, to);
        }
    }
}
