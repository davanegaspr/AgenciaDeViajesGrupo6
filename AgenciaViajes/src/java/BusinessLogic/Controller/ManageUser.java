/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BusinessLogic.Controller;

import DataAccess.DAO.UserDAO;
import DataAccess.Entity.User;
import com.novell.ldap.LDAPException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import javax.servlet.http.HttpServletRequest;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpSession;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;


/**
 *
 * @author Richar
 */
public class ManageUser implements Serializable {
    /**
     *
     * @param username
     * @param firstname
     * @param lastname
     * @param password
     * @param email
     * @param role
     * @param phone
     * @param balance
     * @param documentType
     * @param document
     * @return
     * @throws java.io.IOException
     */
    
    private static final long serialVersionUID = 1L;
    public void createUser(String username, String firstname, String lastname, String password, String email, String role, String phone, double balance, String documentType, String document, String p) throws NoSuchAlgorithmException, IOException, NamingException, SystemException, NotSupportedException, RollbackException, HeuristicMixedException, HeuristicRollbackException, LDAPException{
        
        LoginLDAP l = new LoginLDAP();
        String valid =l.addUserLDAP(firstname+" "+lastname, p, email);
        if("El usuario ha sido creado".equals(valid))  
        {
        
            UserDAO userDAO = new UserDAO();
            UserTransaction transaction = (UserTransaction)new InitialContext().lookup("java:comp/UserTransaction");
            HttpSession session = Util.getSession();         
            long userId;        
            if(session.getAttribute("userId")==null){
                userId=1;
            }
            else{
                userId=(long)session.getAttribute("userId") +1;
            }
            transaction.begin();
            boolean save = userDAO.persist(username, firstname, lastname, password, email,role, phone,  balance, documentType, document, userId);
            transaction.commit();        
            if(save){
                    UserDAO.query(email);
                    renderIndex(); 
            }
            }
        else{
            System.out.println(valid);
        }   
    }
    
    public void createUser2(String username, String firstname, String lastname, String password, String email, String role, String phone, double balance, String documentType, String document) throws NoSuchAlgorithmException, IOException, NamingException, NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException{
        UserDAO userDAO = new UserDAO();
        UserTransaction transaction = (UserTransaction)new InitialContext().lookup("java:comp/UserTransaction");
        HttpSession session = Util.getSession();         
        long userId;        
        if(session.getAttribute("userId")==null){
            userId=1;
        }
        else{
            userId=(long)session.getAttribute("userId") +1;
        }
        transaction.begin();
        boolean save = userDAO.persist(username, firstname, lastname, password, email,role, phone,  balance, documentType, document,userId);
        transaction.commit();        
        if(save){
                renderShowUsers();
        }
        else{
            
        } 
    }
    
    public void renderIndex(){
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest origRequest = (HttpServletRequest)context.getExternalContext().getRequest();
        String contextPath = origRequest.getContextPath();
        try {
            FacesContext.getCurrentInstance().getExternalContext()
            .redirect(contextPath  + "/faces/index.xhtml");
            } catch (IOException e) {
            }
    
    }
    public void renderSignup(){
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest origRequest = (HttpServletRequest)context.getExternalContext().getRequest();
        String contextPath = origRequest.getContextPath();
        try {
            FacesContext.getCurrentInstance().getExternalContext()
            .redirect(contextPath  + "/faces/signup.xhtml");
            } catch (IOException e) {
            }  
    }
    
    public void renderShowUsers(){
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest origRequest = (HttpServletRequest)context.getExternalContext().getRequest();
        String contextPath = origRequest.getContextPath();
        try {
            FacesContext.getCurrentInstance().getExternalContext()
            .redirect(contextPath  + "/faces/showUsers.xhtml");
            } catch (IOException e) {
            }  
    }
    public void renderUserEdit(){
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest origRequest = (HttpServletRequest)context.getExternalContext().getRequest();
        String contextPath = origRequest.getContextPath();
        try {
            FacesContext.getCurrentInstance().getExternalContext()
            .redirect(contextPath  + "/faces/editUser.xhtml");
            } catch (IOException e) {
            }  
    }
    
    public void renderProfile(){
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest origRequest = (HttpServletRequest)context.getExternalContext().getRequest();
        String contextPath = origRequest.getContextPath();
        try {
            FacesContext.getCurrentInstance().getExternalContext()
            .redirect(contextPath  + "/faces/viewProfile.xhtml");
            } catch (IOException e) {
            }  
    }
    public boolean login(String email, String password) throws SQLException {
        String passwordHash = sha256(password);
        boolean result = UserDAO.login(email, passwordHash);  
        if(result){
            UserDAO.query(email);       
            return result;  
        }
        else{
        return result;
        }
    } 
    public void logout() {
        HttpSession session = Util.getSession();
        session.invalidate();
        renderIndex();
    }   
    public String sha256(String base) {
         try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String text = base;
            md.update(text.getBytes("UTF-8")); // Change this to "UTF-16" if needed
            byte[] digest = md.digest();
            BigInteger bigInt = new BigInteger(1, digest);
            String output = bigInt.toString(16);           
            return output;
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
        }
        return null;
    }

    public boolean passwordCheck(String password, String password2) {
        return password.equals(password2);
    }

    public boolean validateEmail(String email) {
        return UserDAO.validateEmail(email); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean validateUsername(String username) {
        return UserDAO.validateUsername(username); //To change body of generated methods, choose Tools | Templates.
    }

    public void updateUser(String firstname, String lastname, String role, String phone) {
        HttpSession session = Util.getSession();
        UserDAO userDAO = new UserDAO();
        if(userDAO.updateUser((long)session.getAttribute("userId"), firstname, lastname, role, phone)){
                UserDAO.query((String)session.getAttribute("email"));
        } 
    }
    
    public void updateBalance(double balance) {
        HttpSession session = Util.getSession();        
        UserDAO userDAO = new UserDAO();
        if(userDAO.updateBalance((long)session.getAttribute("userId"), (double)session.getAttribute("balance") + balance)){
                UserDAO.query((String)session.getAttribute("email"));
        } 
    }

    public void updatePassword(String newPassword) {
        HttpSession session = Util.getSession();        
        UserDAO userDAO = new UserDAO();
        if(userDAO.updatePassword((long)session.getAttribute("userId"), newPassword)){
                UserDAO.query((String)session.getAttribute("email"));
        }
        
    }

    public ArrayList<User> getUsers() {        
        UserDAO userDAO = new UserDAO();
        return userDAO.getUsers();
    }

    public boolean eliminateUser(Long userId) {
        UserDAO userDAO = new UserDAO();
        return userDAO.eliminateUser(userId);
    }

    public boolean editUser(String username, String firstname, String lastname, String password, String email, String role, String phone, long l, String documentType, String document) {
        UserDAO userDAO = new UserDAO();
        HttpSession session = Util.getSession();  
        return userDAO.editUser((long)session.getAttribute("userIdEdit"), username, firstname, lastname, password, email, role, phone, l, documentType, document);
    }

}
