/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uom.bean;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.sql.rowset.serial.SerialBlob;
import org.apache.commons.io.IOUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.TabChangeEvent;
import uom.model.DatabaseConnectionInfo;
import uom.model.SqlQuery;

/**
 *
 * @author Anusha
 */
@ManagedBean
@SessionScoped
public class ManagerBean implements Serializable {

    private int bookID;
    private String bookName;
    private String author;
    private double price;
    private int quantity;
    private String image;
    private String category;
    private String subcategory;
    private List<UserDto> managerDtoList;
    private List<UserDto> customerDtoList;
    private BookDto bookDto;
    private Map<String, Integer> authorMap;
    private Map<String, Integer> publisherMap;
    private Map<String, Integer> categoryMap;
    private Map<String, Integer> subcategoryMap;
    private int authorID = 0;
    private int publisherID = 0;
    private String publisher;
    private int edition;
    private int ISBN;
    private int bookStoreID;

    public int getBookStoreID() {
        return bookStoreID;
    }

    public void setBookStoreID(int bookStoreID) {
        this.bookStoreID = bookStoreID;
    }

    public int getEdition() {
        return edition;
    }

    public void setEdition(int edition) {
        this.edition = edition;
    }

    public int getISBN() {
        return ISBN;
    }

    public void setISBN(int ISBN) {
        this.ISBN = ISBN;
    }

    public int getPublisherID() {
        return publisherID;
    }

    public void setPublisherID(int publisherID) {
        this.publisherID = publisherID;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getAuthorID() {
        return authorID;
    }

    public void setAuthorID(int authorID) {
        this.authorID = authorID;
    }

    public Map<String, Integer> getAuthorMap() {
        if (authorMap == null) {
            authorMap = new LinkedHashMap<String, Integer>();
            int i = 0;
            authorMap.put("Not Selected", i);
            Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
                con = DatabaseConnectionInfo.getInstance();

                String sql = SqlQuery.GET_AUTHORS;

                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
                while (rs.next()) {
                    i++;
                    authorMap.put(rs.getString("authorname"), i);
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
        return authorMap;
    }

    public void setAuthorMap(Map<String, Integer> authorMap) {

        this.authorMap = authorMap;
    }

    public Map<String, Integer> getPublisherMap() {
        if (publisherMap == null) {
            publisherMap = new LinkedHashMap<String, Integer>();
            publisherMap.put("Not Selected", 0);
        }
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DatabaseConnectionInfo.getInstance();

            String sql = SqlQuery.GET_PUBLISHERS;

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            int i = 1;
            while (rs.next()) {
                i++;
                publisherMap.put(rs.getString("publishername"), i);
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
        return publisherMap;
    }

    public void setPublisherMap(Map<String, Integer> publisherMap) {
        this.publisherMap = publisherMap;
    }

    public Map<String, Integer> getCategoryMap() {
        if (categoryMap == null) {
            categoryMap = new LinkedHashMap<String, Integer>();            
        }
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DatabaseConnectionInfo.getInstance();

            String sql = SqlQuery.GET_CATEGORYNAMES;

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            int i = 1;
            while (rs.next()) {                
                categoryMap.put(rs.getString("categoryName"), i);
                i++;
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

        return categoryMap;
    }

    public void setCategoryMap(Map<String, Integer> categoryMap) {
        this.categoryMap = categoryMap;
    }

    public Map<String, Integer> getSubcategoryMap() {
        if (subcategoryMap == null) {
            subcategoryMap = new LinkedHashMap<String, Integer>();
            
        }
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DatabaseConnectionInfo.getInstance();

            String sql = SqlQuery.GET_SUBCATEGORYNAMES;

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            int i = 1;
            while (rs.next()) {
                subcategoryMap.put(rs.getString("subcategoryName"), i);
                i++;
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
        
        return subcategoryMap;
    }

    public void setSubcategoryMap(Map<String, Integer> subcategoryMap) {
        
        this.subcategoryMap = subcategoryMap;
    }

    public BookDto getBookDto() {
        if (bookDto == null) {
            bookDto = new BookDto();
        }
        return bookDto;
    }

    public void setBookDto(BookDto bookDto) {
        this.bookDto = bookDto;
    }

    public List<UserDto> getCustomerDtoList() {
        return customerDtoList;
    }

    public void setCustomerDtoList(List<UserDto> customerDtoList) {
        this.customerDtoList = customerDtoList;
    }

    public List<UserDto> getManagerDtoList() {
        return managerDtoList;
    }

    public void setManagerDtoList(List<UserDto> managerDtoList) {
        this.managerDtoList = managerDtoList;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public String addBook() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {

            con = DatabaseConnectionInfo.getInstance();

            String sql = SqlQuery.BOOK_COUNT;

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            int bookIDNum = 0;
            while (rs.next()) {
                bookIDNum = rs.getInt("a");
            }

            ps = con.prepareStatement(SqlQuery.GET_BOOKSTOREINFO);
            rs = ps.executeQuery();
            boolean b = false;
            while (rs.next()) {
                if (getBookStoreID() == rs.getInt("bookstoreID")) {

                    b = true;
                    break;
                }
            }
            if (!b) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Invalid BookStore ID"));
                return "manager.xhtml?faces-redirect=true";
            }

            if (getAuthorID() == 0) {
                ps = con.prepareStatement(SqlQuery.AUTHOR_COUNT);
                rs = ps.executeQuery();
                while (rs.next()) {
                    setAuthorID(rs.getInt("a") + 1);
                }

                ps = con.prepareStatement(SqlQuery.INSERT_AUTHOR);
                ps.setInt(1, getAuthorID());
                ps.setString(2, getAuthor());
                ps.executeUpdate();
            }

            if (getPublisherID() == 0) {
                ps = con.prepareStatement(SqlQuery.PUBLISHER_COUNT);
                rs = ps.executeQuery();
                while (rs.next()) {
                    setPublisherID(rs.getInt("a") + 1);
                }

                ps = con.prepareStatement(SqlQuery.INSERT_PUBLISHER);
                ps.setInt(1, getPublisherID());
                ps.setString(2, getPublisher());
                ps.executeUpdate();
            }

            sql = SqlQuery.INSERT_BOOK;

            ps = con.prepareStatement(sql);
            ps.setInt(1, bookIDNum + 1);
            ps.setString(2, getBookName());
            ps.setInt(3, getAuthorID());
            ps.setInt(4, getPublisherID());
            ps.setDouble(5, getPrice());
            ps.setInt(6, Integer.parseInt(getCategory()));
            ps.setInt(7, Integer.parseInt(getSubcategory()));
            Blob blob = new SerialBlob(getBookDto().getImage());
            ps.setBlob(8, blob);
            ps.setInt(9, getEdition());
            ps.setInt(10, getISBN());
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

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Book Added Successfully"));

        return "manager.xhtml?faces-redirect=true";
    }

    public String deleteBook() {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DatabaseConnectionInfo.getInstance();
            String sql = SqlQuery.DELETE_BOOK;
            ps = con.prepareStatement(sql);
            ps.setInt(1, getBookID());
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
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Book Deleted Successfully"));
        return "manager.xhtml?faces-redirect=true";
    }

    public void viewManagers() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DatabaseConnectionInfo.getInstance();
            String sql = SqlQuery.GET_MGRS;
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            managerDtoList = new ArrayList<UserDto>();
            while (rs.next()) {
                UserDto userDto = new UserDto();
                userDto.setUserID(rs.getInt("userID"));
//                userDto.setAddress(rs.getString("address"));
                userDto.setUserName(rs.getString("username"));
                userDto.setPassword(rs.getString("password"));
                userDto.setRole(rs.getString("role"));
                managerDtoList.add(userDto);
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

    public void viewCustomers() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DatabaseConnectionInfo.getInstance();
            String sql = SqlQuery.GET_CUSTOMERS;
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            customerDtoList = new ArrayList<UserDto>();
            while (rs.next()) {
                UserDto userDto = new UserDto();
                userDto.setUserID(rs.getInt("userID"));
//                userDto.setAddress(rs.getString("address"));
                userDto.setUserName(rs.getString("username"));
                userDto.setPassword(rs.getString("password"));
                userDto.setRole(rs.getString("role"));
                customerDtoList.add(userDto);
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

    public void allInfo() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DatabaseConnectionInfo.getInstance();
            ps = con.prepareStatement(SqlQuery.GET_AUTHORINFO);
            rs = ps.executeQuery();
            authorMap = new LinkedHashMap<String, Integer>();
            authorMap.put("Not Selected", 0);
            while (rs.next()) {
                authorMap.put(rs.getString("authorName"), rs.getInt("authorID"));
            }

            ps = con.prepareStatement(SqlQuery.GET_PUBLISHERINFO);
            rs = ps.executeQuery();
            publisherMap = new LinkedHashMap<String, Integer>();
            publisherMap.put("Not Selected", 0);
            while (rs.next()) {
                publisherMap.put(rs.getString("publishername"), rs.getInt("publisherID"));
            }

            ps = con.prepareStatement(SqlQuery.GET_CATEGORYINFO);
            rs = ps.executeQuery();
            categoryMap = new LinkedHashMap<String, Integer>();
            while (rs.next()) {
                categoryMap.put(rs.getString("categoryName"), rs.getInt("categoryID"));
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

    public void onTabChange(TabChangeEvent event) {

        if (event.getTab().getTitle().equals("View Customers")) {
            viewCustomers();
        } else if (event.getTab().getTitle().equals("View Managers")) {
            viewManagers();
        } else if (event.getTab().getTitle().equals("Add Book")) {
            allInfo();
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", event.getFile().getFileName() + " is uploaded."));

            getBookDto().setImage(IOUtils.toByteArray(event.getFile().getInputstream()));
        } catch (IOException ex) {
            Logger.getLogger(ManagerBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Entry<T, E> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public void valueChange(ValueChangeEvent event) {

//        String tmp = getKeyByValue(categoryMap, Integer.parseInt(event.getNewValue().toString()));
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DatabaseConnectionInfo.getInstance();

            ps = con.prepareStatement(SqlQuery.GET_SUBCATEGORYINFO);
            ps.setInt(1, Integer.parseInt(event.getNewValue().toString()));
            rs = ps.executeQuery();
            subcategoryMap = new LinkedHashMap<String, Integer>();
            while (rs.next()) {
                subcategoryMap.put(rs.getString("subcategoryname"), rs.getInt("subcategoryID"));
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
}
