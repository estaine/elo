<!DOCTYPE HTML>
<head>
    <title>${'Player stats - ' + playerStats.player.fullName}</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <link rel="stylesheet" href="/css/rating.css"/>
</head>
<body>

<div class="row justify-content-md-center">
    <div class="col-md-4">
        <table class="rwd-table" style="width: 468px;">
            <tr>
                <td>
                    <img style="border-radius: 50%; width: 100px; height: 100px;"
                         src="${'/userpics/' + playerStats.player.username + '.png'}"/>
                </td>
                <td colspan="2" style="font-size: 20pt;">
                    <div style="text-align: center">${playerStats.player.fullName}</div>
                    <div style="text-align: center">

                        <#list playerStats.player.awards as award>
                            <img src="${'/images/' + award.iconFilename}" title="${award.name}"
                                 style="margin: 10px 5px -5px; width: 20px; height: 20px;"/>
                        </#list>
                    </div>
                </td>
            </tr>
            <tr>
                <td></td>
                <td>Rating</td>
                <td>${playerStats.baseStats.rating?round?c}</td>
            </tr>
            <tr>
                <td></td>
                <td>Games played</td>
                <td>${playerStats.baseStats.matchesPlayed}</td>
            </tr>
            <tr>
                <td></td>
                <td>Games won</td>
                <td>${playerStats.wins}</td>
            </tr>
            <tr>
                <td></td>
                <td>Games lost</td>
                <td>${playerStats.losses}</td>
            </tr>
            <tr>
                <td></td>
                <td>Goals for</td>
                <td>${playerStats.goalsFor}</td>
            </tr>
            <tr>
                <td></td>
                <td>Goals against</td>
                <td>${playerStats.goalsAgainst}</td>
            </tr>
        </table>
    </div>
</div>
<div class="row justify-content-md-center">
    <div class="col-md-6">
        <table class="rwd-table">
            <tr>
                <th>
                    <div style="width: 12px; height: 12px; border-radius: 50%; background-color: red; margin: auto;"/>
                </th>
                <th></th>
                <th>
                    <div style="width: 12px; height: 12px; border-radius: 50%; background-color: yellow; margin: auto;"/>
                </th>
            </tr>
            <#list playerStats.matches as match>
                <tr>
                    <td>
                        <img style="border-radius: 50%; width: 36px; height: 36px;"
                             src="${'/userpics/' + match.redTeamPlayer1.username + '.png'}"/>
                        <img style="border-radius: 50%; width: 36px; height: 36px;"
                             src="${'/userpics/' + match.redTeamPlayer2.username + '.png'}"/>
                    </td>
                    <td class="${playerStats.results?api.get(match.id)?then('score-win','score-lose')}">
                        <div style="text-align: center;">${match.redTeamGoals + ' : ' + match.yellowTeamGoals}</div>
                        <div style="text-align: center; font-size: 8pt;">${playerStats.results?api.get(match.id)?then('+ ', '– ') + playerStats.ratingDelta?api.get(match.id)?round?c}</div>
                    </td>
                    <td>
                        <img style="border-radius: 50%; width: 36px; height: 36px;"
                             src="${'/userpics/' + match.yellowTeamPlayer1.username + '.png'}"/>
                        <img style="border-radius: 50%; width: 36px; height: 36px;"
                             src="${'/userpics/' + match.yellowTeamPlayer2.username + '.png'}"/>
                    </td>
                </tr>
            </#list>
        </table>
    </div>
</div>


</body>
</html>