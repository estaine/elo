CREATE TABLE box
(
    id BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    tournament_id BIGINT(20) NOT NULL,
    CONSTRAINT fk_box_tournament FOREIGN KEY (tournament_id) REFERENCES tournament (id)
);
CREATE INDEX fk_box_tournament ON box (tournament_id);
CREATE TABLE box_game
(
    id BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    game_id BIGINT(20),
    box_id BIGINT(20) NOT NULL,
    red_team_id BIGINT(20) NOT NULL,
    yellow_team_id BIGINT(20) NOT NULL,
    CONSTRAINT fk_box_game_game FOREIGN KEY (game_id) REFERENCES game (id),
    CONSTRAINT fk_box_game_box FOREIGN KEY (box_id) REFERENCES box (id),
    CONSTRAINT fk_box_game_red_team FOREIGN KEY (red_team_id) REFERENCES team (id),
    CONSTRAINT fk_box_game_yellow_team FOREIGN KEY (yellow_team_id) REFERENCES team (id)
);
CREATE UNIQUE INDEX box_game_game_id_uindex ON box_game (game_id);
CREATE INDEX fk_box_game_box ON box_game (box_id);
CREATE INDEX fk_box_game_red_team ON box_game (red_team_id);
CREATE INDEX fk_box_game_yellow_team ON box_game (yellow_team_id);
CREATE TABLE game
(
    id BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    red_team_p1_id BIGINT(20) NOT NULL,
    red_team_p2_id BIGINT(20) NOT NULL,
    yellow_team_p1_id BIGINT(20) NOT NULL,
    yellow_team_p2_id BIGINT(20) NOT NULL,
    red_team_goals INT(11) NOT NULL,
    yellow_team_goals INT(11) NOT NULL,
    played_on DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    tournament_game BIT(1) DEFAULT b'0' NOT NULL,
    CONSTRAINT fk_red_p1 FOREIGN KEY (red_team_p1_id) REFERENCES player (id),
    CONSTRAINT fk_red_p2 FOREIGN KEY (red_team_p2_id) REFERENCES player (id),
    CONSTRAINT fk_yellow_p1 FOREIGN KEY (yellow_team_p1_id) REFERENCES player (id),
    CONSTRAINT fk_yellow_p2 FOREIGN KEY (yellow_team_p2_id) REFERENCES player (id)
);
CREATE INDEX fk_red_p1 ON game (red_team_p1_id);
CREATE INDEX fk_red_p2 ON game (red_team_p2_id);
CREATE INDEX fk_yellow_p1 ON game (yellow_team_p1_id);
CREATE INDEX fk_yellow_p2 ON game (yellow_team_p2_id);
CREATE TABLE player
(
    username VARCHAR(255) NOT NULL,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    id BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT
);
CREATE UNIQUE INDEX player_username_uindex ON player (username);
CREATE TABLE team
(
    id BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    p1_id BIGINT(20) NOT NULL,
    p2_id BIGINT(20) NOT NULL,
    name VARCHAR(255),
    tournament_id BIGINT(20) NOT NULL,
    box_id BIGINT(20),
    CONSTRAINT fk_team_p1 FOREIGN KEY (p1_id) REFERENCES player (id),
    CONSTRAINT fk_team_p2 FOREIGN KEY (p2_id) REFERENCES player (id),
    CONSTRAINT fk_team_tournament FOREIGN KEY (tournament_id) REFERENCES tournament (id),
    CONSTRAINT fk_team_box FOREIGN KEY (box_id) REFERENCES box (id)
);
CREATE INDEX fk_team_box ON team (box_id);
CREATE INDEX fk_team_p1 ON team (p1_id);
CREATE INDEX fk_team_p2 ON team (p2_id);
CREATE INDEX fk_team_tournament ON team (tournament_id);
CREATE TABLE tournament
(
    id BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL
);