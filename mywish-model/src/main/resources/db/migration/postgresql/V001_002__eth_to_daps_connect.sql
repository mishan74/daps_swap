CREATE TABLE eth_to_daps_connect (
  id SERIAL PRIMARY KEY ,
  eth_address VARCHAR(50) NOT NULL ,
  daps_address VARCHAR(120) NOT NULL ,
  UNIQUE (eth_address, daps_address)
);
