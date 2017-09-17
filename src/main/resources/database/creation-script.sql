create table box
(
    id bigint not null auto_increment
        primary key,
    name varchar(255) not null,
    tournament_id bigint not null
)
;

create index fk_box_tournament
    on box (tournament_id)
;

create table box_game
(
    id bigint not null auto_increment
        primary key,
    game_id bigint null,
    box_id bigint not null,
    red_team_id bigint not null,
    yellow_team_id bigint not null,
    constraint box_game_game_id_uindex
    unique (game_id),
    constraint fk_box_game_box
    foreign key (box_id) references elo.box (id)
)
;

create index fk_box_game_box
    on box_game (box_id)
;

create index fk_box_game_red_team
    on box_game (red_team_id)
;

create index fk_box_game_yellow_team
    on box_game (yellow_team_id)
;

create table game
(
    id bigint not null auto_increment
        primary key,
    red_team_p1_id bigint not null,
    red_team_p2_id bigint not null,
    yellow_team_p1_id bigint not null,
    yellow_team_p2_id bigint not null,
    red_team_goals int not null,
    yellow_team_goals int not null,
    played_on datetime default CURRENT_TIMESTAMP not null
)
;

create index fk_red_p1
    on game (red_team_p1_id)
;

create index fk_red_p2
    on game (red_team_p2_id)
;

create index fk_yellow_p1
    on game (yellow_team_p1_id)
;

create index fk_yellow_p2
    on game (yellow_team_p2_id)
;

alter table box_game
    add constraint fk_box_game_game
foreign key (game_id) references elo.game (id)
;

create table player
(
    username varchar(255) not null,
    first_name varchar(255) null,
    last_name varchar(255) null,
    id bigint not null auto_increment
        primary key,
    im_channel varchar(255) null,
    constraint player_username_uindex
    unique (username)
)
;

alter table game
    add constraint fk_red_p1
foreign key (red_team_p1_id) references elo.player (id)
;

alter table game
    add constraint fk_red_p2
foreign key (red_team_p2_id) references elo.player (id)
;

alter table game
    add constraint fk_yellow_p1
foreign key (yellow_team_p1_id) references elo.player (id)
;

alter table game
    add constraint fk_yellow_p2
foreign key (yellow_team_p2_id) references elo.player (id)
;

create table team
(
    id bigint not null auto_increment
        primary key,
    p1_id bigint not null,
    p2_id bigint not null,
    name varchar(255) null,
    tournament_id bigint not null,
    box_id bigint null,
    constraint fk_team_p1
    foreign key (p1_id) references elo.player (id),
    constraint fk_team_p2
    foreign key (p2_id) references elo.player (id),
    constraint fk_team_box
    foreign key (box_id) references elo.box (id)
)
;

create index fk_team_box
    on team (box_id)
;

create index fk_team_p1
    on team (p1_id)
;

create index fk_team_p2
    on team (p2_id)
;

create index fk_team_tournament
    on team (tournament_id)
;

alter table box_game
    add constraint fk_box_game_red_team
foreign key (red_team_id) references elo.team (id)
;

alter table box_game
    add constraint fk_box_game_yellow_team
foreign key (yellow_team_id) references elo.team (id)
;

create table tournament
(
    id bigint not null auto_increment
        primary key,
    name varchar(255) not null,
    active bit default b'0' null
)
;

alter table box
    add constraint fk_box_tournament
foreign key (tournament_id) references elo.tournament (id)
;

alter table team
    add constraint fk_team_tournament
foreign key (tournament_id) references elo.tournament (id)
;

