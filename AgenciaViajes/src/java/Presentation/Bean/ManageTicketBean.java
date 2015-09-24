/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Presentation.Bean;

import BusinessLogic.Controller.ManageTicket;
import BusinessLogic.Controller.ManageHotel;
import BusinessLogic.Controller.Util;
import DataAccess.Entity.Hotel;
import DataAccess.Entity.Plan;
import DataAccess.Entity.Tickets;
import java.io.IOException;
import static java.lang.Boolean.TRUE;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Richar
 */
@ManagedBean(name="manageTicket")
@ViewScoped
public class ManageTicketBean {    
    private int quantityAdult;
    private String enoughBalance;
    private int quantityChild;
    ManageTicket manageTicket = new ManageTicket();    
    HttpSession session = Util.getSession(); 
    private Plan plan = manageTicket.getPlan((long)session.getAttribute("planIdBuy"));
    private Hotel hotel = ManageHotel.getHotel((long)plan.getHotelId());
    ManageTicket mt = new ManageTicket();
    private ArrayList<Tickets> ticketsList = mt.getTickets((long)session.getAttribute("userId"), 1);
    
            //(plan.getBaseCostByAdult() * (int)session.getAttribute("quantityAdult")) + (plan.getBaseCostByChild() * (int)session.getAttribute("quantityChild")) + (hotel.getPrice() * ((int)session.getAttribute("quantityChild") + (int)session.getAttribute("quantityAdult")));

    /**
     * Creates a new instance of ManageTicketBean
     */
    public ManageTicketBean() {
    }    
    
    public void createTicket() throws  IOException, NoSuchAlgorithmException {
        if(manageTicket.balance(plan, hotel) == true) {
            setEnoughBalance("true");
            manageTicket.createTicket((long)session.getAttribute("userId"),plan, getHotel(), (int)session.getAttribute("quantityAdult"), (int)session.getAttribute("quantityChild"), 1);
        }
        else {
            setEnoughBalance("false");
        }
    }    
    public void createReservation() throws  IOException, NoSuchAlgorithmException {
        manageTicket.createTicket((long)session.getAttribute("userId"),plan, getHotel(), (int)session.getAttribute("quantityAdult"), (int)session.getAttribute("quantityChild"), 0);
    }
    public void enoughBalance(){    
    }
    public void renderShowTicket(){
        setQuantityChild(getQuantityChild());        
        setQuantityAdult(getQuantityAdult());
        session.setAttribute("quantityChild", getQuantityChild());
        session.setAttribute("quantityAdult", getQuantityAdult());
        ManageTicket mt = new ManageTicket();        
        mt.renderShowTicket();
    }
    
    /**
     * @return the plan
     */
    public Plan getPlan() {
        return plan;
    }

    /**
     * @param plan the plan to set
     */
    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    /**
     * @return the quantityAdult
     */
    public int getQuantityAdult() {
        return quantityAdult;
    }

    /**
     * @param quantityAdult the quantityAdult to set
     */
    public void setQuantityAdult(int quantityAdult) {
        this.quantityAdult = quantityAdult;
    }

    /**
     * @return the quantityChild
     */
    public int getQuantityChild() {
        return quantityChild;
    }

    /**
     * @param quantityChild the quantityChild to set
     */
    public void setQuantityChild(int quantityChild) {
        this.quantityChild = quantityChild;
    }

    /**
     * @return the hotel
     */
    public Hotel getHotel() {
        return hotel;
    }

    /**
     * @param hotel the hotel to set
     */
    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    /**
     * @return the enoughBalance
     */
    public String getEnoughBalance() {
        return enoughBalance;
    }

    /**
     * @param enoughBalance the enoughBalance to set
     */
    public void setEnoughBalance(String enoughBalance) {
        this.enoughBalance = enoughBalance;
    }

    /**
     * @return the ticketsListList
     */
    public ArrayList<Tickets> getTicketsList() {
        return ticketsList;
    }

    /**
     * @param ticketsList the ticketsListList to set
     */
    public void setTicketsList(ArrayList<Tickets> ticketsList) {
        this.ticketsList = ticketsList;
    }
   
}
