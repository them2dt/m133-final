/**
 * register listeners and load the game data
 */
$(document).ready(function () {
    loadPublisher();
    loadGame();

    $("#logoff").click(sendLogoffinGameedit);
    $("#save").click(saveGame)

    /**
     * listener for submitting the form
     */
    $("#gameeditForm").submit(saveGame);

    /**
     * listener for button [cancel], redirects to gamelibrary
     */
    $("#cancel").click(function () {
        window.location.href = "./gamelibrary.html";

    });
});

/**
 *  loads the data of this game
 *
 */
function loadGame() {
    let gameUUID = $.urlParam("uuid");
    if (gameUUID) {
        $
            .ajax({
                url: "./resource/game/read?uuid=" + gameUUID,
                dataType: "json",
                type: "GET"
            })
            .done(showGame)
            .fail(function (xhr, status, errorThrown) {
                if (xhr.status == 403) {
                    window.location.href = "./login.html";
                } else if (xhr.status == 404) {
                    $("#message").text("No game found.");
                } else {
                    window.location.href = "./gamelibrary.html";
                }
            })
    }
}

/**
 * shows the data of this game
 * @param  game  the game data to be shown
 */
function showGame(game) {
    $("#gameUUID").val(game.gameUUID);
    $("#name").val(game.name);
    $("#publisher").val(game.publisher.publisherUUID);
    $("#category").val(game.category);
}

/**
 * sends the game data to the webservice
 * @param form the form being submitted
 */
function saveGame(form) {
    form.preventDefault();

    let url = "./resource/game/";
    let type;

    let gameUUID = $("#title").val();
    console.log(gameUUID);
    if (gameUUID) {
        type = "PUT";
        url += "update"
        console.log("updating a game");

    } else {
        type = "POST";
        url += "create";
        console.log("creating a game");

    }

    $
        .ajax({
            url: url,
            dataType: "text",
            type: type,
            data: $("#gameeditForm").serialize()
        })
        .done(function (jsonData) {
            window.location.href = "./gamelibrary.html"
        })
        .fail(function (xhr, status, errorThrown) {
            if (xhr.status == 404) {
                $("#message").text("Game doesn't exist.");
            } else {
                $("#message").text("Error at saving game.");
            }
        })
}

function loadPublisher() {
    $
        .ajax({
            url: "./resource/publisher/list",
            dataType: "json",
            type: "GET"
        })
        .done(showPublisher)
        .fail(function (xhr, status, errorThrown) {
            if (xhr.status == 404) {
                $("#message").text("No game found.");
            } else {
                window.location.href = "./gamelibrary.html";
            }
        })
}

function showPublisher(publisher) {

    $.each(publisher, function (uuid, publisher) {
        $('#publisher').append($('<option>', {
            value: publisher.publisherUUID,
            text: publisher.publisher
        }));
    });
}

function sendLogoffinGameedit() {
    $
        .ajax({
            url: "./resource/user/logout",
            dataType: "text",
            type: "DELETE"
        })
        .done(function () {
            window.location.href = "./login.html";
        })
        .fail(function (xhr, status, errorThrown) {

            $("#message").text("Error occurred.");

        })
}
