var inited = false;
var yourTurn = false;
var yourLogin;
var playersLogin = new Array();


function InitGame(data) {
    if (data == "")
        return;
    if (data == "invalid session") {
        window.location.href = "/";
        return;
    }

    // refreshHard width height field yourLogin
    // opponentsCount [opponent0,opponent0Letter, opponent1, opponent1Letter...]
    // {turnPlayerLogin|winPlayerLogin}, ["win"]

    // create game field
    var fieldPart = '<button class="button_{0}_{1}" style="width: 25px; height: 25px;">{2}</button>';
    var field = '';
    var strArr = data.split(' ');
    if (strArr[0] != "refreshHard")
        return;

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

    yourLogin = strArr[4];

    var logins = '';
    var playersCount = parseInt(strArr[5]);
    var tmpl = "<div class='player_{0}'>[{1}] {0} <span class='player_{0}_extraInfo'></span></div>";
    for (var i = 0; i < playersCount; i++) {
        var playerLogin = strArr[6+i*2];
        playersLogin.push(playerLogin);
        var playerLetter = strArr[7+i*2];
        logins += tmpl.replace(/\{0\}/g, playerLogin).replace('{1}', playerLetter);
    }
    $(".Players")[0].innerHTML = logins;

    var win = (strArr[7+playersCount*2] == "win");

    var turnOrWinPlayerLogin = strArr[6+playersCount*2];
    if (turnOrWinPlayerLogin == yourLogin && !win)
        yourTurn = true;

    $(".player_{0}".replace('{0}', yourLogin))[0].style.fontWeight = 'bold';
    var extraInfo = "| ходит";
    if (win)
        extraInfo = "| выиграл";
    $(".player_{0}_extraInfo".replace('{0}', turnOrWinPlayerLogin))[0].innerHTML = extraInfo;

    $(".LoadingMsg")[0].innerHTML = "";
    $(".GameInfo")[0].style.display = "";
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
    if (strArr.length <= 0)
        return;

    try {
        if (strArr[0] == "clicked") {
            var win = (strArr[5] == "win");
            console.log(!win);

            $('.button_{0}_{1}'.replace('{0}', strArr[1]).replace('{1}', strArr[2]))[0].innerHTML = strArr[3];
            for (var i = 0; i < playersLogin.length; ++i) {
                $(".player_{0}_extraInfo".replace('{0}', playersLogin[i]))[0].innerHTML = '';
            }

            if (win) {
                $(".player_{0}_extraInfo".replace('{0}', strArr[4]))[0].innerHTML = '| выиграл';
            } else {
                $(".player_{0}_extraInfo".replace('{0}', strArr[4]))[0].innerHTML = '| ходит';
            }

            if (strArr[4] == yourLogin && !win) {
                yourTurn = true;
            }
        }
    } catch (e) {
        console.log(e);
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

    $.get(url, func);
}