/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uom.bean;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
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
import javax.imageio.ImageIO;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import uom.model.DatabaseConnectionInfo;
import uom.model.SqlQuery;

/**
 *
 * @author Anusha
 */
@ManagedBean
@SessionScoped
public class BusinessLogicBean implements Serializable {

    private List<BookDto> ls;
    private StoreDto storeDto;
    private UserDto userDto;
    private CartDto cartDto;
    private List<CartDto> cartList;
    private String bookName;
    private List<StoreDto> storeList;
    private List<UserDto> userList;
    private List<BookDto> bookDtoList;
    private StreamedContent myImg;
    private List<StreamedContent> myImgList = new ArrayList<StreamedContent>();
    private double totalAmount;

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
    

    public CartDto getCartDto() {
        if (cartDto == null) {
            cartDto = new CartDto();
        }
        return cartDto;
    }

    public void setCartDto(CartDto cartDto) {
        this.cartDto = cartDto;
    }

    public StreamedContent getMyImg() {
        return myImg;
    }

    public void setMyImg(StreamedContent myImg) {
        this.myImg = myImg;
    }

    public List<BookDto> getBookDtoList() {
        return bookDtoList;
    }

    public void setBookDtoList(List<BookDto> bookDtoList) {
        this.bookDtoList = bookDtoList;
    }

    public List<CartDto> getCartDtoList() {
        return cartList;
    }

    public void setCartDtoList(List<CartDto> cartDtoList) {
        this.cartList = cartDtoList;
    }

    public UserDto getUserDto() {
        if (userDto == null) {
            userDto = new UserDto();
        }

        return userDto;
    }

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }

    public List<UserDto> getUserList() {
        return userList;
    }

    public void setUserList(List<UserDto> userList) {
        this.userList = userList;
    }

    public List<BookDto> getLs() {
        return ls;
    }

    public void setLs(List<BookDto> ls) {
        this.ls = ls;
    }

    public StoreDto getStoreDto() {
        if (storeDto == null) {
            storeDto = new StoreDto();
        }
        return storeDto;
    }

    public void setStoreDto(StoreDto storeDto) {
        this.storeDto = storeDto;
    }

    public List<StoreDto> getStoreList() {
        return storeList;
    }

    public void setStoreList(List<StoreDto> storeList) {
        this.storeList = storeList;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    private List<BookDto> runQuery(int categoryID, int subCategoryID) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<BookDto> lt = null;
        try {
            con = DatabaseConnectionInfo.getInstance();

            String sql = SqlQuery.GET_BOOKS;

            ps = con.prepareStatement(sql);
            ps.setInt(1, categoryID);
            ps.setInt(2, subCategoryID);
            rs = ps.executeQuery();
            lt = new ArrayList<BookDto>();
            int i = 0;
            while (rs.next()) {
                BookDto dto = new BookDto();
                dto.setBookID(rs.getInt("bookID"));
                dto.setAuthorID(rs.getInt("authorID"));
                dto.setAuthorName(rs.getString("authorname"));
                dto.setBookName(rs.getString("bookname"));
                dto.setPublishserID(rs.getInt("publisherID"));
                dto.setPublisherName(rs.getString("publishername"));
                String tmp = rs.getString("price");
                if (tmp != null) {
                    dto.setPrice(new BigDecimal(tmp));
                } else {
                    dto.setPrice(BigDecimal.ZERO);
                }

                java.sql.Blob img = rs.getBlob("image");
                dto.setImage(img.getBytes(1, (int) img.length()));
                InputStream in = new ByteArrayInputStream(dto.getImage());
                BufferedImage originalImage = ImageIO.read(in);
                int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
                BufferedImage resizedImage = new BufferedImage(100, 100, type);
                Graphics2D g = resizedImage.createGraphics();
                g.drawImage(originalImage, 0, 0, 100, 100, null);
                g.dispose();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(resizedImage, "jpg", baos);
                baos.flush();
                byte[] imageInByte = baos.toByteArray();
                baos.close();
                myImg = new DefaultStreamedContent(new ByteArrayInputStream(imageInByte), "image/jpg");
                myImgList.add(i++, myImg);
                dto.setMyImg(new DefaultStreamedContent(new ByteArrayInputStream(imageInByte), "image/jpg"));
                
                dto.setEdition(rs.getInt("edition"));
                dto.setISBN(rs.getInt("ISBN"));
                lt.add(dto);
            }

        } catch (SQLException ex) {
            Logger.getLogger(BusinessLogicBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
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

        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("img", myImgList);
        return lt;
    }

    public String doBooksBiography() {
        ls = runQuery(2, 1);

        if (ls == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("No Biography Books are Available"));
        }
        return "biography.xhtml?faces-redirect=true";
    }
public String doBooksGraphicBooks() {
        ls = runQuery(2, 3);

        if (ls == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("No Graphic Books are Available"));
        }
        return "biography.xhtml?faces-redirect=true";
    }
    public String doBooksCookBooks() {
        ls = runQuery(2, 2);

        if (ls == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("No Cook Books are Available"));
        }
        return "biography.xhtml?faces-redirect=true";
    }

    public String doBooksHistory() {
        ls = runQuery(2, 4);

        if (ls == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("No History Books are Available"));
        }
        return "biography.xhtml?faces-redirect=true";
    }

    public String doBooksMystery() {
        ls = runQuery(2, 5);

        if (ls == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("No Mystery Books are Available"));
        }
        return "biography.xhtml?faces-redirect=true";
    }

    
    public String doBooksRomance() {
        ls = runQuery(2, 7);

        if (ls == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("No Romance Books are Available"));
        }
        return "biography.xhtml?faces-redirect=true";
    }

    public String doBooksScience() {
        ls = runQuery(2, 8);

        if (ls == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("No Science Books are Available"));
        }
        return "biography.xhtml?faces-redirect=true";
    }

    public String doUsedBooksBiography() {
        ls = runQuery(5, 1);

        if (ls == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("No used biography Books are Available"));
        }
        return "biography.xhtml?faces-redirect=true";
    }

    public String doUsedBooksChildren() {
        ls = runQuery(5, 2);

        if (ls == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("No used children Books are Available"));
        }
        return "biography.xhtml?faces-redirect=true";
    }

    public String doUsedBooksCooking() {
        ls = runQuery(5, 3);

        if (ls == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("No used cooking Books Available"));
        }
        return "biography.xhtml?faces-redirect=true";
    }

    public String doUsedBooksEducation() {
        ls = runQuery(5, 4);

        if (ls == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("No used education Books Available"));
        }
        return "biography.xhtml?faces-redirect=true";
    }

    public String doUsedBooksFiction() {
        ls = runQuery(5, 5);

        if (ls == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("No used fiction Books Available"));
        }
        return "biography.xhtml?faces-redirect=true";
    }

    public String doUsedBooksLiterature() {
        ls = runQuery(5, 6);

        if (ls == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("No used literature Books Available"));
        }
        return "biography.xhtml?faces-redirect=true";
    }

    public String doUsedBooksMystery() {
        ls = runQuery(5, 7);

        if (ls == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("No used mystery Books Available"));
        }
        return "biography.xhtml?faces-redirect=true";
    }

    public String doUsedBooksPoetry() {
        ls = runQuery(5, 8);

        if (ls == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("No used poetry Books Available"));
        }
        return "biography.xhtml?faces-redirect=true";
    }

    public String doNewstandBusiness() {
        ls = runQuery(4, 1);

        if (ls == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("No business magazines are Available"));
        }
        return "biography.xhtml?faces-redirect=true";
    }

    public String doNewstandFashion() {
        ls = runQuery(4, 2);

        if (ls == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("No fashion magazines are Available"));
        }
        return "biography.xhtml?faces-redirect=true";
    }

    public String doNewstandFitness() {
        ls = runQuery(4, 3);

        if (ls == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("No fitness magazines are Available"));
        }
        return "biography.xhtml?faces-redirect=true";
    }

    public String doNewstandInternationalNp() {
        ls = runQuery(4, 4);

        if (ls == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("No international news papers are Available"));
        }
        return "biography.xhtml?faces-redirect=true";
    }

    public String doNewstandNationalNp() {
        ls = runQuery(4, 5);

        if (ls == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("No national news papers are Available"));
        }
        return "biography.xhtml?faces-redirect=true";
    }

    public String doNewstandRegionalNp() {
        ls = runQuery(4, 6);

        if (ls == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("No regional news papers are Available"));
        }
        return "biography.xhtml?faces-redirect=true";
    }

    public String doNewstandSports() {
        ls = runQuery(4, 7);

        if (ls == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("No sports magazines are Available"));
        }
        return "biography.xhtml?faces-redirect=true";
    }

    public String doNewstandTravel() {
        ls = runQuery(4, 8);

        if (ls == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("No travel magazines are Available"));
        }
        return "biography.xhtml?faces-redirect=true";
    }

    public String doBestSellerChildren() {
        ls = runQuery(1, 1);

        if (ls == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("No children magazines are Available"));
        }
        return "biography.xhtml?faces-redirect=true";
    }

    public String doBestSellerCookBook() {
        ls = runQuery(1, 2);

        if (ls == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("No cook books are Available"));
        }
        return "biography.xhtml?faces-redirect=true";
    }

    public String doBestSellerFiction() {
        ls = runQuery(1, 3);

        if (ls == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("No fiction magazines are Available"));
        }
        return "biography.xhtml?faces-redirect=true";
    }

    public String doBestSellerHistory() {
        ls = runQuery(1, 4);

        if (ls == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("No history magazines are Available"));
        }
        return "biography.xhtml?faces-redirect=true";
    }

    public String doBestSellerPsychology() {
        ls = runQuery(1, 5);

        if (ls == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("No psychology magazines are Available"));
        }
        return "biography.xhtml?faces-redirect=true";
    }

    public String doBestSellerScience() {
        ls = runQuery(1, 6);

        if (ls == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("No science magazines are Available"));
        }
        return "biography.xhtml?faces-redirect=true";
    }

    public String doBestSellerSuspense() {
        ls = runQuery(1, 7);

        if (ls == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("No suspense magazines are Available"));
        }
        return "biography.xhtml?faces-redirect=true";
    }

    public String doEntertainmentAction() {
        ls = runQuery(3, 1);

        if (ls == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("No action magazines are Available"));
        }
        return "biography.xhtml?faces-redirect=true";
    }

    public String doEntertainmentAdventure() {
        ls = runQuery(3, 2);

        if (ls == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("No adventure magazines are Available"));
        }
        return "biography.xhtml?faces-redirect=true";
    }

    public String doEntertainmentAnimals() {
        ls = runQuery(3, 3);

        if (ls == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("No animal magazines are Available"));
        }
        return "biography.xhtml?faces-redirect=true";
    }

    public String doEntertainmentBiography() {
        ls = runQuery(3, 4);

        if (ls == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("No biography magazines are Available"));
        }
        return "biography.xhtml?faces-redirect=true";
    }

    public String doEntertainmentDrama() {
        ls = runQuery(3, 5);

        if (ls == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("No drama magazines are Available"));
        }
        return "biography.xhtml?faces-redirect=true";
    }

    public String doEntertainmentFantasy() {
        ls = runQuery(3, 6);

        if (ls == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("No fantasy magazines are Available"));
        }
        return "biography.xhtml?faces-redirect=true";
    }

    public String doEntertainmentSports() {
        ls = runQuery(3, 7);

        if (ls == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("No sport magazines are Available"));
        }
        return "biography.xhtml?faces-redirect=true";
    }

    public String doSearchStore() {

        //FacesContext.getCurrentInstance().getExternalContext.getSessionMap().put("user", username);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        storeList = null;
        try {
            con = DatabaseConnectionInfo.getInstance();

            String sql = SqlQuery.LOOKUP_BRANCH;

            ps = con.prepareStatement(sql);
            ps.setString(1, "%" + storeDto.getBookstoreID()+ "%");
            //ps.setString(2, subCategory);
            rs = ps.executeQuery();
            storeList = new ArrayList<StoreDto>();
            while (rs.next()) {
                StoreDto dto = new StoreDto();
                dto.setBookstoreID(rs.getInt("bookstoreID"));
                dto.setLocationID(rs.getInt("locationID"));
                dto.setEmailID(rs.getString("emailId"));
                dto.setPhoneNumber(rs.getInt("phNumber"));
                dto.setUserID(rs.getInt("userID"));

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

        return "searchStore.xhtml?faces-redirect=true";
    }

    public String doSearchUser() {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        userList = null;
        try {
            con = DatabaseConnectionInfo.getInstance();

            String sql = SqlQuery.LOOKUP_USER;

            ps = con.prepareStatement(sql);
            ps.setString(1, "%" + userDto.getUserID() + "%");
            rs = ps.executeQuery();
            userList = new ArrayList<UserDto>();
            while (rs.next()) {
                UserDto dto = new UserDto();
                dto.setUserID(rs.getInt("userID"));
                dto.setUserName(rs.getString("username"));
                dto.setAddress(rs.getString("address"));
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

        return "searchUser.xhtml?faces-redirect=true";
    }

    public void addToCart(BookDto dto) {

        UserDto dt = (UserDto) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
        if (dt == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "User Not Logged In.  You need to log in to add to cart."));
            return;
        }
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        userList = null;
        try {
            con = DatabaseConnectionInfo.getInstance();

            ps = con.prepareStatement("select max(cartID) a from cart");
            rs = ps.executeQuery();
            int cartID = 0;
            while (rs.next()) {
                cartID = rs.getInt("a");
            }
            //cartList = new ArrayList<CartDto>();
            cartDto = new CartDto();
            cartDto.setCartID(cartID + 1);
            cartDto.setBookID(dto.getBookID());
            cartDto.setUserID(dt.getUserID());
            cartDto.setQuantity(1);
            String sql = SqlQuery.INSERT_CART;

            ps = con.prepareStatement(sql);
            ps.setInt(1, cartID + 1);

            ps.setInt(2, dto.getBookID());
            ps.setInt(3, dt.getUserID());
            ps.setInt(4, 1);

            ps.executeUpdate();
            totalAmount();

           
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Added item successfully.", "Added item successfully."));

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
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Book added to cart"));

    }
    public void totalAmount(){
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        double tempamt = 0;
        UserDto dt = (UserDto) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
        if (dt == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "User Not Logged In.  You need to log in to add to cart."));
            return;
        }
        try{
        con = DatabaseConnectionInfo.getInstance();

            String sql = SqlQuery.TOTAL_ORDERS;
            ps = con.prepareStatement(sql);
            ps.setInt(1, dt.getUserID());
            rs = ps.executeQuery();
            while (rs.next()) {
                tempamt = tempamt + rs.getDouble("price");
            }
            setTotalAmount(tempamt);
        }catch (SQLException ex) {
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

//    public String checkout() {
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
//                ps.setInt(2, dto.getUID());
//                ps.setString(3, dto.getBookID());
//                ps.setString(4, Integer.toString(orderId));
//                ps.setInt(5, dto.getCost());
//                ps.setDate(6, new Date((new java.util.Date()).getTime()));
//                ps.setInt(7, dto.getQuantity());
//
//                ps.executeUpdate();
//
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
//        return "success.xhtml?faces-redirect=true";
//    }

    public String logout() {

        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "index.xhtml?faces-redirect=true";
    }

    public String doSearchBookName() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        bookDtoList = null;
        try {
            con = DatabaseConnectionInfo.getInstance();

            String sql = SqlQuery.LOOKUP_BOOK;

            ps = con.prepareStatement(sql);
            ps.setString(1, "%" + getBookName() + "%");
            rs = ps.executeQuery();
            bookDtoList = new ArrayList<BookDto>();
            while (rs.next()) {
                BookDto dto = new BookDto();
                dto.setBookID(rs.getInt("bookID"));
                dto.setBookName(rs.getString("bookname"));
                dto.setAuthorID(rs.getInt("authorID"));
                dto.setAuthorName(rs.getString("authorname"));
                dto.setPublishserID(rs.getInt("publisherID"));
                dto.setPublisherName(rs.getString("publishername"));
                String tmp = rs.getString("price");
                if (tmp != null) {
                    dto.setPrice(new BigDecimal(tmp));
                } else {
                    dto.setPrice(BigDecimal.ZERO);
                }
                dto.setCategoryID(rs.getInt("categoryID"));
                dto.setSubcategoryID(rs.getInt("subcategoryID"));
                java.sql.Blob b = rs.getBlob("image");
                dto.setImage(b.getBytes(1, (int) b.length()));
                dto.setEdition(rs.getInt("edition"));
                dto.setISBN(rs.getInt("ISBN"));
                bookDtoList.add(dto);
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
        if (bookDtoList == null || bookDtoList.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "WARNING", "No records found"));
        }
        return "searchBookName.xhtml?faces-redirect=true";
    }

    public List<StreamedContent> getMyImgList() {
        return myImgList;
    }

    public void setMyImgList(List<StreamedContent> myImgList) {
        this.myImgList = myImgList;
    }

}
