package ch.bzz.gamelibrary.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.ws.rs.FormParam;


public class Publisher {

    @FormParam("publisherUUID")
    @Pattern(regexp = "|[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}")
    private String publisherUUID;

    @FormParam("publisher")
    @NotEmpty
    @Size(min = 2, max = 50)
    private String publisher;


    /**
     * Default constructor
     */
    public Publisher() {
        setPublisherUUID(null);
        setPublisher(null);
    }

    /**
     * returns the publisherUUID
     *
     * @return publisherUUID
     */
    public String getPublisherUUID() {
        return publisherUUID;
    }

    /**
     * Sets the publisherUUID
     *
     * @param publisherUUID
     */

    public void setPublisherUUID(String publisherUUID) {
        this.publisherUUID = publisherUUID;
    }

    /**
     * returns the Publisher
     *
     * @return publisher
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * sets the Publisher
     *
     * @param publisher
     */

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }


}