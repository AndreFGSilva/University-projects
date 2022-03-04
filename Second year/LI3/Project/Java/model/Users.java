package src.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Users implements Serializable {
    private TreeSet<User> users;

    public Users() {
        this.users = new TreeSet<>();
    }

    public Users(TreeSet<User> users) {
        this.users = users;
    }

    public Users(Users u) {
        this.users = u.getTreeUsers();
    }

    // Getters
    public TreeSet<User> getTreeUsers() {
        return this.users;
    }

    // Clone
    public Users clone() {
        return new Users(this);
    }

    // Método que valida o utilizador e caso seja válido, o adiciona à Tree.
    public boolean addUser(User user) {
        if (!user.getID().equals("user_id") && !user.getID().equals("") && !user.getUserName().equals("")) return users.add((User) user);
        else return false;
    }

    // 
    public Set<String> setIDs() {
        return this.users.stream().map(User::getID).collect(Collectors.toSet());
    }

    // Hash code
    public int hashCode() {
        return Objects.hash(users);
    }
}