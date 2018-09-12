package pl.aszul.hot.rwb.model;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.security.core.GrantedAuthority;

import java.util.HashSet;
import java.util.Set;

public enum Role implements GrantedAuthority {

    ADMIN, WHEREIS, WHEREIS_DETAILS, WHEREIS_ADMIN, ANONYMOUS, OTHER;

    @Override
    public String getAuthority() {
        return this.toString();
    }

    public static Role fromString(String input) {
        for (Role role : Role.values()) {
            if (role.toString().equalsIgnoreCase(input)) {
                return role;
            }
        }
        return Role.OTHER;
    }

    public static Set<Role> stringArrayToSet(String[] roles) {
        var result = new HashSet<Role>();
        for (String role : roles) {
            result.add(Role.fromString(role.toUpperCase().replace("-", "_")));
        }
        return result;
    }

    public static boolean isAuthorizedForManagement(String[] roles) {
        return roles != null &&
                (ArrayUtils.contains(roles, "admin") || ArrayUtils.contains(roles, "whereis-admin"));
    }

}
