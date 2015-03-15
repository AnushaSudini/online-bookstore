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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.TabChangeEvent;
import uom.model.DatabaseConnectionInfo;
import uom.model.SqlQuery;

/**
 *
 * @author Anusha
 */
@ManagedBean
@SessionScoped
public class AdminBean implements Serializable {

    private int userID;
    private String address;
    private String role;
    private String newUserName;
    private String password;
    private int bookstoreID;
    private int phoneNumber;
    private String emailID;
    private List<UserDto> userList;
    private List<StoreDto> storeList;
    private int assignUserId;
    private int assignBranchId;
    private TabView messagesTab = new TabView();

    public int getBookstoreID() {
        return bookstoreID;
    }

    public void setBookstoreID(int bookstoreID) {
        this.bookstoreID = bookstoreID;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAssignBranchId() {
        return assignBranchId;
    }

    public void setAssignBranchId(int assignBranchId) {
        this.assignBranchId = assignBranchId;
    }

    public int getAssignUserId() {
        return assignUserId;
    }

    public void setAssignUserId(int assignUserId) {
        this.assignUserId = assignUserId;
    }

    public String getNewUserName() {
        return newUserName;
    }

    public void setNewUserName(String newUserName) {
        this.newUserName = newUserName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public List<StoreDto> getStoreList() {
        return storeList;
    }

    public void setStoreList(List<StoreDto> storeList) {
        this.storeList = storeList;
    }

    public List<UserDto> getUserList() {
        return userList;
    }

    public void setUserList(List<UserDto> userList) {
        this.userList = userList;
    }

    public TabView getMessagesTab() {
        return messagesTab;
    }

    public void setMessagesTab(TabView messagesTab) {
        this.messagesTab = messagesTab;
    }

    public void doLookupStoreDealloc() {
        doLookupStore();
        this.messagesTab.setActiveIndex(1);
    }

    public void doLookupUserIDDealloc() {
        doLookupUserID();
        this.messagesTab.setActiveIndex(1);
    }

    public void doLookupStore() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        storeList = null;
        try {
            con = DatabaseConnectionInfo.getInstance();

            String sql = SqlQuery.LOOKUP_STORE;

            ps = con.prepareStatement(sql);
            ps.setString(1, "%" + getBookstoreID() + "%");
            rs = ps.executeQuery();
            storeList = new ArrayList<StoreDto>();
            while (rs.next()) {
                StoreDto dto = new StoreDto();
                dto.setUserID(rs.getInt("userID"));
                dto.setBookstoreID(rs.getInt("bookstoreID"));
                dto.setPhoneNumber(rs.getLong("phoneNumber"));
                dto.setEmailID(rs.getString("emailId"));
                dto.setLocationID(rs.getInt("locationID"));
               
                storeList.add(dto);
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
    }

    public void doLookupUserID() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        userList = null;
        try {
            con = DatabaseConnectionInfo.getInstance();

            String sql = SqlQuery.LOOKUP_USER;
            System.out.println(getUserID());
            ps = con.prepareStatement(sql);
            ps.setString(1, "%" + getUserID() + "%");
            System.out.println(sql);
            rs = ps.executeQuery();
            userList = new ArrayList<UserDto>();
            while (rs.next()) {
                UserDto dto = new UserDto();
                dto.setUserID(rs.getInt("userID"));
                dto.setUserName(rs.getString("username"));
                dto.setFirstName(rs.getString("firstname"));
                dto.setLastName(rs.getString("lastname"));
                dto.setGender(rs.getString("gender"));
                dto.setAddress(rs.getString("address"));
                dto.setCity(rs.getString("city"));
                dto.setStateName("statename");
                dto.setPhoneNumber(rs.getLong("phonenumber"));
                dto.setEmailID(rs.getString("emailID"));
                dto.setRole(rs.getString("role"));
                userList.add(dto);
                System.out.println(dto.getUserID() + dto.getUserName() + dto.getAddress() + dto.getRole());
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

    }

    public String doUserEdit(int uid) {

        assignUserId = uid;
        return "admin.xhtml?faces-redirect=true";
    }

    public String doBranchEdit(int branch) {

        assignBranchId = branch;
        return "admin.xhtml?faces-redirect=true";
    }

    public void assignMgr() {
        UserDto userDto = new UserDto();
        StoreDto storeDto = new StoreDto();
        Iterator<UserDto> iterator = userList.iterator();
        while (iterator.hasNext()) {
            UserDto dt = iterator.next();
            if (dt.getUserID() == assignUserId) {
                userDto = dt;
                break;
            }
        }
        Iterator<StoreDto> itr = storeList.iterator();
        while (itr.hasNext()) {
            StoreDto dt = itr.next();
            if (dt.getBookstoreID() == assignBranchId) {
                storeDto = dt;
                break;
            }
        }
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DatabaseConnectionInfo.getInstance();

            String sql = SqlQuery.UPDATE_USER;

            ps = con.prepareStatement(sql);
            ps.setString(1, "manager");
            ps.setInt(2, userDto.getUserID());
            ps.executeUpdate();
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,"Success","Assigned successfully"));
//            sql = SqlQuery.INSERT_BOOKSTORE_VALUES;
//
//            ps = con.prepareStatement(sql);
//            ps.setInt(1, storeDto.getBookstoreID());
//            ps.setInt(4, storeDto.getLocationID());
//            ps.setLong(3, storeDto.getPhoneNumber());
//            ps.setString(2, storeDto.getEmailID());
//            ps.setInt(5, userDto.getUserID());
//           
//            ps.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(BusinessLogicBean.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
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

    }

    public void deAssignMgr() {
        UserDto userDto;
        StoreDto storeDto;
        Iterator<UserDto> iterator = userList.iterator();
        while (iterator.hasNext()) {
            UserDto dt = iterator.next();
            if (dt.getUserID() == assignUserId) {
                userDto = dt;
                break;
            }
        }
        Iterator<StoreDto> itr = storeList.iterator();
        while (itr.hasNext()) {
            StoreDto dt = itr.next();
            if (dt.getBookstoreID() == assignBranchId) {
                storeDto = dt;
                break;
            }
        }
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DatabaseConnectionInfo.getInstance();

            String sql = SqlQuery.UPDATE_USER;

            ps = con.prepareStatement(sql);
            ps.setString(1, "customer");
            ps.setInt(2, getUserID());
            ps.executeUpdate();
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,"Success","Deassigned successfully"));
        } catch (SQLException ex) {
            Logger.getLogger(BusinessLogicBean.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
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

    }

    public void viewUser() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        userList = null;
        try {
            con = DatabaseConnectionInfo.getInstance();

            String sql = SqlQuery.ALL_USERS;

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            userList = new ArrayList<UserDto>();
            while (rs.next()) {
                UserDto dto = new UserDto();
                dto.setUserID(rs.getInt("userID"));
                dto.setUserName(rs.getString("username"));
//                dto.setAddress(rs.getString("address"));
                dto.setRole(rs.getString("role"));
                userList.add(dto);
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
    }

    public String addUser() {
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = DatabaseConnectionInfo.getInstance();

            String sql = SqlQuery.INSERT_USER;

            ps = con.prepareStatement(sql);
            ps.setInt(1, new Random().nextInt());
            ps.setString(2, getNewUserName());
            ps.setString(3, getPassword());
            ps.setString(4, getAddress());
            ps.setString(5, getRole());
            ps.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(BusinessLogicBean.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
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

        return "admin.xhtml?faces-redirect=true";
    }

    public void onTabChange(TabChangeEvent event) {

        if (event.getTab().getTitle().equals("View Users")) {
            viewUser();
        } else if (event.getTab().getTitle().equals("Allocate Manager")) {
            userID = 0;
            bookstoreID = 0;
        } else if (event.getTab().getTitle().equals("Deallocate Manager")) {
            userID = 0;
            bookstoreID = 0;
        }
    }
}
