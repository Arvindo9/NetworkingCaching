package com.example.networkingcaching.model.access;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

/**
 * Author : Arvindo Mondal
 * Created on 19-12-2019
 */
public class BookPremium {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("book_id")
    @Expose
    private Integer bookId = 0;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("date_of_purchase")
    @Expose
    private String dateOfPurchase;

    @SerializedName("is_download")
    @Expose
    private Integer isDownload;


    @SerializedName("file")
    @Expose
    private String file;

    @SerializedName("fileName")
    @Expose
    private String fileName;

    @SerializedName("product_id")
    @Expose
    private String productId;

    @SerializedName("ios_productId")
    @Expose
    private String iosProductId;

    @SerializedName("fileNameEncrypt")
    @Expose
    private String fileNameEncrypt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @NotNull
    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(@NotNull Integer bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDateOfPurchase() {
        return dateOfPurchase;
    }

    public void setDateOfPurchase(String dateOfPurchase) {
        this.dateOfPurchase = dateOfPurchase;
    }

    public Integer getIsDownload() {
        return isDownload;
    }

    public void setIsDownload(Integer isDownload) {
        this.isDownload = isDownload;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getIosProductId() {
        return iosProductId;
    }

    public void setIosProductId(String iosProductId) {
        this.iosProductId = iosProductId;
    }

    public String getFileNameEncrypt() {
        return fileNameEncrypt;
    }

    public void setFileNameEncrypt(String fileNameEncrypt) {
        this.fileNameEncrypt = fileNameEncrypt;
    }
}
