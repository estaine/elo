<!DOCTYPE HTML>
<head>
    <title>${tournament.name}</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<#include "include/imports.ftl">
</head>
<body>

<#include "include/header.ftl">

<div class="row" style="margin-top:6em;">
    <div class="col-md-6">
        <table class="rwd-table" style="font-size:14pt;">
            <tr>
                <th>#</th>
                <th style="text-align: center;">Team</th>
                <th>+/-</th>
                <th>G</th>
                <th>Pts</th>
            </tr>

        <#list tournament.groups[0].teams as team>
            <tr>
                <td>${team?counter}</td>
                <td>
                    <div class="row" style="text-align: center; width: 280px;">
                        <div class="col-md-6">
                            <a href="${'/player/' + team.player1.username}">
                                <img style="border-radius: 50%; width: 44px; height: 44px;"
                                     src="${'/userpics/' + team.player1.username + '.png'}"/>
                            </a>
                        </div>
                        <div class="col-md-6">
                            <a href="${'/player/' + team.player2.username}">
                                <img style="border-radius: 50%; width: 44px; height: 44px;"
                                     src="${'/userpics/' + team.player2.username + '.png'}"/>
                            </a>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6" style="font-size: 9pt; text-align: center; padding-top: 2pt;">${team.player1.username}</div>
                        <div class="col-md-6" style="font-size: 9pt; text-align: center; padding-top: 2pt;">${team.player2.username}</div>
                    </div>
                </td>
                <td>${team.goalsDelta}</td>
                <td>${team.matchesPlayed}</td>
                <td style="text-align: right;">${team.points}</td>
            </tr>
        </#list>
        </table>
    </div>
    <div class="col-md-6">
        <table class="rwd-table" style="font-size:16pt;">
            <tr>
                <th>#</th>
                <th style="text-align: center;">Team</th>
                <th>+/-</th>
                <th>G</th>
                <th>Pts</th>
            </tr>

        <#list tournament.groups[1].teams as team>
            <tr>
                <td>${team?counter}</td>
                <td>
                    <div class="row" style="text-align: center; width: 280px;">
                        <div class="col-md-6">
                            <a href="${'/player/' + team.player1.username}">
                                <img style="border-radius: 50%; width: 44px; height: 44px;"
                                     src="${'/userpics/' + team.player1.username + '.png'}"/>
                            </a>
                        </div>
                        <div class="col-md-6">
                            <a href="${'/player/' + team.player2.username}">
                                <img style="border-radius: 50%; width: 44px; height: 44px;"
                                     src="${'/userpics/' + team.player2.username + '.png'}"/>
                            </a>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6" style="font-size: 9pt; text-align: center; padding-top: 2pt;">${team.player1.username}</div>
                        <div class="col-md-6" style="font-size: 9pt; text-align: center; padding-top: 2pt;">${team.player2.username}</div>
                    </div>
                </td>
                <td>${team.goalsDelta}</td>
                <td>${team.matchesPlayed}</td>
                <td style="text-align: right;">${team.points}</td>
            </tr>
        </#list>
        </table>
    </div>
</div>
<div class="row">
    <div class="col-md-6">
        <table class="rwd-table" style="font-size: 12pt;">
            <tr>
                <th>
                    <div style="width: 12px; height: 12px; border-radius: 50%; background-color: red; margin: auto;"/>
                </th>
                <th></th>
                <th>
                    <div style="width: 12px; height: 12px; border-radius: 50%; background-color: yellow; margin: auto;"/>
                </th>
            </tr>
        <#list tournament.groups[0].groupMatches as groupMatch>
            <tr>
                <td style="text-align: center;">
                    <a href="${'/player/' + groupMatch.redTeam.player1.username}">
                        <img style="border-radius: 50%; width: 36px; height: 36px;"
                             src="${'/userpics/' + groupMatch.redTeam.player1.username + '.png'}"/>
                    </a>
                    <a href="${'/player/' + groupMatch.redTeam.player2.username}">
                        <img style="border-radius: 50%; width: 36px; height: 36px;"
                             src="${'/userpics/' + groupMatch.redTeam.player2.username + '.png'}"/>
                    </a>
                </td>
                <td style="text-align: center;">
                ${groupMatch.played?then(groupMatch.match.redTeamGoals + ' : ' + groupMatch.match.yellowTeamGoals, '-:-')}
                </td>
                <td style="text-align: center;">
                    <a href="${'/player/' + groupMatch.yellowTeam.player1.username}">
                        <img style="border-radius: 50%; width: 36px; height: 36px;"
                             src="${'/userpics/' + groupMatch.yellowTeam.player1.username + '.png'}"/>
                    </a>
                    <a href="${'/player/' + groupMatch.yellowTeam.player2.username}">
                        <img style="border-radius: 50%; width: 36px; height: 36px;"
                             src="${'/userpics/' + groupMatch.yellowTeam.player2.username + '.png'}"/>
                    </a>
                </td>
            </tr>
        </#list>
        </table>
    </div>
    <div class="col-md-6">
        <table class="rwd-table" style="font-size: 12pt;">
            <tr>
                <th>
                    <div style="width: 12px; height: 12px; border-radius: 50%; background-color: red; margin: auto;"/>
                </th>
                <th></th>
                <th>
                    <div style="width: 12px; height: 12px; border-radius: 50%; background-color: yellow; margin: auto;"/>
                </th>
            </tr>
        <#list tournament.groups[1].groupMatches as groupMatch>
            <tr>
                <td style="text-align: center;">
                    <a href="${'/player/' + groupMatch.redTeam.player1.username}">
                        <img style="border-radius: 50%; width: 36px; height: 36px;"
                             src="${'/userpics/' + groupMatch.redTeam.player1.username + '.png'}"/>
                    </a>
                    <a href="${'/player/' + groupMatch.redTeam.player2.username}">
                        <img style="border-radius: 50%; width: 36px; height: 36px;"
                             src="${'/userpics/' + groupMatch.redTeam.player2.username + '.png'}"/>
                    </a>
                </td>
                <td style="text-align: center;">
                ${groupMatch.played?then(groupMatch.match.redTeamGoals + ' : ' + groupMatch.match.yellowTeamGoals, '-:-')}
                </td>
                <td style="text-align: center;">
                    <a href="${'/player/' + groupMatch.yellowTeam.player1.username}">
                        <img style="border-radius: 50%; width: 36px; height: 36px;"
                             src="${'/userpics/' + groupMatch.yellowTeam.player1.username + '.png'}"/>
                    </a>
                    <a href="${'/player/' + groupMatch.yellowTeam.player2.username}">
                        <img style="border-radius: 50%; width: 36px; height: 36px;"
                             src="${'/userpics/' + groupMatch.yellowTeam.player2.username + '.png'}"/>
                    </a>
                </td>
            </tr>
        </#list>
        </table>
    </div>
</div>
</body>
</html>