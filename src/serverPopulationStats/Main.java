package serverPopulationStats;

import com.sasha.eventsys.SimpleEventHandler;
import com.sasha.eventsys.SimpleListener;
import com.sasha.reminecraft.Logger;
import com.sasha.reminecraft.ReMinecraft;
import com.sasha.reminecraft.api.RePlugin;
import com.sasha.reminecraft.api.event.ChatRecievedEvent;
import com.sasha.reminecraft.client.ReClient;

import java.io.*;
import java.time.*;
import java.util.Timer;
import java.util.TimerTask;


public class Main extends RePlugin implements SimpleListener {
    private Logger logger = new Logger("ServerPopulationStats");
    private boolean inQueue = true;
    @Override
    public ReMinecraft getReMinecraft() {
        return super.getReMinecraft();
    }

    @Override
    public void onPluginInit() {
        this.getReMinecraft().EVENT_BUS.registerListener(this);
        logger.log("MICHU: Plugin serverPopulationStats initiated. Current time is: " + LocalDateTime.now().toString());
    }
    @SimpleEventHandler
    private void inQueue(ChatRecievedEvent e){
        if(e.messageText.startsWith("<")){
            inQueue=false;
        }
        if (e.messageText.startsWith("2b2t is full")) {
            inQueue=true;
        }

    }

    @Override
    public void  onPluginEnable() {
        logger.log("MICHU: Plugin serverPopulationStats enabled");

        Timer michutimer = new Timer();

        michutimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(ReClient.ReClientCache.INSTANCE.playerListEntries.size() !=0&& !inQueue){
                    writeToFile();
                }

            }
        }, 5000,  60 * 1000 /*1 minute */);

    }

    //this is the method that I wanna call on a timer; it takes the current population numbers and writes them to a file
    private void writeToFile() {
        //Detecting if the date changed
        String data = LocalDateTime.now().getHour() + ":" + LocalDateTime.now().getMinute() + " " + ReClient.ReClientCache.INSTANCE.playerListEntries.size();
        String filename ="Population Data: " + LocalDateTime.now().getYear() + "-" + LocalDateTime.now().getMonthValue() + "-" + LocalDateTime.now().getDayOfMonth() + ".txt";

        try {
            FileWriter fw = new FileWriter(filename, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.newLine();
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
