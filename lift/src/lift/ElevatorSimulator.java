package lift;

public class ElevatorSimulator {

    public static void main(String[] args) {
        LiftView lv = new LiftView();
        LiftState ls = new LiftState(lv);
        LiftThread lift = new LiftThread(ls, lv);

        PersonThread gustav = new PersonThread(ls);
        PersonThread erik = new PersonThread(ls);
        PersonThread noah = new PersonThread(ls);

        gustav.start();
        erik.start();
        noah.start();

        lift.start();
    }

}
