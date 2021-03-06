<!DOCTYPE HTML>
<head>
    <title>ZenSoft kicker rating</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<#include "include/imports.ftl">
</head>
<body>


<!--<div class="navigation">-->
<!--<a href="/tournament/groups">Tournament</a>-->
<!--</div>-->

<#include "include/header.ftl">

<table class="rwd-table" style="margin-top: 6em;">
    <tr>
        <th>#</th>
        <th colspan="2" style="text-align: center">Player</th>
        <th style="text-align: center">Games</th>
        <th style="text-align: center">Rated</th>
        <th style="text-align: center">Rating</th>
    </tr>
<#list ratings as playerStats>
    <tr>
        <td>${playerStats?counter}</td>
        <td>
            <a href="${'/player/' + playerStats.player.username}">
                <img style="border-radius: 50%; width: 28px; height: 28px;"
                     src="${'/userpics/' + playerStats.player.username + '.png'}"/>
            </a>
        </td>
        <td><a href="${'/player/' + playerStats.player.username}">${playerStats.player.fullName}</a></td>
        <td>${playerStats.baseStats.matchesPlayed}</td>
        <td>${playerStats.baseStats.matchesRated}</td>
        <td>${playerStats.baseStats.rating?round?c}</td>
    </tr>
</#list>
<#list inactiveRatings as playerStats>
    <#if playerStats?is_first>
        <tr style="border-top: 1px solid #eee;">
    <#else>
        <tr>
    </#if>
        <td></td>
        <td>
            <a href="${'/player/' + playerStats.player.username}">
                <img style="border-radius: 50%; width: 28px; height: 28px;"
                     src="${'/userpics/' + playerStats.player.username + '.png'}"/>
            </a>
        </td>
        <td><a href="${'/player/' + playerStats.player.username}">${playerStats.player.fullName}</a></td>
        <td>${playerStats.baseStats.matchesPlayed}</td>
        <td>${playerStats.baseStats.matchesRated}</td>
        <td></td>
    </tr>
</#list>
</table>

</body>
</html>