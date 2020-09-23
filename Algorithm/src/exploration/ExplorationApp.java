package exploration;

import connection.SocketConnection;
import main.Constants;
import robot.Robot;
import robot.SimulatorRobot;

import java.util.concurrent.atomic.AtomicBoolean;

public class ExplorationApp {
    private Robot robot;
    private int time;
    private int percentage;
    private int speed;
    private boolean image_recognition;

    private static final AtomicBoolean running = new AtomicBoolean(false);
    private static final AtomicBoolean completed = new AtomicBoolean(false);
    private static ExplorationApp explorationApp;
    //private static ExplorationThread thread = null;


    private ExplorationApp(Robot robot, int time, int percentage, int speed, boolean image_recognition) {
        //super("ExplorationThread");
        this.robot = robot;
        this.time = time;
        this.percentage = percentage;
        this.speed = speed;
        this.image_recognition = image_recognition;
        //start();
    }

    public void run() {
        running.set(true);

        // Check if it is the simulator mode
        boolean isSimulated = robot.getClass().equals(SimulatorRobot.class);
        Exploration exploration = new Exploration(robot,time, percentage, speed, image_recognition);
        exploration.explore();
        //exploration.Exploration(robot, time, percentage, speed, image_recognition);
        if (running.get()) {
            completed.set(true);
        }
        else {
            completed.set(false);
        }
        running.set(false);

        if (!isSimulated){
            if (SocketConnection.checkConnection()) {
                // Send the MDF String at the end when it is completed
                String[] arr2 = robot.getMdfString();
                SocketConnection.getInstance().sendMessage("M{\"map\":[{\"explored\": \"" + arr2[0] + "\",\"length\":" + arr2[1] + ",\"obstacle\":\"" + arr2[2] +
                        "\"}]}");
                SocketConnection.getInstance().sendMessage(Constants.END_TOUR);
            }
        }
    }

    public static ExplorationApp getInstance(Robot r, int time, int percentage, int speed, boolean image_recognition) {
        if (explorationApp == null) {
            explorationApp = new ExplorationApp(r, time, percentage, speed, image_recognition);
        }
        return explorationApp;
    }

    public static boolean getRunning() {
        return running.get();
    }

    public static boolean getCompleted() {
        return completed.get();
    }
}