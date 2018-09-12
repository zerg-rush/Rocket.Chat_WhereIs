create table USERS
(
  USERNAME           VARCHAR(255) not null primary key,
  FULLNAME           VARCHAR(255) not null,
  DESCRIPTION        VARCHAR(255) not null,
  AVATAR_URL         VARCHAR(255)
);

create table LOCATION
(
  INDEX              VARCHAR(255) not null primary key,
  TYPE               VARCHAR(4) not null,
  NAME               VARCHAR(255) not null,
  DEVICE             VARCHAR(255) not null,
  SN                 VARCHAR(255) not null,
  DESCRIPTION        VARCHAR(255) not null,
  MAP_URL            VARCHAR(255)
);

create table REVOKED_TOKEN
(
  ID                 UUID not null primary key,
  USERNAME           VARCHAR(255) not null,
  EXPIRATION_DATE    TIMESTAMP not null
);
