package src.model;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class Business implements Comparable<Business>, Serializable {
    private final String businessID;
    private final String name;
    private final String city;
    private final String state;
    private final List<String> categories;

    public Business() {
        this.businessID = new String();
        this.name = new String();
        this.city = new String();
        this.state = new String();
        this.categories = new ArrayList<>();
    }

    public Business(String businessID) {
        this.businessID = businessID;
        this.name = null;
        this.city = null;
        this.state = null;
        this.categories = null;
    }

    public Business(String businessID, String name, String city, String state, List<String> categories) {
        this.businessID = businessID;
        this.name = name;
        this.city = city;
        this.state = state;
        this.categories = categories;
    }

    public Business(Business b) {
        this.businessID = b.getBusinessID();
        this.name = b.getName();
        this.city = b.getCity();
        this.state = b.getState();
        this.categories = b.getCategories();
    }

    // Getters
    public String getBusinessID() {
        return this.businessID;
    }

    public String getName() {
        return this.name;
    }

    public String getCity() {
        return this.city;
    }

    public String getState() {
        return this.state;
    }

    public List<String> getCategories() {
        return this.categories.stream().collect(Collectors.toList());
    }

    // Compare
    public int compareTo(Business b) {
        return this.businessID.compareTo(b.businessID);
    }

    // Equals
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null | getClass() != o.getClass()) return false;

        Business b = (Business) o;
        return  this.businessID.equals(b.getBusinessID());
    }

    // Clone
    public Business clone() {
        return new Business(this);
    }

    // Hash code
    public int hashCode() {
        return Objects.hash(businessID, name, city, state, categories);
    }
}