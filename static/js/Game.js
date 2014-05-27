var inited = false;
var yourTurn = false;


function InitGame(data) {
    if (data == "")
        return;
    if (data == "invalid session") {
        window.location.href = "/";
        return;
    }

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
                c = '&nbsp';
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
                var arr = className.split('_');

                $.get(
                    '/ajax?action=user_click&x={0}&y={1}'.replace("{0}", arr[1]).replace("{1}", arr[2])
                );
            });
        }
    }

    if (strArr[4] == "you")
        yourTurn = true;
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
            $('.button_{0}_{1}'.replace('{0}', strArr[1]).replace('{1}', strArr[2]))[0].innerHTML = strArr[3];
            if (strArr[4] == "you")
                yourTurn = true;
        }
    } catch (e) {
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