package poweroptions;

import mousekeyboard.MouseKeyboardControl;

public class PowerOptions {

    String os;
    Runtime runtime;

    public PowerOptions() {
        os = System.getProperty("os.name");
        runtime = Runtime.getRuntime();
    }

    public void shutdown() {
        try {
            System.out.println(os);
            if (os.contains("Windows")) {
                runtime.exec("shutdown -s");
            } else if ("Linux".equals(os)) {
                runtime.exec("sudo shutdown -h now");
            } else {
                System.out.println("Unsupported OS.");
            }
        } catch (Exception e) {
            System.out.println("Shutdown Error.");
            e.printStackTrace();
        }

    }

    public void restart() {
        try {
            if (os.contains("Windows")) {
                runtime.exec("shutdown -r");
            } else if ("Linux".equals(os)) {
                runtime.exec("sudo shutdown -r now");
            } else {
                System.out.println("Unsupported OS.");
            }
        } catch (Exception e) {
            System.out.println("Restart error.");
            e.printStackTrace();
        }

    }

    public void suspend() {
        try {
            if (os.contains("Windows")) {
                runtime.exec("Rundll32.exe powrprof.dll,SetSuspendState Sleep");
            } else {
                System.out.println("Unsupported OS.");
            }
        } catch (Exception e) {
            System.out.println("Suspend error.");
            e.printStackTrace();
        }
    }

    public void lock() {
        try {
            if ("Linux".equals(os) || "Mac OS X".equals(os)) {
                new MouseKeyboardControl().ctrlAltL();
            } else if (os.contains("Windows")) {
                runtime.exec("Rundll32.exe user32.dll,LockWorkStation");
            } else {
                System.out.println("Unsupported OS.");
            }
        } catch (Exception e) {
            System.out.println("PC lock error.");
            e.printStackTrace();
        }

    }

    public static void main(String args[]) {
        PowerOptions powerOff = new PowerOptions();
        powerOff.lock();
    }
}
