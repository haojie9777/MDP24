package connection;

import map.MapPanel;

public class CameraInstructionTest {
    public static void main(String[] args) {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        boolean connected = false;

        while (!connected) {
            connected = connectionManager.connectToRPi();
        }
        try {
            //connectionManager.start();
            while (connected) {
                SocketConnection socketConnection = SocketConnection.getInstance();
                System.out.println("CONNECTED TO SOCKET");
                socketConnection.sendMessage("C");
                System.out.println("camera instruction send");
                break;

            }
        } catch (Exception e) {
            connectionManager.stopCM();
            System.out.println("ConnectionManager is stopped");
        }
    }
}
