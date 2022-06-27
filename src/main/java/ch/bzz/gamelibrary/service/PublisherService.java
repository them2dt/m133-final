package ch.bzz.gamelibrary.service;

import ch.bzz.gamelibrary.data.DataHandler;
import ch.bzz.gamelibrary.model.Publisher;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.UUID;


@Path("publisher")
public class PublisherService {

    /**
     * Creates a map from all publishers
     *
     * @return Response
     */
    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listPublisher(
    ) {
        Map<String, Publisher> publisherMap = DataHandler.getPublisherMap();

        return UserRole.createResponse(200, publisherMap);
    }

    /**
     * Reads Publisher with the publisherUUID
     *
     * @param publisherUUID the publisherUUID
     * @return Response
     */
    @GET
    @Path("read")
    @Produces(MediaType.APPLICATION_JSON)
    public Response readPublisher(
            @NotEmpty
            @Pattern(regexp = "[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}")
            @QueryParam("uuid") String publisherUUID
    ) {
        int httpStatus = 404;

        Publisher publisher = DataHandler.readPublisher(publisherUUID);
        if (publisher.getPublisher() != null) {
            httpStatus = 200;
        }

        return UserRole.createResponse(httpStatus, publisher);
    }

    /**
     * creates a new Publisher without games
     *
     * @param publisher the publisher object
     * @return Response
     */

    @POST
    @Path("create")
    @Produces(MediaType.TEXT_PLAIN)
    public Response createPublisher(
            @Valid @BeanParam Publisher publisher
    ) {
        publisher.setPublisherUUID(UUID.randomUUID().toString());
        DataHandler.insertPublisher(publisher);

        return UserRole.createResponse(200, "");
    }

    /**
     * updates the Publisher in all it's games
     *
     * @param publisher the publisher object
     * @return Response
     */
    @PUT
    @Path("update")
    @Produces(MediaType.TEXT_PLAIN)
    public Response updatePublisher(
            @Valid @BeanParam Publisher publisher
    ) {
        int httpStatus = DataHandler.updatePublisher(publisher) ? 200 : 404;

        return UserRole.createResponse(httpStatus, "");
    }

    /**
     * deletes a Publisher
     *
     * @param publisherUUID the uuid of the Publisher to be deleted
     * @return Response
     */
    @DELETE
    @Path("delete")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deletePublisher(
            @NotEmpty
            @Pattern(regexp = "[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}")
            @QueryParam("uuid") String publisherUUID
    ) {
        int httpStatus = 404;

        int errorCode = DataHandler.deletePublisher(publisherUUID);
        if (errorCode == 0) httpStatus = 200;
        else if (errorCode == -1) httpStatus = 409;

        return UserRole.createResponse(httpStatus, "");
    }
}
