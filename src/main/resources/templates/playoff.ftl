<!DOCTYPE HTML>
<head>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous"/>
    <title>${tournament.name}</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <link rel="stylesheet" href="/css/rating.css"/>
</head>
<body>


<div class="row justify-content-md-center" style="text-align: center;">
    <div class="col-md-12">
    <#list tournament.seriesByStage?keys as stage>
        <div>
            <h3>${stage?api.getName()}</h3>
            <table class="rwd-table">
                <#list tournament.seriesByStage?api.get(stage) as serie>
                    <tr>
                        <td style="text-align: center; vertical-align: top;">
                            <#if serie.firstTeam??>
                                <img style="border-radius: 50%; width: 36px; height: 36px;"
                                     src="${'/userpics/' + serie.firstTeam.player1.username + '.png'}"/>
                                <img style="border-radius: 50%; width: 36px; height: 36px;"
                                     src="${'/userpics/' + serie.firstTeam.player2.username + '.png'}"/>
                            </#if>
                        </td>
                        <td style="text-align: center;">
                            <#if serie.firstTeam?? && serie.secondTeam??>
                                <div style="text-align: center; font-size: 20pt;">${serie.firstTeamWinCount + ':' + serie.secondTeamWinCount}</div>
                                <#list serie.playoffMatches as playoffMatch>
                                    <#if playoffMatch.played>
                                        <div style="text-align: center; font-size: 8pt;">
                                            <#if playoffMatch.redTeam.id == serie.firstTeam.id>
                                                <div style="width: 6px;height: 6px;border-radius: 50%;background-color: red;margin-right: 4px;display: inline-block; margin-bottom: 1px;"></div>
                                            ${playoffMatch.match.redTeamGoals + ':' + playoffMatch.match.yellowTeamGoals}
                                                <div style="width: 6px;height: 6px;border-radius: 50%;background-color: yellow;margin-left: 4px;display: inline-block; margin-bottom: 1px;"></div>
                                            <#else>
                                                <div style="width: 6px;height: 6px;border-radius: 50%;background-color: yellow;margin-right: 4px;display: inline-block; margin-bottom: 1px;"></div>
                                            ${playoffMatch.match.yellowTeamGoals + ':' + playoffMatch.match.redTeamGoals}
                                                <div style="width: 6px;height: 6px;border-radius: 50%;background-color: red;margin-left: 4px;display: inline-block; margin-bottom: 1px;"></div>
                                            </#if>
                                        </div>
                                    </#if>
                                </#list>
                            <#else>
                                <div style="text-align: center; font-size: 20pt;">${'-:-'}</div>
                            </#if>

                        </td>
                        <td style="text-align: center; vertical-align: top;">
                            <#if serie.secondTeam??>
                                <img style="border-radius: 50%; width: 36px; height: 36px;"
                                     src="${'/userpics/' + serie.secondTeam.player1.username + '.png'}"/>
                                <img style="border-radius: 50%; width: 36px; height: 36px;"
                                     src="${'/userpics/' + serie.secondTeam.player2.username + '.png'}"/>
                            </#if>
                        </td>
                    </tr>
                </#list>
            </table>
        </div>
    </#list>
    </div>
</div>
</body>
</html>