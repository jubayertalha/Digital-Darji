package digital.com.digitaldorji.Model;

public class Tailor {
    int _id;
    String type;
    String email;
    String occupation;
    String address;
    String category;
    String pass;
    String name;
    String img;
    int star;
    int total;
    int star_5;
    int star_4;
    int star_3;
    int star_2;
    int star_1;
    String token;

    public Tailor(int _id, String type, String email, String occupation, String address, String category, String pass, String name, String img, int star, int total, int star_5, int star_4, int star_3, int star_2, int star_1, String token) {
        this._id = _id;
        this.type = type;
        this.email = email;
        this.occupation = occupation;
        this.address = address;
        this.category = category;
        this.pass = pass;
        this.name = name;
        this.img = img;
        this.star = star;
        this.total = total;
        this.star_5 = star_5;
        this.star_4 = star_4;
        this.star_3 = star_3;
        this.star_2 = star_2;
        this.star_1 = star_1;
        this.token = token;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
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

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public int getStar_5() {
        return star_5;
    }

    public void setStar_5(int star_5) {
        this.star_5 = star_5;
    }

    public int getStar_4() {
        return star_4;
    }

    public void setStar_4(int star_4) {
        this.star_4 = star_4;
    }

    public int getStar_3() {
        return star_3;
    }

    public void setStar_3(int star_3) {
        this.star_3 = star_3;
    }

    public int getStar_2() {
        return star_2;
    }

    public void setStar_2(int star_2) {
        this.star_2 = star_2;
    }

    public int getStar_1() {
        return star_1;
    }

    public void setStar_1(int star_1) {
        this.star_1 = star_1;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
