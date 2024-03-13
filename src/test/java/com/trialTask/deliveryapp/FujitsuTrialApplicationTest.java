package com.trialTask.deliveryapp;


import com.trialTask.deliveryapp.DAO.DataManagerDAO;
import com.trialTask.deliveryapp.Exceptions.WeatherException;
import com.trialTask.deliveryapp.controller.AppController;
import com.trialTask.deliveryapp.logic.BusinessLogicController;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class FujitsuTrialApplicationTest {
	private AppController controller=mock(AppController.class);
	private BusinessLogicController BLC=new BusinessLogicController();
	private DataManagerDAO dao = new DataManagerDAO();
	@Before
	public void setup(){
	}
	@Test
	void ContextLoads() throws Exception{
		assertThat(controller).isNotNull();
	}
	@Test
	public void testAddingItems(){
		DataManagerDAO DaoMock = mock(DataManagerDAO.class);
		DaoMock.insertData("name",12345,0.0f,0.0f,"Light snow shower");
		assertThat(DaoMock).isNotNull();
	}
	@Test
	public void testLogicControllerCalc(){
		double a = BLC.calculateFeeWithPhenomenon("Tartu-Tõravere",12345,-2.1f,4.7f,"SLEET","bike");
		assertThat(a).isEqualTo(4);
	}
	@Test
	public void testWeatherErrorWithWind(){
		boolean thrown=false;
		try{
			double a = BLC.calculateFeeWithPhenomenon("Tartu-Tõravere",12345,-2.1f,120.7f,"SLEET","bike");
		}catch(WeatherException e){
			thrown=true;
		}
		assertThat(thrown).isEqualTo(true);
	}
	@Test
	public void testWeatherErrorWithPhenomenon(){
		boolean thrown=false;
		try{
			double a = BLC.calculateFeeWithPhenomenon("Tartu-Tõravere",12345,-20.1f,0f,"EXTREME","scooter");
		}catch(WeatherException e){
			thrown=true;
		}
		assertThat(thrown).isEqualTo(true);
	}
	@Test
	public void testWeatherErrorWithCar(){
		boolean thrown=false;
		try{
			double a = BLC.calculateFeeWithPhenomenon("Tartu-Tõravere",12345,-20.1f,200f,"EXTREME","car");
		}catch(WeatherException e){
			thrown=true;
		}
		assertThat(thrown).isEqualTo(false);
	}

}
