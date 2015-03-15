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
public class RegisterBean implements Serializable {

    private UserDto userDto;
    private String confirmPassword;

    public UserDto getUserDto() {
        if (userDto == null) {
            userDto = new UserDto();
        }
        return userDto;
    }

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String newUser() {
        if (!userDto.getPassword().equals(confirmPassword)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Password should be same as Confirm Password"));
            return "newUser.xhtml?faces-redirect=true";
        }
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DatabaseConnectionInfo.getInstance();
            ps = con.prepareStatement(SqlQuery.CREATE_USERID);

            rs = ps.executeQuery();

            if (rs.next()) {
                userDto.setUserID(rs.getInt(1) + 1);
            }
            ps = con.prepareStatement(SqlQuery.INSERT_USER);
            ps.setInt(1, userDto.getUserID());
            ps.setString(2, userDto.getUserName());
            ps.setString(3, userDto.getFirstName());
            ps.setString(4, userDto.getLastName());
            ps.setString(5, userDto.getGender());
            ps.setString(6, userDto.getAddress());
            ps.setString(7, userDto.getCity());
            ps.setString(8, userDto.getStateID());
            ps.setLong(9, userDto.getPhoneNumber());
            ps.setString(10, userDto.getEmailID());
            ps.executeUpdate();

            ps = con.prepareStatement(SqlQuery.INSERT_USERROLE);
            ps.setInt(1, userDto.getUserID());
            ps.setString(2, userDto.getUserName());
            ps.setString(3, userDto.getPassword());
            ps.setString(4, userDto.getRole());
            ps.executeUpdate();

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
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Registration completed successfully"));

        return "index.xhtml?faces-redirect=true";
    }
}
