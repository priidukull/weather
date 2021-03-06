CREATE SCHEMA weather;

CREATE TABLE weather.city
(
  id BIGSERIAL NOT NULL PRIMARY KEY,
  zip CHAR(5) UNIQUE NOT NULL,
  name TEXT NOT NULL,
  state TEXT NOT NULL
);

CREATE TABLE weather.data
(
  cityId BIGINT NOT NULL PRIMARY KEY,
  imperialData TEXT NOT NULL,
  metricsData TEXT NOT NULL,
  FOREIGN KEY (cityId) REFERENCES weather.city (id) ON DELETE CASCADE
);