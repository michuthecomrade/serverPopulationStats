package serverPopulationStats;

import com.sasha.eventsys.SimpleEventHandler;
import com.sasha.eventsys.SimpleListener;
import com.sasha.reminecraft.Logger;
import com.sasha.reminecraft.ReMinecraft;
import com.sasha.reminecraft.api.RePlugin;
import com.sasha.reminecraft.api.event.ChatRecievedEvent;
import com.sasha.reminecraft.client.ReClient;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Main extends RePlugin implements SimpleListener {
    private Logger logger = new Logger("ServerPopulationStats");
    private boolean inQueue = true;
    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);

    @Override
    public void onPluginInit() {
        this.getReMinecraft().EVENT_BUS.registerListener(this);
        logger.log("MICHU: Plugin serverPopulationStats initiated. Current time is: " + LocalDateTime.now().toString());
    }

    @SimpleEventHandler
    private void detectQueueStatus(ChatRecievedEvent e) {
        if (e.messageText.startsWith("<")) {
            inQueue = false;
        }
        if (e.messageText.startsWith("2b2t is full")
           || e.messageText.startsWith("position in queue")) {
            inQueue = true;
        }

    }

    @Override
    public void onPluginEnable() {
        logger.log("MICHU: Plugin serverPopulationStats enabled");
        executor.scheduleAtFixedRate(() -> {
            if (ReClient.ReClientCache.INSTANCE.playerListEntries.size() != 0 && !inQueue) {
                writeToFile();
            }
        }, 5L, 60L, TimeUnit.SECONDS);

    }

    /**
     *  Method to be called at a fixed rate,
     *  writes everything to a file and switches to a new file if needed
     */
    private void writeToFile() {
        //Detecting if the date changed
        String data = LocalDateTime.now().getHour() + ":" + LocalDateTime.now().getMinute() + " " + ReClient.ReClientCache.INSTANCE.playerListEntries.size();
        String filename = "Population Data: " + LocalDateTime.now().getYear() + "-" + LocalDateTime.now().getMonthValue() + "-" + LocalDateTime.now().getDayOfMonth() + ".txt";
        logger.log("MICHU: Writing current populaton data to file");
        try {
            FileWriter fw = new FileWriter(filename, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.newLine();
            bw.write(data);
            bw.flush();
            fw.flush();
            bw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    @Override
    public void onPluginDisable() {
        logger.log("MICHU: Plugin serverPopulationStats disabled");
    }

    @Override
    public void registerCommands() {

    }

    @Override
    public void registerConfig() {

    }
}
