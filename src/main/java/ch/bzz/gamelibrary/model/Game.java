package ch.bzz.gamelibrary.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.ws.rs.FormParam;


public class Game {

    @FormParam("gameUUID")
    @Pattern(regexp = "|[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}")
    private String gameUUID;

    @FormParam("name")
    @NotEmpty
    @Size(min = 2, max = 40)
    private String name;

    @FormParam("category")
    @NotEmpty
    @Size(min = 1, max = 50)
    private String category;

    private Publisher publisher;

    /**
     * Default constructor
     */
    public Game() {
        setGameUUID(null);
        setName(null);
        setPublisher(null);
        setCategory(null);
    }

    /**
     * returns the gameUUID
     *
     * @return bookUUID
     */
    public String getGameUUID() {
        return gameUUID;
    }

    /**
     * Sets the gameUUID
     *
     * @param gameUUID the gameUUID
     */

    public void setGameUUID(String gameUUID) {
        this.gameUUID = gameUUID;
    }

    /**
     * returns the name
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name
     *
     * @param name the name
     */

    public void setName(String name) {
        this.name = name;
    }

    /**
     * returns the Publisher
     *
     * @return publisher
     */
    public Publisher getPublisher() {
        return publisher;
    }

    /**
     * Sets the Publisher
     *
     * @param publisher the publisher
     */

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }


    /**
     * returns the Category
     *
     * @return category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the Category
     *
     * @param category the Category
     */

    public void setCategory(String category) {
        this.category = category;
    }


}