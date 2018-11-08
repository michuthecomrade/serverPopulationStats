package serverPopulationStats;

import com.sasha.eventsys.SimpleListener;
import com.sasha.reminecraft.Logger;
import com.sasha.reminecraft.ReMinecraft;
import com.sasha.reminecraft.api.RePlugin;
import com.sasha.reminecraft.client.ReClient;

import java.io.*;
import java.time.*;
import java.util.Timer;
import java.util.TimerTask;


public class Main extends RePlugin implements SimpleListener {
    private Logger logger = new Logger("ServerPopulationStats");

    @Override
    public ReMinecraft getReMinecraft() {
        return super.getReMinecraft();
    }

    @Override
    public void onPluginInit() {

        logger.log("MICHU: Plugin serverPopulationStats initiated. Current time is: " + LocalDateTime.now().toString());
    }

    @Override
    public void onPluginEnable() {
        logger.log("MICHU: Plugin serverPopulationStats enabled");

        Timer michutimer = new Timer();

        michutimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                writeToFile();

            }
        }, 5000, 5 * 60 * 1000 /*5 minutes */);

    }

    //this is the method that I wanna call on a timer; it takes the current population numbers and writes them to a file
    private void writeToFile() {
        //Detecting if the date changed
        String data = LocalDateTime.now().getHour() + ":" + LocalDateTime.now().getMinute() + " " + ReClient.ReClientCache.INSTANCE.playerListEntries.size();
        String filename = "Population Data: " + LocalDateTime.now().getYear() + "-" + LocalDateTime.now().getMonthValue() + "-" + LocalDateTime.now().getDayOfMonth() + ".txt";

        try {
            FileWriter fw = new FileWriter(filename, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(data);
            bw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.log("MICHU: Writing current populaton data to file");


    }


    @Override
    public void onPluginDisable() {

        logger.log("MICHU: Plugin serverPopulationStats disabled");
        logger.log("MICHU: Closing the writer");
    }

    @Override
    public void registerCommands() {

    }

    @Override
    public void registerConfig() {

    }
}
