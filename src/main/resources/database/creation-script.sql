CREATE TABLE game
(
    id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
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