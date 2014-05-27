var inited = false;
var yourTurn = false;
var gameEnd = false;
var yourId;
var playersId = new Array();


function clearPlayersExtraInfo() {
    for (var i = 0; i < playersId.length; ++i) {
        if (playersId[i] != undefined) {
            $(".player_{0}_extraInfo".replace('{0}', playersId[i]))[0].innerHTML = '';
        }
    }
}


function InitGame(data) {
    if (data == "")
        return;
    if (data == "invalid session") {
        window.location.href = "/";
        return;
    }
    if (data == "invalid game") {
        window.location.href = '/timer';
        return;
    }

    // refreshHard width height field yourLogin
    // opponentsCount [opponent0,opponent0Letter, opponent1, opponent1Letter...]
    // {turnPlayerLogin|winPlayerLogin}, ["win"]

    // create game field
    var fieldPart = '<button class="button_{0}_{1}" style="width: 25px; height: 25px;">{2}</button>';
    var field = '';
    var strArr = data.split(' ');
    var gameTime = strArr.shift();

    if (strArr[0] != "refreshHard")
        return;

    $(".GameTimeValue")[0].innerHTML = gameTime;

    var fieldWidth = parseInt(strArr[1], 10);
    var fieldHeight = parseInt(strArr[2], 10);
    for (var j = 0; j < fieldHeight; j++) {
        for (var i = 0; i < fieldWidth; i++) {
            var c = strArr[3][j*fieldWidth + i];
            if (c == 'n')
                c = '&nbsp;';
            field += fieldPart.replace('{0}', i).replace('{1}', j).replace('{2}', c);
        }
        field += '<br/>';
    }
    $(".GameField")[0].innerHTML = field;

    // onclick
    for (var j = 0; j < fieldHeight; j++) {
        for (var i = 0; i < fieldWidth; i++) {
            $(".button_{0}_{1}".replace("{0}", i).replace("{1}", j)).click(function(event) {
                if (!yourTurn)
                    return;
                yourTurn = false;

                var className = event.currentTarget.className;
                if ($("." + className)[0].innerHTML == '&nbsp;') {
                    $("." + className)[0].innerHTML = '*';
                }
                var arr = className.split('_');

                $.get(
                    '/ajax?action=user_click&x={0}&y={1}'.replace("{0}", arr[1]).replace("{1}", arr[2])
                );
            });
        }
    }

    yourId = strArr[4];

    var logins = '';
    var playersCount = parseInt(strArr[5]);
    var tmpl = "<div class='player_{0}'>[{2}] {1} <span class='player_{0}_extraInfo'></span></div>";
    for (var i = 0; i < playersCount; i++) {
        var playerLogin = strArr[6+i*3];
        var playerId = strArr[7+i*3];
        playersId.push(playerId);
        var playerLetter = strArr[8+i*3];
        logins += tmpl.replace(/\{0\}/g, playerId).replace('{1}', playerLogin).replace('{2}', playerLetter);
    }
    $(".Players")[0].innerHTML = logins;

    var win = (strArr[7+playersCount*3] == "win");

    var turnOrWinPlayerId = strArr[6+playersCount*3];
    if (turnOrWinPlayerId == yourId && !win)
        yourTurn = true;

    $(".player_{0}".replace('{0}', yourId))[0].style.fontWeight = 'bold';
    var extraInfo = "| ходит";
    if (win) {
        var gameStatus;
        if (turnOrWinPlayerId == yourId) {
            gameStatus = "Вы выиграли!";
        } else {
            gameStatus = "Вы проиграли =(";
        }
        $(".GameStatus")[0].innerHTML = gameStatus;
        $(".GameStatus")[0].style.fontWeight = 'bold';
        $(".GoToNewGame")[0].style.display = '';

        extraInfo = "| выиграл";
        gameEnd = true;
    }
    $(".player_{0}_extraInfo".replace('{0}', turnOrWinPlayerId))[0].innerHTML = extraInfo;

    $(".LoadingMsg")[0].innerHTML = "";
    $(".GameInfo")[0].style.display = "";

    $(".KickTurnPlayer").click(function (event) {
        $.get('/ajax?action=kick_turn_player');
        return false;
    });
    inited = true;
}


function UpdateGame(data) {
    if (data == "")
        return;
    if (data == "invalid session") {
        window.location.href = "/";
        return;
    }

    var strArr = data.split(' ');
    $(".GameTimeValue")[0].innerHTML = strArr.shift();

    if (strArr[0] == "clicked") {
        var win = (strArr[5] == "win");

        $('.button_{0}_{1}'.replace('{0}', strArr[1]).replace('{1}', strArr[2]))[0].innerHTML = strArr[3];
        clearPlayersExtraInfo();

        if (win) {
            $(".player_{0}_extraInfo".replace('{0}', strArr[4]))[0].innerHTML = '| выиграл';
            gameEnd = true;
            var gameStatus;
            if (strArr[4] == yourId) {
                gameStatus = "Вы выиграли!";
            } else {
                gameStatus = "Вы проиграли =(";
            }
            $(".GameStatus")[0].innerHTML = gameStatus;
            $(".GameStatus")[0].style.fontWeight = 'bold';
            $(".GoToNewGame")[0].style.display = '';
        } else {
            $(".player_{0}_extraInfo".replace('{0}', strArr[4]))[0].innerHTML = '| ходит';
        }

        if (strArr[4] == yourId && !win) {
            yourTurn = true;
        }
    } else if (strArr[0] == "kicked") {
        if (strArr[1] == yourId) {
            gameEnd = true;
            yourTurn = false;
            $(".GameStatus")[0].innerHTML = "Вы были исключены из игры";
            $(".GoToNewGame")[0].style.display = '';
        } else {
            delete playersId[playersId.indexOf(strArr[1])];
            $(".player_{0}".replace('{0}', strArr[1])).remove();

            clearPlayersExtraInfo();
            $(".player_{0}_extraInfo".replace('{0}', strArr[2]))[0].innerHTML = '| ходит';

            if (strArr[2] == yourId)
                yourTurn = true;
        }
    }
}


function refresh() {
    var url = null;
    var func = null;
    if (inited) {
        func = UpdateGame;
        url = '/ajax?action=game_refresh';
    } else {
        func = InitGame;
        url = '/ajax?action=game_refresh_hard';
    }

    if (!(inited && gameEnd))
        $.get(url, func);
}