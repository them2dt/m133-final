package ch.bzz.gamelibrary.service;

import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

public class UserRole {

    public static boolean isInvalid(String userRole) {
        boolean isValid = userRole.equals("user") || userRole.equals("admin");
        return !isValid;
    }

    public static Response createInvalidUserResponse(String userRole) {
        return createResponse(403, "invalid user role", userRole);
    }

    public static Response createResponse(int httpStatus, Object entity) {
        return Response
                .status(httpStatus)
                .entity(entity)
                .build();
    }

    public static Response createResponse(int httpStatus, Object entity, String userRole) {
        NewCookie cookie = renewCookie(userRole);
        return Response
                .status(httpStatus)
                .entity(entity)
                .cookie(cookie)
                .build();
    }

    private static NewCookie renewCookie(String userRole) {
        return new NewCookie(
                "userRole",
                userRole,
                "/",
                "",
                "Login-Cookie",
                600,
                false
        );
    }
}
