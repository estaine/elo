<!DOCTYPE HTML>
<head>
    <title>${tournament.name}</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<#include "include/imports.ftl">
</head>
<body>

<#include "include/header.ftl">

<div class="row justify-content-md-center" style="text-align: center; margin-top: 5em;">
    <div class="col-md-12">
    <#list tournament.seriesByStage?keys as stage>
        <div>
            <h3>${stage?api.getName()}</h3>
            <table class="rwd-table">
                <#list tournament.seriesByStage?api.get(stage) as serie>
                    <tr>
                        <td style="text-align: center; vertical-align: top;">
                            <#if serie.firstTeam??>
                                <a href="${'/player/' + serie.firstTeam.player1.username}">
                                    <img style="border-radius: 50%; width: 36px; height: 36px;"
                                         src="${'/userpics/' + serie.firstTeam.player1.username + '.png'}"/>
                                </a>
                                <a href="${'/player/' + serie.firstTeam.player2.username}">
                                    <img style="border-radius: 50%; width: 36px; height: 36px;"
                                         src="${'/userpics/' + serie.firstTeam.player2.username + '.png'}"/>
                                </a>
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
                                <a href="${'/player/' + serie.secondTeam.player1.username}">
                                    <img style="border-radius: 50%; width: 36px; height: 36px;"
                                         src="${'/userpics/' + serie.secondTeam.player1.username + '.png'}"/>
                                </a>
                                <a href="${'/player/' + serie.secondTeam.player2.username}">
                                    <img style="border-radius: 50%; width: 36px; height: 36px;"
                                         src="${'/userpics/' + serie.secondTeam.player2.username + '.png'}"/>
                                </a>
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