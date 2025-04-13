import java.text.SimpleDateFormat;
import java.util.Date;

class Clock {
    private String currentTime;
    private final SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");

    public synchronized void updateTime() {
        currentTime = formatter.format(new Date());
    }

    public synchronized String getTime() {
        return currentTime;
    }
}

class TimeUpdater extends Thread {
    private final Clock clock;

    public TimeUpdater(Clock clock) {
        this.clock = clock;
    }

    @Override
    public void run() {
        while (true) {
            clock.updateTime();
            try {
                Thread.sleep(950); // Slightly less than a second for better accuracy
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

class TimeDisplay extends Thread {
    private final Clock clock;

    public TimeDisplay(Clock clock) {
        this.clock = clock;
    }

    @Override
    public void run() {
        while (true) {
            System.out.println(clock.getTime());
            try {
                Thread.sleep(1000); // Display every second
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

public class ClockApplication {
    public static void main(String[] args) {
        Clock clock = new Clock();

        TimeUpdater updater = new TimeUpdater(clock);
        TimeDisplay display = new TimeDisplay(clock);

        // Set priorities
        updater.setPriority(Thread.MIN_PRIORITY);  // Lower priority
        display.setPriority(Thread.MAX_PRIORITY); // Higher priority

        updater.start();
        display.start();
    }
}
