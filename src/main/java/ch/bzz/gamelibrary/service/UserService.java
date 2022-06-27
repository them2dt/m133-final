package ch.bzz.gamelibrary.service;

import ch.bzz.gamelibrary.data.UserData;
import ch.bzz.gamelibrary.model.User;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("user")
public class UserService {

    /**
     * login a user with username/password
     *
     * @param username the username
     * @param password the password
     * @return Response-object with the userRole
     */
    @POST
    @Path("login")
    @Produces(MediaType.TEXT_PLAIN)
    public Response login(
            @FormParam("username") String username,
            @FormParam("password") String password
    ) {
        User user = UserData.findUser(username, password);
        String userRole = user.getRole();
        int httpStatus = userRole.equals("guest") ? 401 : 200;

        return UserRole.createResponse(httpStatus, "", userRole);
    }

    /**
     * logout current user
     *
     * @return Response object with guest-Cookie
     */
    @DELETE
    @Path("logout")
    @Produces(MediaType.TEXT_PLAIN)
    public Response logout() {

        return UserRole.createResponse(200, "", "guest");
    }
}
