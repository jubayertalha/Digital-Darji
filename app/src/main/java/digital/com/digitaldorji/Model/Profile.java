package digital.com.digitaldorji.Model;

public class Profile {

    private int id;
    private int sell;
    private int portfolio;
    private String name;
    private String occupation;
    private String address;
    private String img;
    private String category;

    public Profile(int id, int sell, int portfolio, String name, String occupation, String address, String img, String category) {
        this.id = id;
        this.sell = sell;
        this.portfolio = portfolio;
        this.name = name;
        this.occupation = occupation;
        this.address = address;
        this.img = img;
        this.category = category;
    }

    public int getSell() {
        return sell;
    }

    public void setSell(int sell) {
        this.sell = sell;
    }

    public int getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(int portfolio) {
        this.portfolio = portfolio;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
