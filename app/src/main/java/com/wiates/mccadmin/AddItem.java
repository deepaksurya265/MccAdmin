package com.wiates.mccadmin;

class AddItem {

    String name;
    String quantity;
    String price;
    String description;

    public AddItem(String id, String name, String quantity, String price, String description) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.description = description;


    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
