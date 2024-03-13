package com.trialTask.deliveryapp;
import com.trialTask.deliveryapp.DAO.DataManagerDAO;
import com.trialTask.deliveryapp.logic.BusinessLogicController;
import com.trialTask.deliveryapp.web.QueryExecutionTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@ComponentScan("com.trialTask.deliveryapp")
@EntityScan("com.trialTask.deliveryapp")
@EnableScheduling
public class FujitsuTrialApplication{
	@Autowired
	QueryExecutionTask QET;
	@Autowired
	DataManagerDAO DeliveryDAO;
	@Autowired
	private BusinessLogicController BLC;
	//the main method to launch the application
	public static void main(String[] args) {
		SpringApplication.run(FujitsuTrialApplication.class, args);
	}
	//CommandLineRunner to show extra information
	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx){
		return args -> {
			QET = new QueryExecutionTask();
			Class.forName("org.h2.Driver");
			DeliveryDAO = new DataManagerDAO();
			BLC=new BusinessLogicController();
			String[] beanNames= ctx.getBeanDefinitionNames();
		};
	}
}
