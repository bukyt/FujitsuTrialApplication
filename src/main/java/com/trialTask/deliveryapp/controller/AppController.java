package com.trialTask.deliveryapp.controller;

import com.trialTask.deliveryapp.DAO.DataManagerDAO;
import com.trialTask.deliveryapp.Exceptions.WeatherException;
import com.trialTask.deliveryapp.logic.FeeResponse;
import com.trialTask.deliveryapp.logic.BusinessLogicController;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;


/**
 * Controller class for the application, directs requests to GET and POST in the right places for the application to work
 *
 */
@Controller
public class AppController {
    @Autowired
    private DataManagerDAO DeliveryDao;
    @Autowired
    private BusinessLogicController BLC;
    private FeeResponse feeResponse;


    /**
     * Used to test what user is logged in on the application
     * @param authentication authentication token to allow access to the application
     * @return String that tells the name of the account that is authenticated
     */
    @GetMapping("/user")
    public String getUser(Authentication authentication) {
        return "Hello Spring Security"+authentication.getName();
    }

    /**
     * the default page a user is redirected to when logged in
     * @return index.html page under resources
     */

    @GetMapping(value="/")
    public String index(){
        return "index.html";
    }

    /**
     * The redirect from index.html when a form is filled
     * @param city the city name gotten from index.html filled fields
     * @param vehicle the vehicle type gotten from index.html filled fields
     * @param model the model attributes holder so html can display the value calculated with BusinessLogicController
     * @param session session attributes holder, holds the displayable value calculated with BusinessLogicController
     * @return submit.html page if no errors ocurred, error.html otherwise
     */
    @RequestMapping(method = {RequestMethod.POST, RequestMethod.GET}, value="/submit")
    public String submit(@RequestParam(value = "city") String city,
                         @RequestParam(value = "vehicle") String vehicle,
                         Model model,
                         HttpSession session){
        feeResponse=new FeeResponse(BLC.calculateFee(city, vehicle));
        try{
            session.setAttribute("sessionFee",Double.toString(feeResponse.getFee()));
            return "submit.html";
        }catch(WeatherException e){
            return "weathererror.html";
        }
    }

    /**
     * mapping for error page redirect
     * @return error.html page
     */
    @RequestMapping(method = {RequestMethod.GET}, value="/error")
    public String error(){
        return "error.html";
    }
}