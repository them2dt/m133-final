package ch.bzz.gamelibrary.data;

import ch.bzz.gamelibrary.model.Game;
import ch.bzz.gamelibrary.model.Publisher;
import ch.bzz.gamelibrary.service.Config;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class DataHandler {
    private static final DataHandler instance = new DataHandler();
    private static Map<String, Game> gameMap;
    private static Map<String, Publisher> publisherMap;

    /**
     * default constructor: defeat instantiation
     */
    private DataHandler() {
        gameMap = new HashMap<>();
        publisherMap = new HashMap<>();
        readJSON("gameJSON");
    }

    /**
     * restores the backup data
     */
    public static void restoreData() {
        gameMap = new HashMap<>();
        publisherMap = new HashMap<>();
        readJSON("backupJSON");
    }

    /**
     * Reads a Game with gameUUID
     *
     * @param gameUUID
     * @return game-object
     */
    public static Game readGame(String gameUUID) {
        Game game = new Game();
        if (getGameMap().containsKey(gameUUID)) {
            game = getGameMap().get(gameUUID);
        }
        return game;
    }

    /**
     * inserts a new game into the gameMap
     *
     * @param game
     */
    public static void insertGame(Game game) {
        getGameMap().put(game.getGameUUID(), game);
        writeJSON();
    }

    /**
     * updates the gameMap
     */
    public static void updateGame() {
        writeJSON();
    }

    /**
     * removes a game from the gameMap
     *
     * @param gameUUID
     * @return success
     */
    public static boolean deleteGame(String gameUUID) {
        if (getGameMap().remove(gameUUID) != null) {
            writeJSON();
            return true;
        } else
            return false;
    }

    /**
     * saves ein Game
     *
     * @param game
     */
    public static void saveGame(Game game) {
        getGameMap().put(game.getGameUUID(), game);
        writeJSON();
    }

    /**
     * Reads a  Publisher with the publisherUUID
     *
     * @param publisherUUID
     * @return publisher-object
     */
    public static Publisher readPublisher(String publisherUUID) {
        Publisher publisher = new Publisher();
        if (getPublisherMap().containsKey(publisherUUID)) {
            publisher = getPublisherMap().get(publisherUUID);
        }
        return publisher;
    }

    /**
     * inserts a new Publisher in an empty game
     *
     * @param publisher
     */
    public static void insertPublisher(Publisher publisher) {
        Game game = new Game();
        game.setGameUUID(UUID.randomUUID().toString());
        game.setName("");
        game.setPublisher(publisher);
        insertGame(game);
    }

    public static boolean updatePublisher(Publisher publisher) {
        boolean found = false;
        String publisherUUID = publisher.getPublisherUUID();
        if (getPublisherMap().containsKey(publisherUUID)) {
            getPublisherMap().put(publisherUUID, publisher);
            found = true;
        }

        for (Map.Entry<String, Game> entry : getGameMap().entrySet()) {
            Game game = entry.getValue();
            if (game.getPublisher().getPublisherUUID().equals(publisher.getPublisherUUID())) {
                game.setPublisher(publisher);
            }
        }
        writeJSON();
        return found;
    }

    /**
     * deletes a Publisher, if it has no games
     *
     * @param publisherUUID
     * @return errorCode  0=ok, -1=referential integrity, 1=not found
     */
    public static int deletePublisher(String publisherUUID) {
        int errorCode = 1;
        for (Map.Entry<String, Game> entry : getGameMap().entrySet()) {
            Game game = entry.getValue();
            if (game.getPublisher().getPublisherUUID().equals(publisherUUID)) {
                if (game.getName() == null || game.getName().equals("")) {
                    deleteGame(game.getGameUUID());
                } else {
                    return -1;
                }
            }
        }

        if (getPublisherMap().containsKey(publisherUUID)) {
            getPublisherMap().remove(publisherUUID);
            errorCode = 0;
            writeJSON();
        }

        return errorCode;
    }

    /**
     * Saves a Publisher
     *
     * @param publisher
     */
    public static void savePublisher(Publisher publisher) {
        getPublisherMap().put(publisher.getPublisherUUID(), publisher);
        writeJSON();
    }

    /**
     * returns the GameMap
     *
     * @return gameMap
     */
    public static Map<String, Game> getGameMap() {
        return gameMap;
    }

    /**
     * returns the PublisherMap
     *
     * @return publisherMap
     */
    public static Map<String, Publisher> getPublisherMap() {
        return publisherMap;
    }

    /**
     * sets the publisherMap
     *
     * @param publisherMap
     */

    public static void setPublisherMap(Map<String, Publisher> publisherMap) {
        DataHandler.publisherMap = publisherMap;
    }

    /**
     * Reads a game and the publisher
     */
    private static void readJSON(String propertyName) {
        try {
            byte[] jsonData = Files.readAllBytes(Paths.get(Config.getProperty(propertyName)));
            ObjectMapper objectMapper = new ObjectMapper();
            Game[] games = objectMapper.readValue(jsonData, Game[].class);
            for (Game game : games) {
                String publisherUUID = game.getPublisher().getPublisherUUID();
                Publisher publisher;
                if (getPublisherMap().containsKey(publisherUUID)) {
                    publisher = getPublisherMap().get(publisherUUID);
                } else {
                    publisher = game.getPublisher();
                    getPublisherMap().put(publisherUUID, publisher);
                }
                game.setPublisher(publisher);
                getGameMap().put(game.getGameUUID(), game);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * writes the Games und Publisher
     */
    private static void writeJSON() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer(new DefaultPrettyPrinter());
        FileOutputStream fileOutputStream = null;
        Writer fileWriter;

        String gamePath = Config.getProperty("gameJSON");
        try {
            fileOutputStream = new FileOutputStream(gamePath);
            fileWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8));
            objectMapper.writeValue(fileWriter, getGameMap().values());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
