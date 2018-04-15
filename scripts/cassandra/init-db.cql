CREATE KEYSPACE wikipedia
  WITH REPLICATION = {
   'class' : 'SimpleStrategy',
   'replication_factor' : 2
  };

CREATE TABLE wikipedia.changes (
    id text PRIMARY KEY,
    timestamp Timestamp,
    user text,
    server_name text,
    bot text,
    title text);
