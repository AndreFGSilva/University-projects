package src.model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Businesses implements Serializable {
    private TreeSet<Business> businesses;

	public Businesses() {
		this.businesses = new TreeSet<>();
	}

    public Businesses(TreeSet<Business> businesses) {
        this.businesses = businesses;
    }

    public Businesses(Businesses b) {
        this.businesses = b.getTreeBusinesses();
    }

    // Getters
    public TreeSet<Business> getTreeBusinesses() {
        return this.businesses;
    }

    // Clone
    public Businesses clone() {
        return new Businesses(this);
    }

    // Método que valida o businesse e o adiciona à TreeSet.
    public boolean addBusiness(Business business) {
        if (!business.getBusinessID().equals("business_id") && !business.getBusinessID().equals("") && !business.getName().equals("") && !business.getCity().equals("") && !business.getState().equals(""))
            return businesses.add((Business) business);
        return false;
    }

    public List<Business> filter(Predicate<? super Business> predicate) {
        return businesses.stream().filter(predicate).collect(Collectors.toList());
    }

	public Set<String> setIDs() {
		return this.businesses.stream().map(Business::getBusinessID).collect(Collectors.toSet());
	}

    // Hash code
	public int hashCode() {
		return Objects.hash(businesses);
	}

    public String getBusinessCity(String bizid) {
        for(Business b : this.businesses)
            if (b.getBusinessID().equals(bizid))
                return b.getCity();
        return "empty";
    }
}
