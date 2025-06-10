package com.java.revaniexchange.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Data Model for a Product.
 * Uses JavaFX properties for easy binding with TableView.
 */
public class Product {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty name;
    private final SimpleStringProperty description;
    private final SimpleDoubleProperty price;
    private final SimpleStringProperty imageUrl;

    public Product(int id, String name, String description, double price, String imageUrl) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.description = new SimpleStringProperty(description);
        this.price = new SimpleDoubleProperty(price);
        this.imageUrl = new SimpleStringProperty(imageUrl);
    }

    public int getId() { return id.get(); }
    public String getName() { return name.get(); }
    public String getDescription() { return description.get(); }
    public double getPrice() { return price.get(); }
    public String getImageUrl() { return imageUrl.get(); }
}
