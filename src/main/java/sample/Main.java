package sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sample.bsp.BspFile;
import sample.gui.GUIRunner;

public class Main {

    private Logger log = LoggerFactory.getLogger(Main.class);
    private Thread glThread;

    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        try {
            BspFile file = BspFile.open("C:\\Program Files (x86)\\Half-Life\\valve\\maps\\c2a3.bsp");
            file.read();
            glThread = new Thread(new GUIRunner(file));
            glThread.run();
        } catch (Throwable e) {
            log.error(e.getLocalizedMessage());
        }
    }

}