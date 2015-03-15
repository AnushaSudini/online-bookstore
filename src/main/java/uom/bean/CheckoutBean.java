/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uom.bean;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
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
public class CheckoutBean {

    private String ccType;
    private long ccNum;
    private String ccName;
    private Date expDate;
    private int cvv;
    private String month;
    private String year;
    private double amount;
    private List<Integer> cartIDs;

    public double getAmount() {
        return amount;
    }

    public List<Integer> getCartIDs() {
        return cartIDs;
    }

    public void setCartIDs(List<Integer> cartIDs) {
        this.cartIDs = cartIDs;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getCcType() {
        return ccType;
    }

    public void setCcType(String ccType) {
        this.ccType = ccType;
    }

    public long getCcNum() {
        return ccNum;
    }

    public void setCcNum(long ccNum) {
        this.ccNum = ccNum;
    }

    public String getCcName() {
        return ccName;
    }

    public void setCcName(String ccName) {
        this.ccName = ccName;
    }

    public Date getExpDate() {
        return expDate;
    }

    public void setExpDate(Date expDate) {
        this.expDate = expDate;
    }

    public int getCvv() {
        return cvv;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }

    public String checkout() {
        UserDto dt = (UserDto) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
        if (dt == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "User Not Logged In.  You need to log in to add to cart."));
            return "";
        }
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        double tempamt = 0;
        cartIDs = new ArrayList<Integer>();
        String sql = null;
        try {
            con = DatabaseConnectionInfo.getInstance();

            sql = SqlQuery.TOTAL_ORDERS;

            ps = con.prepareStatement(sql);
            ps.setInt(1, dt.getUserID());
            rs = ps.executeQuery();
            while (rs.next()) {
                tempamt = tempamt + rs.getDouble("price");
            }
            setAmount(tempamt);

            sql = SqlQuery.CARD_DETAILS;
            ps = con.prepareStatement(sql);
            //Calendar c = Calendar.getInstance();
            //c.set(Integer.parseInt(getYear()), Integer.parseInt(getMonth()), 1);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            try {
                java.util.Date tdate = sdf.parse(year + "-" + month + "-" + "1");
                Date tempd = new Date(tdate.getTime());
                setExpDate(tempd);
            } catch (ParseException ex) {
                Logger.getLogger(CheckoutBean.class.getName()).log(Level.SEVERE, null, ex);
            }
            //java.util.Date tdate = new java.util.Date();
            //Date tempd = new Date(tdate.getTime());

            //setExpDate(Date.valueOf(year +"-"+ month +"-"+ "1"));
            //date1.setDate(c.getTime());
            //setExpDate(c.getTime().getTime());
            ps.setLong(1, getCcNum());
            ps.setString(2, getCcType());
            ps.setString(3, getCcName());
            ps.setDate(4, getExpDate());
            ps.setInt(5, getCvv());
            ps.executeUpdate();

            sql = SqlQuery.USER_CARD;
            ps = con.prepareStatement(sql);
            ps.setInt(1, dt.getUserID());
            ps.setLong(2, getCcNum());
            ps.executeUpdate();

            sql = SqlQuery.DELETE_CART;
            ps = con.prepareStatement(sql);
            ps.setInt(1, dt.getUserID());
            ps.executeUpdate();
            sql = SqlQuery.GET_CARTIDS;
            ps = con.prepareStatement(sql);
            ps.setInt(1, dt.getUserID());
            rs = ps.executeQuery();
            while (rs.next()) {
                cartIDs.add(rs.getInt("cartID"));

            }
            sql = SqlQuery.PAYMENT;
            java.util.Date date1 = new java.util.Date();
            ps = con.prepareStatement(sql);
            ps.setInt(1, (int) date1.getTime());
            ps.setInt(2, dt.getUserID());
            ps.setDate(3, new Date(date1.getTime()));
            ps.setDouble(4, getAmount());
            ps.executeUpdate();
            //int c=1;
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

//        int orderId = (new Random()).nextInt();
//        for (CartDto dto : cartDtoList) {
//            Connection con = null;
//            PreparedStatement ps = null;
//            try {
//                con = DatabaseConnectionInfo.getInstance();
//
//                String sql = SqlQuery.INSERT_CART;
//
//                ps = con.prepareStatement(sql);
//                ps.setInt(1, (new Random()).nextInt());
//                ps.setInt(2, dto.getUserID());
//                ps.setInt(3, dto.getBookID());
//                ps.setString(4, Integer.toString(orderId));
////                ps.setInt(5, dto.getCost());
//                ps.setDate(6, new java.sql.Date((new java.util.Date()).getTime()));
//                ps.setInt(7, dto.getQuantity());
//
//                ps.executeUpdate();
//            } catch (SQLException ex) {
//                Logger.getLogger(BusinessLogicBean.class.getName()).log(Level.SEVERE, null, ex);
//            } finally {
//                if (ps != null) {
//                    try {
//                        ps.close();
//                    } catch (SQLException ex) {
//                        Logger.getLogger(BusinessLogicBean.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                }
//                if (con != null) {
//                    try {
//                        con.close();
//                    } catch (SQLException ex) {
//                        Logger.getLogger(BusinessLogicBean.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                }
//            }
//        }
        return "success.xhtml?faces-redirect=true";
    }

}
