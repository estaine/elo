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
                            <div style="text-align: center; font-size: 20pt;">${'2:2'}</div>
                            <div style="text-align: center; font-size: 8pt;">10:6</div>
                            <div style="text-align: center; font-size: 8pt;">7:10</div>
                            <div style="text-align: center; font-size: 8pt;">10:9</div>
                            <div style="text-align: center; font-size: 8pt;">8:10</div>
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