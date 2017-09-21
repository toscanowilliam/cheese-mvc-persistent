package org.launchcode.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Menu {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @Size(min=3, max=15)
    private String name;

    @ManyToMany
    private List<Cheese> cheeses;

    // Adds an item to the cheeses list
    public void addItem(Cheese item) {
        cheeses.add(item);
    }
    // Removes an item from the cheeses list

    public void removeItem(Cheese item) {
        cheeses.remove(item);
    }

    // Default constructor to be used by Hibernate
    public Menu() {
    }

    // Constructor to accept parameter to set name
    public Menu(String name) {
        this.name = name;
    }

    // Appropriate getters amd setters for fields above

    public int getId() { return id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public List<Cheese> getCheeses() { return cheeses; }

}