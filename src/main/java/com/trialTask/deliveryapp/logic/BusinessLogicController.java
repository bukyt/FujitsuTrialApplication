package com.trialTask.deliveryapp.logic;

import com.trialTask.deliveryapp.DAO.DataManagerDAO;
import com.trialTask.deliveryapp.Exceptions.WeatherException;
import com.trialTask.deliveryapp.entity.WeatherData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
@Component
public class BusinessLogicController {
    /**
     * Business rules to calculate regional base fee (RBF):
     * In case City = Tallinn and:
     * Vehicle type = Car, then RBF = 4
     * Vehicle type = Scooter, then RBF = 3,5
     * Vehicle type = Bike, then RBF = 3
     * In case City = Tartu and:
     * Vehicle type = Car, then RBF = 3,5
     * Vehicle type = Scooter, then RBF = 3
     * Vehicle type = Bike, then RBF = 2,5
     * In case City = Pärnu and:
     * Vehicle type = Car, then RBF = 3
     * Vehicle type = Scooter, then RBF = 2,5
     * Vehicle type = Bike, then RBF = 2
     * Business rules to calculate extra fees for weather conditions:
     * Extra fee based on air temperature (ATEF) in a specific city is paid in case Vehicle type =
     * Scooter or Bike and:
     * Air temperature is less than -10? C, then ATEF = 1
     * Air temperature is between -10? C and 0? C, then ATEF = 0,5
     * Extra fee based on wind speed (WSEF) in a specific city is paid in case Vehicle type = Bike
     * and:
     * Wind speed is between 10 m/s and 20 m/s, then WSEF = 0,5
     * In case of wind speed is greater than 20 m/s, then the error message "Usage of selected vehicle
     * type is forbidden" has to be given
     * Extra fee based on weather phenomenon (WPEF) in a specific city is paid in case Vehicle
     * type = Scooter or Bike and:
     * Weather phenomenon is related to snow or sleet, then WPEF = 1
     * Weather phenomenon is related to rain, then WPEF = 0,5
     * In case the weather phenomenon is glaze, hail, or thunder, then the error message "Usage of
     * selected vehicle type is forbidden" has to be given
     */
    @Autowired
    public DataManagerDAO DeliveryDao;
    String[] cities= new String[]{"Tallinn-Harku","Tartu-Tõravere","Pärnu"};
    HashMap<String,double[]> vehicles= new HashMap<>();

    /**
     * Setup of the RBF of each city and vehicle so that they can be easily managed
     */
    @Bean
    public void BusinessLogicController(){
        vehicles.put("car",new double[]{4,3.5,3});
        vehicles.put("scooter",new double[]{3.5,3,2.5});
        vehicles.put("bike",new double[]{3,2.5,2});
    }

    /**
     * Calculates the fee according to the rules listed above
     * @param name Name of the city that the fee is calculated for
     * @param vehicle Name of the vehicle the fee is calculated for
     * @return Double of total sum that the different fees amount to
     * @throws WeatherException when the selected vehicle cannot operate under the conditions at the location
     */
    public Double calculateFee(String name,String vehicle) throws WeatherException{
        vehicle=vehicle.toLowerCase();
        WeatherData weatherData = DeliveryDao.selectLatestByLocation(name);
        double RBF=0;
        double WPEF=0;
        double WSEF=0;
        double ATEF=0;
        for (int i = 0; i < cities.length; i++) {
            if(name.equals(cities[i])){
                RBF=vehicles.get(vehicle)[i];
                break;
            }
        }
        float temperature=weatherData.getAIR();
        float wind = weatherData.getWIND();
        String phenomenon = weatherData.getPHENOMENON();
        if(vehicle.equals("scooter")||vehicle.equals("bike")){
            if(temperature<0)
                ATEF=0.5;
            if(temperature<-10)
                ATEF=1;
            if(wind>=20)
                throw new WeatherException();
            if(wind>=10)
                WSEF=0.5;
            if(phenomenon.equals("EXTREME"))
                throw new WeatherException();
            if(phenomenon.equals("SLEET"))
                WPEF=1;
            if(phenomenon.equals("RAIN"))
                WPEF=0.5;
        }
        return RBF+WSEF+WPEF+ATEF;
    }

    /**
     * Copy of calculateFee, used for tests to use with different phenomenon
     * @param name name of location
     * @param vehicle vehicle used
     * @param phenomenon phenomenon at the location
     * @return Double of different fees
     * @throws WeatherException incase selected vehicle may not be used at the time
     */
    public Double calculateFeeWithPhenomenon(String name,int wmocode,float temperature, float wind,String phenomenon, String vehicle) throws WeatherException{
        vehicle=vehicle.toLowerCase();
        double RBF=0;
        double WPEF=0;
        double WSEF=0;
        double ATEF=0;
        vehicles =new HashMap<>();
        vehicles.put("car",new double[]{4,3.5,3});
        vehicles.put("scooter",new double[]{3.5,3,2.5});
        vehicles.put("bike",new double[]{3,2.5,2});
        for (int i = 0; i < cities.length; i++) {
            if(name.equals(cities[i])){
                RBF=vehicles.get(vehicle)[i];
                break;
            }
        }
        if(vehicle.equals("scooter")||vehicle.equals("bike")){
            if(temperature<0)
                ATEF=0.5;
            if(temperature<-10)
                ATEF=1;
            if(wind>=20)
                throw new WeatherException();
            else if(wind>=10)
                WSEF=0.5;
            if(phenomenon.equals("EXTREME"))
                throw new WeatherException();
            else if(phenomenon.equals("SLEET"))
                WPEF=1;
            else if(phenomenon.equals("RAIN"))
                WPEF=0.5;
        }
        return RBF+WSEF+WPEF+ATEF;
    }
}
