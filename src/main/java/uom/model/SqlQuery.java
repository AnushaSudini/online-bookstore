/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uom.model;

/**
 *
 * @author prankris
 */
public final class SqlQuery {

    public static final String LOOKUP_STORE = "select * from bookstore where bookstoreID like ? group by bookstoreID;";
    public static final String LOOKUP_CUSTOMER = "SELECT * FROM user_details where userID like ? ; ";
    public static final String UPDATE_USER = "update user_details set role = ? where userID = ?  ";
    public static final String INSERT_BOOKSTORE_VALUES = "insert into bookstore values (?,?,?,?,?)  ";
    public static final String ALL_USERS = "SELECT * FROM user_details";
    public static final String TOTAL_ORDERS = "SELECT * FROM cart c inner join bookdetails bd on c.bookID=bd.bookID inner join users u on c.userID=u.userID where c.userID=?";
    public static final String INSERT_USER = "insert into users values (?,?,?,?,?,?,?,?,?,?)  ";
    public static final String CREATE_USERID = "SELECT max(userID) from users";
    public static final String INSERT_USERROLE = "insert into user_details values (?,?,?,?)";
    public static final String GET_BOOKS = "SELECT * FROM bookdetails a inner join author b on a.authorID = b.authorID inner join publisher c on a.publisherID = c.publisherID where categoryID=? and subcategoryID=?";
    public static final String RETRIEVE_BOOKS = "SELECT * FROM bookdetails where bookname=?";
    public static final String LOOKUP_BRANCH = "SELECT * FROM bookstore where bookstoreID like ?";
    public static final String LOOKUP_USER = "SELECT users.userID,users.username,users.firstname,users.lastname,users.gender,users.address,users.city,users.phonenumber,users.emailID,state.statename,user_details.role from (users inner join state on users.stateID=state.stateID) inner join user_details on users.userID=user_details.userID where users.userID like ?";
    public static final String LOOKUP_BOOK = "select * from bookdetails a inner join author b on a.authorID = b.authorID inner join publisher c on a.publisherID = c.publisherID where bookname like ?";
    public static final String INSERT_CART = "insert into cart values (?,?,?,?)";
    public static final String USER_CARD = "insert into usercard values (?,?)";   
    public static final String CARD_DETAILS = "insert into carddetails values (?,?,?,?,?)";
    public static final String CALCULATE_TOTALAMOUNT="SELECT sum(price) s FROM dbproj.cart c inner join users u on c.userID=u.userID inner join bookdetails d on c.bookID=d.bookID where userID=? and cartID=? ";
    public static final String PAYMENT = "insert into payment values (?,?,?,?)";
    public static final String GET_CARTIDS = "SELECT cartID FROM dbproj.cart  where userID =?";
    public static final String DELETE_CART = "delete from cart where userID=?";    
    public static final String CART_COUNT = "select max(cartID) a from cart";
    public static final String AUTHENTICATE = "SELECT * FROM user_details ud inner join users u on ud.userID=u.userID where ud.username = ? and ud.password = ?";
    public static final String BOOK_COUNT = "select max(bookID) a from bookdetails";
    public static final String INSERT_BOOK = "insert into bookdetails values (?,?,?,?,?,?,?,?,?,?)  ";
    public static final String DELETE_BOOK = "delete from bookdetails where bookID = ?  ";
    public static final String GET_MGRS = "select * from user_details where role = 'manager'  ";
    public static final String GET_CUSTOMERS = "select * from user_details where role = 'customer'  ";
    public static final String GET_AUTHORINFO = "select * from author";
    public static final String GET_PUBLISHERINFO = "select * from publisher";
    public static final String GET_CATEGORYINFO = "select * from category";
    public static final String GET_SUBCATEGORYINFO = "select * from subcategory where categoryID = ?";
    public static final String INSERT_AUTHOR = "insert into author values (?,?)";
    public static final String AUTHOR_COUNT = "select max(authorID) a from author";
    public static final String INSERT_PUBLISHER = "insert into publisher values (?,?)";
    public static final String PUBLISHER_COUNT = "select max(publisherID) a from publisher";
    public static final String GET_BOOKSTOREINFO = "select * from bookstore";
    public static final String GET_AUTHORS="SELECT authorname FROM dbproj.author";
    public static final String GET_PUBLISHERS="SELECT publishername FROM dbproj.publisher";
    public static final String GET_CATEGORYNAMES="SELECT categoryName FROM dbproj.category";
    public static final String GET_SUBCATEGORYNAMES="SELECT subcategoryName FROM dbproj.subcategory";
}
