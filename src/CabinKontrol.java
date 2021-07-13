
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.rmi.UnknownHostException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CabinKontrol {

    private static boolean control = false;
    private static boolean control2 = false;
    static Vector<String> messages = new Vector<String>();

    public static void clientStart() {
        Socket socket = null;
        try {

            int port = 3700;
            socket = new Socket("localhost", port);

            PrintStream ps = new PrintStream(socket.getOutputStream(), true);
            communique(socket, ps);
        } catch (UnknownHostException e) {
            System.out.println("Unknown Host..");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("IO-Problems:...");

        } finally {

        }

    }

    public static void addUser() {
        messages.add("STRT|KabinKontrol|TestUser|Admin|70|1000"); //Cabinet name| user name| user role| failure rate
    }

    public static void addMsg(String slotNr, String bauteilId) {
        messages.add("INIT|" + slotNr + "|" + bauteilId);
    }

    public static void deleteMsg(String slotNr) {
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).contains(slotNr)) {
                messages.remove(i);
                break;
            }
        }
    }

    public static void endInit() {
        messages.add("ENDINIT");
    }

    public static void setControl() {
        control = false;
    }


    private static void communique(Socket socket, PrintStream toServer) throws IOException {
        messages.add("STRTPRE|5");
        for (int i = 1; i <= 20; i++) {
            messages.add("PRETST|" + i);
        }
        messages.add("ENDPRE");
        messages.add("STRTBURNIN");
        messages.add("SETTARGET|70.5|180|3|5"); //Target Temperature | TimeFrame[secs] | ToleranceRate[%]
        messages.add("OPERTEMP");
        messages.add("STRTPING|11");

        for (int i = 1; i <= 20; i++) {
            messages.add("PING|" + i);
        }

        messages.add("STOP");

        messages.add("SETTARGET|-30|210|3|5");
        messages.add("OPERTEMP");
        messages.add("STRTPING|11");

        for (int i = 1; i <= 20; i++) {
            messages.add("PING|" + i);
        }

        messages.add("STOPPING");

        BufferedReader fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        for (String message : messages) {
            if (message.startsWith("STRTBURNIN")) {
                control = true;
            }
            while (control) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(CabinKontrol.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (message.startsWith("PRETEST")) {
                String slt = message.substring(8);
                toServer.println(message);
                long start = System.currentTimeMillis();
                fromServer.readLine();
                long stop = System.currentTimeMillis();
                System.out.println("Slot - " + slt + " answered in " + (stop - start) + " milliseconds");
            } else {
                toServer.println(message);
                String answer = fromServer.readLine();
                System.out.println(answer);
                if (answer != null && answer.startsWith("STOPPING")) {
                    System.exit(0);
                }
            }
        }

    }

}
