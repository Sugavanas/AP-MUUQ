package sample.objects;

import javafx.scene.image.Image;

public class Finalist extends Objects{
    private String id;
    private String name;
    private String password;
    private String country;
    private String countryImage;
    private String image;
    private String anthem;

    public Finalist(String data) {
        super(data);
    }

    @Override
    public void loadFromString(String data) {
        try {
            String[] parts = data.split(":");
            this.id = parts[0];
            this.name = parts[1];
            this.password = parts[2];
            this.country = parts[3];
            this.countryImage = parts[4];
            this.image = parts[5];
            this.anthem = parts[6];
        } catch (Exception ex) {
            System.out.println("Error Loading."); //TODO log error
        }
    }

    @Override
    public String saveString() {
        return null;
    }

    @Override
    public String getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean checkPassword(String password) {
        return password.equals(this.password);
    }

    public String getCountry() {
        return country;
    }

    public Image getCountryImage() {
        return new Image("images/country/" + countryImage);
    }

    public Image getImage() {
        return new Image("images/profile/" + image);
    }
}
