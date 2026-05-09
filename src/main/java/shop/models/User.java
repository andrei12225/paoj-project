package shop.models;

import shop.enums.UserPermission;
import shop.enums.UserRole;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class User {
    private int userID;
    private String username;
    private String password;
    private UserRole role;
    private Map<UserPermission, Boolean> permissions = new HashMap<>();
    private LocalDateTime lastLogin;

    public User(int userID, String username, String password, UserRole role) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.role = role;

        if (role == UserRole.ADMIN) {
            for (UserPermission permission : UserPermission.values()) {
                permissions.put(permission, true);
            }
        }
    }

    public void updateLastLogin() {
        this.lastLogin = LocalDateTime.now();
    }

    public boolean hasPermission(UserPermission permission) {
        return permissions.getOrDefault(permission, false);
    }

    public String getDisplayId() {
        return "USR-" + String.format("%06d", userID);
    }

    @Override
    public String toString() {
        return "User {" +
            " userID='" + getDisplayId() + "'" +
            ", username='" + username + "'" +
            ", password='" + password + "'" +
            ", role='" + role + "'" +
            ", lastLogin='" + lastLogin + "'" +
            ", permissions='" + permissions.keySet().stream().filter(k -> permissions.get(k)).toList() + "'" +
            " }";
    }

    public int getUserID() {
        return this.userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public UserRole getRole() {
        return this.role;
    }

    public LocalDateTime getLastLogin() {
        return this.lastLogin;
    }

    public Map<UserPermission, Boolean> getPermissions() {
        return permissions;
    }
}
