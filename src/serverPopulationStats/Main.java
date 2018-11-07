package serverPopulationStats;

import com.sasha.eventsys.SimpleListener;
import com.sasha.reminecraft.ReMinecraft;
import com.sasha.reminecraft.api.RePlugin;
import com.sasha.reminecraft.client.ReClient;

import java.io.*;
import java.time.*;
import java.io.PrintWriter;
import java.util.Timer;
import java.util.TimerTask;


public class Main extends RePlugin implements SimpleListener {
    //public classes
    public String filename;
    public int currentDay = 400;
    public String data;
    public PrintWriter writer = null;



    @Override
    public ReMinecraft getReMinecraft() {
        return super.getReMinecraft();
    }

    @Override
    public void onPluginInit() {

        System.out.println("MICHU: Plugin serverPopulationStats initiated. Current time is: "+LocalDateTime.now().toString());
        }
    @Override
    public void onPluginEnable() {
        System.out.println("MICHU: Plugin serverPopulationStats enabled");

        Timer michutimer = new Timer();

        michutimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                writeToFile();

            }
        }, 5000,5*60*1000 /*5 minutes */ );
        
    }
    //this is the method that I wanna call on a timer; it takes the current population numbers and writes them to a file
    public void writeToFile() {
        //Detecting if the date changed

        filename = "Population Data: " + LocalDateTime.now().getYear() + "-" + LocalDateTime.now().getMonthValue() + "-" + LocalDateTime.now().getDayOfMonth() + ".txt";
        data = LocalDateTime.now().getHour() + ":" + LocalDateTime.now().getMinute() + " " + ReClient.ReClientCache.INSTANCE.playerListEntries.size();

        if(currentDay==400){
            currentDay= LocalDateTime.now().getDayOfYear();
            try {
                writer = new PrintWriter(filename, "UTF-8");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        if (currentDay != LocalDateTime.now().getDayOfYear()) {
            System.out.println("MICHU: day change detected. Starting new file.");
            writer.close();
            //Creating a new PrintWriter object
            try {
                writer = new PrintWriter(filename, "UTF-8");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }

        System.out.println("MICHU: Writing current populaton data to file");


        writer.write(data);

    }


    @Override
    public void onPluginDisable() {

        System.out.println("MICHU: Plugin serverPopulationStats disabled");
        System.out.println("MICHU: Closing the writer");
    }

    @Override
    public void registerCommands() {

    }

    @Override
    public void registerConfig() {

    }
}
