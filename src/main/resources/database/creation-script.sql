CREATE TABLE award
(
    id INTEGER DEFAULT nextval('award_id_seq'::regclass) PRIMARY KEY NOT NULL,
    type VARCHAR(255) NOT NULL,
    level VARCHAR(255),
    name VARCHAR(255) NOT NULL,
    player_id BIGINT NOT NULL,
    CONSTRAINT fk_award_player FOREIGN KEY (player_id) REFERENCES player (id)
);
CREATE TABLE box
(
    id INTEGER DEFAULT nextval('box_id_seq'::regclass) PRIMARY KEY NOT NULL,
    name VARCHAR(255) NOT NULL,
    tournament_id BIGINT NOT NULL,
    CONSTRAINT fk_box_tournament FOREIGN KEY (tournament_id) REFERENCES tournament (id)
);
CREATE INDEX fk_box_tournament ON box (tournament_id);
CREATE TABLE box_game
(
    id INTEGER DEFAULT nextval('box_game_id_seq'::regclass) PRIMARY KEY NOT NULL,
    game_id BIGINT,
    box_id BIGINT NOT NULL,
    red_team_id BIGINT NOT NULL,
    yellow_team_id BIGINT NOT NULL,
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
    id INTEGER DEFAULT nextval('game_id_seq'::regclass) PRIMARY KEY NOT NULL,
    red_team_p1_id BIGINT NOT NULL,
    red_team_p2_id BIGINT NOT NULL,
    yellow_team_p1_id BIGINT NOT NULL,
    yellow_team_p2_id BIGINT NOT NULL,
    red_team_goals INTEGER NOT NULL,
    yellow_team_goals INTEGER NOT NULL,
    played_on TIMESTAMP DEFAULT now() NOT NULL,
    reported_by VARCHAR(255),
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
    id INTEGER DEFAULT nextval('player_id_seq'::regclass) PRIMARY KEY NOT NULL,
    username VARCHAR(255) NOT NULL,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    slack_id VARCHAR(255),
    im_channel VARCHAR(255)
);
CREATE UNIQUE INDEX player_username_uindex ON player (username);
CREATE UNIQUE INDEX player_slack_id_uindex ON player (slack_id);
CREATE TABLE playoff_game
(
    id INTEGER DEFAULT nextval('playoff_game_id_seq'::regclass) PRIMARY KEY NOT NULL,
    red_team_id BIGINT NOT NULL,
    yellow_team_id BIGINT NOT NULL,
    game_id BIGINT,
    playoff_serie_id BIGINT NOT NULL,
    CONSTRAINT fk_playoff_game_red_team FOREIGN KEY (red_team_id) REFERENCES team (id),
    CONSTRAINT fk_playoff_game_yellow_team FOREIGN KEY (yellow_team_id) REFERENCES team (id),
    CONSTRAINT fk_playoff_game_game FOREIGN KEY (game_id) REFERENCES game (id),
    CONSTRAINT fk_playoff_game_playoff_serie FOREIGN KEY (playoff_serie_id) REFERENCES playoff_serie (id)
);
CREATE TABLE playoff_serie
(
    id INTEGER DEFAULT nextval('playoff_serie_id_seq'::regclass) PRIMARY KEY NOT NULL,
    first_team_id BIGINT,
    second_team_id BIGINT,
    stage INTEGER NOT NULL,
    subsequent_winner_serie_id BIGINT,
    subsequent_loser_serie_id BIGINT,
    best_of INTEGER NOT NULL,
    tournament_id BIGINT NOT NULL,
    CONSTRAINT fk_playoff_serie_first_team FOREIGN KEY (first_team_id) REFERENCES team (id),
    CONSTRAINT fk_playoff_serie_second_team FOREIGN KEY (second_team_id) REFERENCES team (id),
    CONSTRAINT fk_playoff_serie_self_winner FOREIGN KEY (subsequent_winner_serie_id) REFERENCES playoff_serie (id),
    CONSTRAINT fk_playoff_serie_self_loser FOREIGN KEY (subsequent_loser_serie_id) REFERENCES playoff_serie (id),
    CONSTRAINT fk_playoff_serie_tournament FOREIGN KEY (tournament_id) REFERENCES tournament (id)
);
CREATE TABLE team
(
    id INTEGER DEFAULT nextval('team_id_seq'::regclass) PRIMARY KEY NOT NULL,
    p1_id BIGINT NOT NULL,
    p2_id BIGINT NOT NULL,
    name VARCHAR(255),
    tournament_id BIGINT NOT NULL,
    box_id BIGINT,
    CONSTRAINT fk_team_p1 FOREIGN KEY (p1_id) REFERENCES player (id),
    CONSTRAINT fk_team_p2 FOREIGN KEY (p2_id) REFERENCES player (id),
    CONSTRAINT fk_team_tournament FOREIGN KEY (tournament_id) REFERENCES tournament (id),
    CONSTRAINT fk_team_box FOREIGN KEY (box_id) REFERENCES box (id)
);
CREATE INDEX fk_team_p1 ON team (p1_id);
CREATE INDEX fk_team_p2 ON team (p2_id);
CREATE INDEX fk_team_tournament ON team (tournament_id);
CREATE INDEX fk_team_box ON team (box_id);
CREATE TABLE tournament
(
    id INTEGER DEFAULT nextval('tournament_id_seq'::regclass) PRIMARY KEY NOT NULL,
    name VARCHAR(255) NOT NULL,
    active BOOLEAN DEFAULT false
);