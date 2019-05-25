package com.appincubator.digitaldarji.Model;

public class Carts {

    int _id;
    int id;
    String type;
    String email;
    String selleremail;
    String status;
    int count;
    int price;
    String name;
    String img;
    String size;
    String color;
    String neckAround;
    String shoulderWidth;
    String waistAround;
    String bicepAround;
    String sleeveLength;
    String chestAround;
    String shirtLength;
    String wristAround;

    public Carts(int _id, int id, String type, String email, String selleremail, String status, int count, int price, String name, String img, String size, String color, String neckAround, String shoulderWidth, String waistAround, String bicepAround, String sleeveLength, String chestAround, String shirtLength, String wristAround) {
        this._id = _id;
        this.id = id;
        this.type = type;
        this.email = email;
        this.selleremail = selleremail;
        this.status = status;
        this.count = count;
        this.price = price;
        this.name = name;
        this.img = img;
        this.size = size;
        this.color = color;
        this.neckAround = neckAround;
        this.shoulderWidth = shoulderWidth;
        this.waistAround = waistAround;
        this.bicepAround = bicepAround;
        this.sleeveLength = sleeveLength;
        this.chestAround = chestAround;
        this.shirtLength = shirtLength;
        this.wristAround = wristAround;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSelleremail() {
        return selleremail;
    }

    public void setSelleremail(String selleremail) {
        this.selleremail = selleremail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getNeckAround() {
        return neckAround;
    }

    public void setNeckAround(String neckAround) {
        this.neckAround = neckAround;
    }

    public String getShoulderWidth() {
        return shoulderWidth;
    }

    public void setShoulderWidth(String shoulderWidth) {
        this.shoulderWidth = shoulderWidth;
    }

    public String getWaistAround() {
        return waistAround;
    }

    public void setWaistAround(String waistAround) {
        this.waistAround = waistAround;
    }

    public String getBicepAround() {
        return bicepAround;
    }

    public void setBicepAround(String bicepAround) {
        this.bicepAround = bicepAround;
    }

    public String getSleeveLength() {
        return sleeveLength;
    }

    public void setSleeveLength(String sleeveLength) {
        this.sleeveLength = sleeveLength;
    }

    public String getChestAround() {
        return chestAround;
    }

    public void setChestAround(String chestAround) {
        this.chestAround = chestAround;
    }

    public String getShirtLength() {
        return shirtLength;
    }

    public void setShirtLength(String shirtLength) {
        this.shirtLength = shirtLength;
    }

    public String getWristAround() {
        return wristAround;
    }

    public void setWristAround(String wristAround) {
        this.wristAround = wristAround;
    }
}
