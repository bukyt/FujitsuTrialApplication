package com.trialTask.deliveryapp.DAO;

import com.trialTask.deliveryapp.entity.WeatherData;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.sql.*;

/**
 * Component class to manage database entries and queries
 */
@Component
public class DataManagerDAO implements dao{
    //the connection for the database
    private Connection conn;

    /**
     * Sets up the connection that will be used for altering and accessing the database
     * @throws SQLException incase the connection cannot be established
     */
    @Bean
    public void DataManagerDAO() throws SQLException{
        conn=DriverManager.getConnection("jdbc:h2:~/Fujitsu", "user", "password");
    }

    /**
     * returns the values of every entry in the Weather table, not currently implemented anywhere but is useful for testing purposes
     * @return String queryRes, all entries in Weather table, or exception message if the Query failed for some reason
     */
    public String selectAll(){
        /*CREATE TABLE PUBLIC.WEATHER[
        ID numeric NOT NULL AUTO_INCREMENT,
        NAME varchar(50) NOT NULL,
        WMOCODE numeric(5) NOT NULL,
        AIR numeric(3,1) NOT NULL,
        WIND numeric(3,1) NOT NULL,
        PHENOMENON varchar(50) NOT NULL,
        QUERYTIME TIMESTAMP NOT NULL default CURRENT_TIMESTAMP,
        PRIMARY KEY ID
        ];*/
        //stmt.executeUpdate("insert into WEATHER(NAME,WMOCODE,AIR,WIND,PHENOMENON) values('tere','12345','20','45','SLEET')");
        String queryRes=null;
        try {
            Statement stmt=this.conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT NAME, WMOCODE, AIR, WIND, PHENOMENON, QUERYTIME, ID FROM WEATHER");
            queryRes="";
            while (rs.next()) {
                String[] columns = new String[]{"NAME", "WMOCODE", "AIR", "WIND", "PHENOMENON", "QUERYTIME", "ID"};
                for (String column : columns) {
                    queryRes += rs.getString(column) + " ";
                }
                queryRes += "\n";
            }
        }catch(SQLException e){
            queryRes= e.getMessage();
        }
        return queryRes;
    }

    /**
     * function to select the latest entry of a certain location, so the most up to date information is used
     * @param locationName the name of the city of which someone may inquire about
     * @return WeatherData wd which is the latest entry in the database of locationName
     */
    public WeatherData selectLatestByLocation(String locationName){
        String queryRes=null;
        WeatherData wd=null;
        try {
            Statement stmt=this.conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT NAME, WMOCODE, AIR, WIND, PHENOMENON, QUERYTIME, ID FROM WEATHER WHERE NAME='"+locationName+"' ORDER BY QUERYTIME DESC LIMIT 1");
            queryRes="";
            while (rs.next()) {
                String[] columns = new String[]{"NAME", "WMOCODE", "AIR", "WIND", "PHENOMENON", "QUERYTIME", "ID"};

                for (String column : columns) {
                    queryRes += rs.getString(column) + ";";
                }
                queryRes=queryRes.strip();
            }
            String[] data = queryRes.split(";");
            wd = new WeatherData(data[0],Integer.parseInt(data[1]),Float.parseFloat(data[2]),Float.parseFloat(data[3]),Timestamp.valueOf(data[5]),data[4]);
        }catch(SQLException e){
            queryRes= e.getMessage();
            System.out.println(queryRes);
        }
        return wd;
    }

    /**
     * function to insert new data in the database, accepts information from QueryExecutionTask class, which runs every hour on the 15th minute
     * @param name name of the city or location
     * @param WMOCODE the wmocode of the weather station
     * @param wind windspeed from the location data
     * @param air air temperature from the location data
     * @param phenomenon phenomenon that has been detailed in the location data
     */
    public void insertData(String name, int WMOCODE, float wind, float air, String phenomenon){
        phenomenon=phenomenon.toUpperCase(); //to standardise the casing of weather phenomenon it is needed to be capitalised
        switch(phenomenon) { //switch case to categorize weather phenomenon
            case("BLOWING SNOW"):
            case("HEAVY SNOWFALL"):
            case("MODERATE SNOWFALL"):
            case("LIGHT SNOWFALL"):
            case("MODERATE SLEET"):
            case("LIGHT SLEET"):
            case("MODERATE SNOW SHOWER"):
            case("HEAVY SNOW SHOWER"):
            case("LIGHT SNOW SHOWER"):
            case("SLEET"):
                phenomenon="SLEET"; //anything related to snow gets categorized as SLEET in the database
                break;
            case("HEAVY RAIN"):
            case("MODERATE RAIN"):
            case("LIGHT RAIN"):
            case("HEAVY SHOWER"):
            case("MODERATE SHOWER"):
            case("LIGHT SHOWER"):
            case("RAIN"):
                phenomenon="RAIN"; //anything related to rain gets categorized as RAIN in the database
                break;
            case("THUNDERSTORM"):
            case("HAIL"):
            case("GLAZE"):
            case("THUNDER"):
            case("EXTREME"):
                phenomenon="EXTREME"; //all phenomenon in which scooters and bikes cannot operate are categorized as EXTREME
                break;
            default:
                phenomenon="CLEAR"; //anything else is categorized as clear
                break;
        }
        try{//tries to insert the standardised data into the database
            Statement stmt=this.conn.createStatement();
            //(name+" "+WMOCODE+ " "+wind+" "+air+ " " + phenomenon);
            String statement = "insert into WEATHER(NAME,WMOCODE,AIR,WIND,PHENOMENON) values('"+name+"',"+WMOCODE+","+wind+","+air+",'"+phenomenon+"')";
            //System.out.println(statement);
            stmt.executeUpdate(statement);
        }catch(SQLException e){//catch clause if the execution of the insertion encountered an error
            System.out.println("Exception while updating WEATHER table "+e.getMessage());
        }
    }

    /**
     * Closes the connection to the database
     */
    public void close(){
        try{
            this.conn.close();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
