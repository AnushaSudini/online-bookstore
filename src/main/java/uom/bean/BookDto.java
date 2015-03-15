/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uom.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Anusha
 */
@SessionScoped
public class BookDto implements Serializable {

    private int bookID;
    private String bookName;
    private int authorID;
    private String authorName;
    private int quantity;
    private int publishserID;
    private BigDecimal price;
    private int categoryID;
    private int subcategoryID;
    private byte[] image;
    private int edition;
    private int ISBN;
    private String publisherName;
    private StreamedContent myImg;
    

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public int getPublishserID() {
        return publishserID;
    }

    public void setPublishserID(int publishserID) {
        this.publishserID = publishserID;
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

    public int getAuthorID() {
        return authorID;
    }

    public void setAuthorID(int authorID) {
        this.authorID = authorID;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public int getSubcategoryID() {
        return subcategoryID;
    }

    public void setSubcategoryID(int subcategoryID) {
        this.subcategoryID = subcategoryID;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
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

    public StreamedContent getMyImg() {
        System.out.println("Phase " + FacesContext.getCurrentInstance().getCurrentPhaseId());
        if(FacesContext.getCurrentInstance().getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            return new DefaultStreamedContent();
        } else {
        return myImg;
        }
    }

    public void setMyImg(StreamedContent myImg) {
        this.myImg = myImg;
    }

}
