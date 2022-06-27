package ch.bzz.gamelibrary.service;

import ch.bzz.gamelibrary.data.DataHandler;
import ch.bzz.gamelibrary.model.Game;
import ch.bzz.gamelibrary.model.Publisher;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;


@Path("game")
public class GameService {

    /**
     * Creates a  Map from all games
     *
     * @return Response
     */
    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listGames(
            @CookieParam("userRole") String userRole
    ) {
        if (UserRole.isInvalid(userRole)) return UserRole.createInvalidUserResponse(userRole);

        return UserRole.createResponse(200, DataHandler.getGameMap(), userRole);

    }

    /**
     * Reads a Game with the gameUUID
     *
     * @param gameUUID
     * @return Response
     */
    @GET
    @Path("read")
    @Produces(MediaType.APPLICATION_JSON)
    public Response readGame(
            @NotEmpty
            @Pattern(regexp = "[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}")
            @QueryParam("uuid") String gameUUID,
            @CookieParam("userRole") String userRole
    ) {
        if (UserRole.isInvalid(userRole)) return UserRole.createInvalidUserResponse(userRole);
        Game game = DataHandler.readGame(gameUUID);
        ;

        int httpStatus = 404;
        if (game.getName() != null) {
            httpStatus = 200;
        }

        return UserRole.createResponse(httpStatus, game);
    }

    /**
     * creates a new game
     *
     * @param game
     * @param publisherUUID
     * @return Response
     */
    @POST
    @Path("create")
    @Produces(MediaType.TEXT_PLAIN)
    public Response createGame(
            @Valid @BeanParam Game game,
            @NotEmpty
            @Pattern(regexp = "[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}")
            @FormParam("publisherUUID") String publisherUUID,
            @CookieParam("userRole") String userRole
    ) {
        if (UserRole.isInvalid(userRole)) return UserRole.createInvalidUserResponse(userRole);
        int httpStatus = 404;

        game.setGameUUID(UUID.randomUUID().toString());
        Publisher publisher = DataHandler.readPublisher(publisherUUID);
        if (publisher != null) {
            game.setPublisher(publisher);
            DataHandler.insertGame(game);
            httpStatus = 200;
        }

        return UserRole.createResponse(httpStatus, "");
    }

    /**
     * updates an existing game
     *
     * @param game
     * @param publisherUUID
     * @return Response
     */
    @PUT
    @Path("update")
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateGame(
            @Valid @BeanParam Game game,
            @NotEmpty
            @Pattern(regexp = "[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}")
            @FormParam("publisherUUID") String publisherUUID,
            @CookieParam("userRole") String userRole
    ) {
        if (UserRole.isInvalid(userRole)) return UserRole.createInvalidUserResponse(userRole);
        int httpStatus;

        Game oldGame = DataHandler.readGame(game.getGameUUID());
        if (oldGame.getName() != null) {
            httpStatus = 200;
            oldGame.setName(game.getName());
            oldGame.setCategory(game.getCategory());

            Publisher publisher = DataHandler.readPublisher(publisherUUID);
            oldGame.setPublisher(publisher);
            DataHandler.updateGame();
        } else {
            httpStatus = 404;
        }

        return UserRole.createResponse(httpStatus, "");
    }

    /**
     * deletes an existing game identified by its uuid
     *
     * @param gameUUID
     * @return Response
     */
    @DELETE
    @Path("delete")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteGame(
            @NotEmpty
            @Pattern(regexp = "[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}")
            @QueryParam("uuid") String gameUUID,
            @CookieParam("userRole") String userRole
    ) {
        if (UserRole.isInvalid(userRole)) return UserRole.createInvalidUserResponse(userRole);
        int httpStatus = 404;
        if (DataHandler.deleteGame(gameUUID)) {
            httpStatus = 200;
        }

        return UserRole.createResponse(httpStatus, "");
    }

    /**
     * sets the attribute values of the game object
     *
     * @param game
     * @param name
     * @param publisherUUID
     * @param category
     */
    private void setValues(
            Game game,
            String name,
            String publisherUUID,
            String category) {
        game.setName(name);
        game.setCategory(category);

        Publisher publisher = DataHandler.getPublisherMap().get(publisherUUID);
        game.setPublisher(publisher);
    }
}
