CREATE TABLE eth_to_daps_connect (
  id SERIAL PRIMARY KEY ,
  eth_address VARCHAR(50) NOT NULL ,
  daps_address VARCHAR(100) NOT NULL ,
  connect_tx_hash VARCHAR(66) UNIQUE NOT NULL
);
