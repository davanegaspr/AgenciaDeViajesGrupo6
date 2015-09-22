/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataAccess.DAO;

import DataAccess.Entity.Plan;
import DataAccess.Entity.Tickets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Richar
 */
public class TicketDAO {

    public Plan getPlan(long planId) {
        Connection con = null;
        PreparedStatement ps = null;
        Plan plan = new Plan();
        try {
            con = Database.getConnection();
            ps = con.prepareStatement(
                    "select * from plan WHERE planId =? ");
            ps.setString(1, String.valueOf(planId));
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                plan.setPlanId(rs.getLong("planId"));
                plan.setName(rs.getString("name"));
                plan.setDepartureCity(rs.getString("departureCity"));
                plan.setArrivalCity(rs.getString("arrivalCity"));
                plan.setDepartureDate(rs.getString("departureDate"));
                plan.setReturnDate(rs.getString("returnDate"));
                plan.setModeTransport(rs.getString("modeTransport"));
                plan.setBaseCostByAdult(rs.getDouble("baseCostByAdult"));
                plan.setBaseCostByChild(rs.getDouble("baseCostByChild"));
                plan.setHotelId(rs.getLong("hotelId"));           
            }          
        } catch (Exception ex) {
            System.out.println("Error in login() -->" + ex.getMessage());
        } finally {
            Database.close(con);
        } 
        return plan;
    }

    public Tickets createTicket(long userId, long planId, int quantityAdult, int quantityChild) {
        
        Connection con = null;
        PreparedStatement ps = null;
        Tickets ticket = new Tickets();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm");
	   //get current date time with Date()
	   Date date = new Date();
        float price;
        try {
            con = Database.getConnection();
            ps = con.prepareStatement(
                    "INSERT INTO tickets VALUES (null,?,?,?,?,?)");
            ps.setString(1, String.valueOf(dateFormat.format(date)));
            ps.setString(2, String.valueOf(dateFormat.format(date)));
            ps.setString(3, String.valueOf(planId));
            ps.setString(4, String.valueOf(userId));
            price = (quantityAdult * 3) + (quantityChild * 1);
            ps.setString(5, String.valueOf(price));
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                ticket.setIdPlan(rs.getLong("idPlan"));
                ticket.setDateBuy(rs.getDate("Date_Buy"));
                ticket.setDateStart(rs.getDate("Date_Start"));
                ticket.setIdUser(rs.getLong("idUser"));
                ticket.setPrice(rs.getFloat("price"));          
            }          
        } catch (Exception ex) {
            System.out.println("Error in Ticket creation() -->" + ex.getMessage());
        } finally {
            Database.close(con);
        }
        
        return ticket;
    }
    
}
