<!DOCTYPE HTML>
<head>
    <title>ZenSoft kicker rating</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link rel="stylesheet" href="/css/rating.css" />
</head>
<body>


<!--<div class="navigation">-->
<!--<a href="/tournament/groups">Tournament</a>-->
<!--</div>-->

<table class="rwd-table">
    <tr>
        <th>#</th>
        <th colspan="2" style="text-align: center">Player</th>
        <th  style="text-align: center">Games</th>
        <th  style="text-align: center">Rating</th>
    </tr>
    <#list ratings as playerStats>
        <tr>
            <td>${playerStats?counter}</td>
            <td><img style="border-radius: 50%; width: 28px; height: 28px;"
                     src="${'/userpics/' + playerStats.player.username + '.png'}"/></td>
            <td><a href="${'/player/' + playerStats.player.username}">${playerStats.player.fullName}</a></td>
            <td>${playerStats.baseStats.matchesPlayed}</td>
            <td>${playerStats.baseStats.rating?round?c}</td>
        </tr>
    </#list>
</table>

</body>
</html>