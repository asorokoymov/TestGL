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
        glThread = new Thread(new GUIRunner());
        glThread.run();
       /* try {
            BspFile file = BspFile.open("src/main/resources/map.bsp");
            file.read();
        } catch (Throwable e) {
            log.error(e.getLocalizedMessage());
        }*/
    }

}