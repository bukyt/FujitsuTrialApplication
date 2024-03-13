package com.trialTask.deliveryapp.web;

import com.trialTask.deliveryapp.DAO.DataManagerDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Class to run the fetch request from ilmateenistus observations file
 */
@Component
public class QueryExecutionTask implements Runnable {
    //uses DeliveryDAO to save new data
    @Autowired
    private DataManagerDAO DeliveryDAO;
    //empty constructor so it can be initialized
    public QueryExecutionTask() {
    }

    /**
     * run task, scheduled to run at every hour on the 15th minute wih cron
     * Saves the data from observations to the h2 database
     */
    @Override
    @Scheduled(cron="0 15 * * * *")
    public void run() {
        System.out.println("running fetch");
        try {
            // Define the URLs for the weather data
            String URL = "https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php";
            // Fetch and save weather data for needed cities
            String data = fetchDataFromUrl(URL);
            String[] cities = data.split("NEW FOUND:");
            for (String city : cities) {
                String[] cityData = city.split(",");
                if(city.length()==0) continue;
                //Tallinn-Harku,26038,,0.7,2.2,
                //name,code,phenomenon,airtemp,windspeed,
                DeliveryDAO.insertData(cityData[0],Integer.parseInt(cityData[1]),Float.parseFloat(cityData[3]),Float.parseFloat(cityData[4]),cityData[2]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Extra function to clean up the data for insertion to the database
     * @param urlString the url of the site that the data is fetched from
     * @return cleaned up data that can be used to insert into the database
     * @throws IOException incase the Data cannot be accessed or the connection is refused
     */
    private String fetchDataFromUrl(String urlString) throws IOException {
        //Creates a URL object
        URL url = new URL(urlString);

        //Opens a connection to the URL
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        //Sets request method
        connection.setRequestMethod("GET");

        //Gets the response code
        int responseCode = connection.getResponseCode();
        String[] needed = new String[]{"name","wmocode","phenomenon","airtemperature","<windspeed>"};
        String[] cities = new String[]{"<name>Pärnu</name>","<name>Tallinn-Harku</name>","<name>Tartu-Tõravere</name>"};
        //Reads the response from the server
        StringBuilder response = new StringBuilder();
        int shouldLineBeAdded=0;
        //Each line is handled separately, checks for needed stations and adds their selected information to a final string that is used to add data to the database
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line=line.strip();
                for(String city:cities){
                    if(line.equals(city)){
                        shouldLineBeAdded=1;
                        response.append("NEW FOUND:");//Added so the stations can easily be separated later
                    }
                }
                if(shouldLineBeAdded==1){
                    for (String name : needed) {
                        if(line.contains(name)){
                            response.append(line.split("<|>")[2]+",");
                        }
                    }
                }
                if(line.contains("</station>"))
                    shouldLineBeAdded=0;
            }
        }

        // Close the connection
        connection.disconnect();
        return response.toString();
    }
}
