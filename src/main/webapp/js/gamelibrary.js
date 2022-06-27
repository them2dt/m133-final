/**
 * register listeners and load all games
 */
$(document).ready(function () {
    loadGames();

    $("#logoff").click(sendLogoff);

    /**
     * listener for buttons within form
     */
    $("#shopForm").on("click", "button", function () {
        if (confirm("Do you want to delete this game?")) {
            deleteGame(this.value);
        }
    });
});

function loadGames() {
    $
        .ajax({
            url: "./resource/game/list",
            dataType: "json",
            type: "GET"
        })
        .done(showGames)
        .fail(function (xhr, status, errorThrown) {
            if (xhr.status == 403) {
                window.location.href = "./login.html";
            } else if (xhr.status == 404) {
                $("#message").text("No games available");
            } else {
                $("#message").text("Error at reading game");
            }
        })
}

/**
 * shows all games as a table
 *
 * @param gameData all games as an array
 */
function showGames(gameData) {

    let table = document.getElementById("gamelibrary");
    clearTable(table);

    $.each(gameData, function (uuid, game) {
        if (game.name) {
            let row = table.insertRow(-1);
            let cell = row.insertCell(-1);
            cell.innerHTML = game.name;

            cell = row.insertCell(-1);
            cell.innerHTML = game.publisher.publisher;

            cell = row.insertCell(-1);
            cell.innerHTML = game.category;

            cell = row.insertCell(-1);
            cell.innerHTML = "<a href='./gameedit.html?uuid=" + uuid + "'>Edit</a>";

            cell = row.insertCell(-1);
            cell.innerHTML = "<button type='button' id='delete_" + uuid + "' value='" + uuid + "'>Delete</button>";
        }
    });
}

function clearTable(table) {
    while (table.hasChildNodes()) {
        table.removeChild(table.firstChild);
    }
}

/**
 * send delete request for a game
 * @param gameUUID
 */
function deleteGame(gameUUID) {
    $
        .ajax({
            url: "./resource/game/delete?uuid=" + gameUUID,
            dataType: "text",
            type: "DELETE",
        })
        .done(function (data) {
            loadGames();
            $("#message").text("Game deleted");

        })
        .fail(function (xhr, status, errorThrown) {
            $("#message").text("Error at deleting the game");
        })
}
