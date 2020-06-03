package com.example.uberapp.pojo;

public class info {
    private String carofdriver;
    private String email;
    private String image;
    private String name;
    private String phone;
    private String type;

    public info(String email, String phone, String name, String image, String carofdriver) {
        this.email = email;
        this.phone = phone;
        this.name = name;
        this.image = image;
        this.carofdriver = carofdriver;
    }

    public info(String email, String phone, String name, String image) {
        this.email = email;
        this.phone = phone;
        this.name = name;
        this.image = image;

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCarofdriver() {
        return carofdriver;
    }

    public void setCarofdriver(String carofdriver) {
        this.carofdriver = carofdriver;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
