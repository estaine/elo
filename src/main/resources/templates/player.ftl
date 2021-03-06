<!DOCTYPE HTML>
<head>
    <title>${'Player stats - ' + playerStats.player.fullName}</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<#include "include/imports.ftl">
</head>
<body>

<#include "include/header.ftl">

<div class="row justify-content-md-center" style="margin-top: 6em;">
    <div class="col-md-12">
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
                <td>${playerStats.baseStats.matchesPlayed?c}</td>
            </tr>
            <tr>
                <td></td>
                <td>Games rated</td>
                <td>${playerStats.baseStats.matchesRated?c}</td>
            </tr>
            <tr>
                <td></td>
                <td>Games won</td>
                <td>${playerStats.wins?c}</td>
            </tr>
            <tr>
                <td></td>
                <td>Games lost</td>
                <td>${playerStats.losses?c}</td>
            </tr>
            <tr>
                <td></td>
                <td>Goals for</td>
                <td>${playerStats.goalsFor?c}</td>
            </tr>
            <tr>
                <td></td>
                <td>Goals against</td>
                <td>${playerStats.goalsAgainst?c}</td>
            </tr>
        <#if playerStats.personalBests??>
            <tr class="personal-best">
                <td></td>
                <td>Longest win streak</td>
                <td>
                    <div>${playerStats.personalBests.longestWinStreak.length}</div>
                    <div style="font-size: 8pt; margin-top: -3px; ">
                    ${playerStats.personalBests.longestWinStreak.formattedStart + '&nbsp;-&nbsp;' + playerStats.personalBests.longestWinStreak.formattedEnd}
                    </div>
                </td>
            </tr>
            <tr class="personal-best">
                <td></td>
                <td>Longest loss streak</td>
                <td>
                    <div>${playerStats.personalBests.longestLossStreak.length}</div>
                    <div style="font-size: 8pt; margin-top: -3px; ">
                    ${playerStats.personalBests.longestLossStreak.formattedStart + '&nbsp;-&nbsp;' + playerStats.personalBests.longestLossStreak.formattedEnd}
                    </div>
                </td>
            </tr>
        </#if>
            <tr>
                <td style="text-align: center;">
                    <a href="${'/player/' + playerStats.bestPercentagePartner.partner.username}">
                        <img style="border-radius: 50%; width: 36px; height: 36px;"
                             src="${'/userpics/' + playerStats.bestPercentagePartner.partner.username + '.png'}"/>
                    </a>
                </td>
                <td>Best&nbsp;performing&nbsp;partner</td>
                <td>
                    <div>${playerStats.bestPercentagePartner.winRate?string.percent}</div>
                    <div style="font-size: 8pt; margin-top: -3px; ">
                        ${playerStats.bestPercentagePartner.winsTogether}&nbsp;wins&nbsp;in&nbsp;${playerStats.bestPercentagePartner.matchesTogether}&nbsp;matches
                    </div>
                </td>
            </tr>
            <tr>
                <td style="text-align: center;">
                    <a href="${'/player/' + playerStats.worstPercentagePartner.partner.username}">
                        <img style="border-radius: 50%; width: 36px; height: 36px;"
                             src="${'/userpics/' + playerStats.worstPercentagePartner.partner.username + '.png'}"/>
                    </a>
                </td>
                <td>Worst&nbsp;performing&nbsp;partner</td>
                <td>
                    <div>${playerStats.worstPercentagePartner.winRate?string.percent}</div>
                    <div style="font-size: 8pt; margin-top: -3px; ">
                        ${playerStats.worstPercentagePartner.winsTogether}&nbsp;wins&nbsp;in&nbsp;${playerStats.worstPercentagePartner.matchesTogether}&nbsp;matches
                    </div>
                </td>
            </tr>
            <tr>
                <td style="text-align: center;">
                    <a href="${'/player/' + playerStats.favouritePartner.partner.username}">
                        <img style="border-radius: 50%; width: 36px; height: 36px;"
                             src="${'/userpics/' + playerStats.favouritePartner.partner.username + '.png'}"/>
                    </a>
                </td>
                <td>Favourite&nbsp;partner</td>
                <td>
                    <div>${playerStats.favouritePartner.matchesTogether?c}</div>
                    <div style="font-size: 8pt; margin-top: -3px; ">
                    matches&nbsp;together
                    </div>
                </td>
            </tr>
        </table>
    </div>
</div>
<div class="row justify-content-md-center">
    <div class="col-md-12">
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
                <td style="text-align: center;">
                    <a href="${'/player/' + match.redTeamPlayer1.username}">
                        <img style="border-radius: 50%; width: 36px; height: 36px;"
                             src="${'/userpics/' + match.redTeamPlayer1.username + '.png'}"/>
                    </a>
                    <a href="${'/player/' + match.redTeamPlayer2.username}">
                        <img style="border-radius: 50%; width: 36px; height: 36px;"
                             src="${'/userpics/' + match.redTeamPlayer2.username + '.png'}"/>
                    </a>
                </td>
                <td class="${playerStats.results?api.get(match.id)?then('score-win','score-lose')}">
                    <div style="text-align: center; padding-top:3px;">${match.redTeamGoals + ' : ' + match.yellowTeamGoals}</div>
                    <div style="text-align: center; font-size: 8pt; margin-top: -2px; ">${playerStats.results?api.get(match.id)?then('+ ', '– ') + playerStats.ratingDelta?api.get(match.id)?round?c}</div>
                </td>
                <td style="text-align: center;">
                    <a href="${'/player/' + match.yellowTeamPlayer1.username}">
                        <img style="border-radius: 50%; width: 36px; height: 36px;"
                             src="${'/userpics/' + match.yellowTeamPlayer1.username + '.png'}"/>
                    </a>
                    <a href="${'/player/' + match.yellowTeamPlayer2.username}">
                        <img style="border-radius: 50%; width: 36px; height: 36px;"
                             src="${'/userpics/' + match.yellowTeamPlayer2.username + '.png'}"/>
                    </a>
                </td>
            </tr>
        </#list>
        </table>
    </div>
</div>


</body>
</html>