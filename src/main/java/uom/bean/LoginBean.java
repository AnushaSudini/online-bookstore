/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uom.bean;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import uom.model.DatabaseConnectionInfo;
import uom.model.SqlQuery;

/**
 *
 * @author Anusha
 */
@ManagedBean
@SessionScoped
public class LoginBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private String userName;
    private String password;
    private String role;
    private boolean loggedin = false;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isLoggedin() {
        return loggedin;
    }

    public void setLoggedin(boolean loggedin) {
        this.loggedin = loggedin;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String doSubmit() {
        //
        UserDto dto = runQuery(userName, password);
        if (dto == null || dto.getRole() == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid username or password", "Invalid username or password"));
            return "index.xhtml?faces-redirect=true";
        } else {
            loggedin = true;
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("user", dto);
            if (dto.getRole().equals("admin")) {
                return "admin.xhtml?faces-redirect=true";
            } else if (dto.getRole().equals("manager")) {                
                return "manager.xhtml?faces-redirect=true";
            } else if (dto.getRole().equals("customer")) {
                return "customer.xhtml?faces-redirect=true";
            }
        }

        return "index.xhtml?faces-redirect=true";
    }

    private UserDto runQuery(String uName, String uPass) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        UserDto dto = null;
        try {
            con = DatabaseConnectionInfo.getInstance();

            String sql = SqlQuery.AUTHENTICATE;
            //System.out.println(uName + uPass);
            ps = con.prepareStatement(sql);
            ps.setString(1, uName);
            ps.setString(2, uPass);
            rs = ps.executeQuery();
            dto = new UserDto();
            while (rs.next()) {
                dto.setUserName(rs.getString("username"));
                dto.setRole(rs.getString("role"));
                dto.setUserID(rs.getInt("userID"));
                dto.setAddress(rs.getString("address"));
                dto.setCity(rs.getString("city"));

            }

        } catch (SQLException ex) {
            Logger.getLogger(BusinessLogicBean.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(BusinessLogicBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                    Logger.getLogger(BusinessLogicBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(BusinessLogicBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return dto;
    }

}
