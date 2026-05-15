INSERT INTO team (city, name) SELECT 'Chicago', 'Bulls' WHERE NOT EXISTS (SELECT 1 FROM team WHERE name = 'Bulls');
INSERT INTO team (city, name) SELECT 'Los Angeles', 'Lakers' WHERE NOT EXISTS (SELECT 1 FROM team WHERE name = 'Lakers');
INSERT INTO team (city, name) SELECT 'Golden State', 'Warriors' WHERE NOT EXISTS (SELECT 1 FROM team WHERE name = 'Warriors');

INSERT INTO player (name, position) SELECT 'Michael Jordan', 'SG' WHERE NOT EXISTS (SELECT 1 FROM player WHERE name = 'Michael Jordan');
INSERT INTO player (name, position) SELECT 'LeBron James', 'SF' WHERE NOT EXISTS (SELECT 1 FROM player WHERE name = 'LeBron James');
INSERT INTO player (name, position) SELECT 'Stephen Curry', 'PG' WHERE NOT EXISTS (SELECT 1 FROM player WHERE name = 'Stephen Curry');

INSERT INTO contract (salary, player_id, team_id) 
SELECT 33000000, 
       (SELECT id FROM player WHERE name = 'Michael Jordan'), 
       (SELECT id FROM team WHERE name = 'Bulls') 
WHERE NOT EXISTS (
    SELECT 1 FROM contract 
    WHERE player_id = (SELECT id FROM player WHERE name = 'Michael Jordan') 
      AND team_id = (SELECT id FROM team WHERE name = 'Bulls')
);

INSERT INTO contract (salary, player_id, team_id) 
SELECT 40000000, 
       (SELECT id FROM player WHERE name = 'LeBron James'), 
       (SELECT id FROM team WHERE name = 'Lakers') 
WHERE NOT EXISTS (
    SELECT 1 FROM contract 
    WHERE player_id = (SELECT id FROM player WHERE name = 'LeBron James') 
      AND team_id = (SELECT id FROM team WHERE name = 'Lakers')
);

INSERT INTO contract (salary, player_id, team_id) 
SELECT 45000000, 
       (SELECT id FROM player WHERE name = 'Stephen Curry'), 
       (SELECT id FROM team WHERE name = 'Warriors') 
WHERE NOT EXISTS (
    SELECT 1 FROM contract 
    WHERE player_id = (SELECT id FROM player WHERE name = 'Stephen Curry') 
      AND team_id = (SELECT id FROM team WHERE name = 'Warriors')
);

