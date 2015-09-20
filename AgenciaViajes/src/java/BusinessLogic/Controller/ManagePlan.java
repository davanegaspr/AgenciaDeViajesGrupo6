/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BusinessLogic.Controller;

import DataAccess.DAO.PlanDAO;
import DataAccess.Entity.Plan;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Richar
 */
public class ManagePlan {

    public ArrayList<Plan> getPlans() {
        PlanDAO planDAO = new PlanDAO();
        return planDAO.getPlans();
    }

    public void createPlan(String name, String departureCity, String arrivalCity, String departureDate, String returnDate, String modeTransport, double baseCostByAdult, double baseCostByChild, long hotelId) {
        Plan plan = new Plan();
        plan.setName(name);
        plan.setArrivalCity(arrivalCity);
        plan.setBaseCostByAdult(baseCostByAdult);
        plan.setBaseCostByChild(baseCostByChild);
        plan.setDepartureCity(departureCity);
        plan.setDepartureDate(departureDate);
        plan.setModeTransport(modeTransport);
        plan.setHotelId(hotelId);
        plan.setReturnDate(returnDate);
        PlanDAO planDAO = new PlanDAO();
        Plan planE = planDAO.persist(plan);
        if(planE != null){
                renderShowPlans(); 
        }
        else{
            
        }
    }

    public void renderShowPlans() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest origRequest = (HttpServletRequest)context.getExternalContext().getRequest();
        String contextPath = origRequest.getContextPath();
        try {
            FacesContext.getCurrentInstance().getExternalContext()
            .redirect(contextPath  + "/faces/showPlans.xhtml");
            } catch (IOException e) {
            } 
    }

    public boolean eliminatePlan(long planId) {
        PlanDAO planDAO = new PlanDAO();
        return planDAO.eliminatePlan(planId);    
    }

    public void renderPlanEdit() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest origRequest = (HttpServletRequest)context.getExternalContext().getRequest();
        String contextPath = origRequest.getContextPath();
        try {
            FacesContext.getCurrentInstance().getExternalContext()
            .redirect(contextPath  + "/faces/editPlan.xhtml");
            } catch (IOException e) {
            }
    }

    public boolean editPlan(String name, String departureCity, String arrivalCity, String date, String date0, String modeTransport, Double baseCostByAdult, Double baseCostByChild, long hotelId) {
        PlanDAO planDAO = new PlanDAO();
        HttpSession session = Util.getSession();  
        return planDAO.editPlan((long)session.getAttribute("planIdEdit"), name, departureCity, arrivalCity, date, date0, modeTransport, baseCostByAdult, baseCostByChild, hotelId);
    }

    public boolean isDate(String departureDate) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
  	
  	try {
	  	String dat = format.format(format.parse(departureDate));
	  	return true;
	
	}catch (Exception e) {
	  	return false;
	  }
    }

    public boolean departureDateValidator(String departureDate) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
  	String d2 = format.format(new Date());  	
  	try {
  	  	Date currentDate = format.parse(d2);
  	  	Date date = format.parse(departureDate);
  		if(currentDate.before(date)){
	  		String dat = format.format(format.parse(departureDate));//Se valida si getDepartureDate() o getReturnDate() Es valido con respecto al formato definido
	  		return true;
	 	}
	  	else {
	  		return false;
	  	}
	}catch (Exception e) {
	  	return false;
        }
    }

    public boolean dateValidator(String departureDate, String returnDate) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");	
  	try {
  	  	Date departureDate2 = format.parse(departureDate);
  	  	Date returnDate2 = format.parse(returnDate);
  		if(departureDate2.before(returnDate2)){
	  		String dat = format.format(format.parse(departureDate));//Se valida si getDepartureDate() o getReturnDate() Es valido con respecto al formato definido
	  		return true;
	 	}
	  	else {
	  		return false;
	  	}
	}catch (Exception e) {
	  	return false;
	  }
	
}
    }
