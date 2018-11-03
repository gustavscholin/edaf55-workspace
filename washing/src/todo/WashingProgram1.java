package todo;

import done.*;

public class WashingProgram1 extends WashingProgram {

    public WashingProgram1(AbstractWashingMachine mach,
                           double speed,
                           TemperatureController tempController,
                           WaterController waterController,
                           SpinController spinController) {
        super(mach, speed, tempController, waterController, spinController);
    }


    protected void wash() throws InterruptedException {

        myMachine.setLock(true);

        myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_FILL, 9.0/20.0));
        mailbox.doFetch();

        myTempController.putEvent(new TemperatureEvent(this, TemperatureEvent.TEMP_SET, 60.0));
        mailbox.doFetch();

        mySpinController.putEvent(new SpinEvent(this, SpinEvent.SPIN_SLOW));

        Thread.sleep((long) (30*60000/mySpeed));

        mySpinController.putEvent(new SpinEvent(this, SpinEvent.SPIN_OFF));

        myTempController.putEvent(new TemperatureEvent(this, TemperatureEvent.TEMP_IDLE, 0.0));

        myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_DRAIN, 0.0));
        mailbox.doFetch();

        myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_IDLE, 0.0));

        for (int i = 0; i < 5; i++) {
            myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_FILL, 9.0/20.0));
            mailbox.doFetch();

            mySpinController.putEvent(new SpinEvent(this, SpinEvent.SPIN_SLOW));

            Thread.sleep((long) (2*60000/mySpeed));

            mySpinController.putEvent(new SpinEvent(this, SpinEvent.SPIN_OFF));

            myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_DRAIN, 0.0));
            mailbox.doFetch();

            myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_IDLE, 0.0));
        }

        mySpinController.putEvent(new SpinEvent(this, SpinEvent.SPIN_FAST));

        Thread.sleep((long) (5*60000/mySpeed));

        WashingProgram3 ending = new WashingProgram3(myMachine, mySpeed, myTempController, myWaterController, mySpinController);
        ending.start();
    }
}
