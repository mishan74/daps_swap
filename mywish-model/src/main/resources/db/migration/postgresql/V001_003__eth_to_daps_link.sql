CREATE TABLE eth_daps_swap_connect_entry (
  id SERIAL PRIMARY KEY ,
  symbol VARCHAR(10) NOT NULL,
  eth_address VARCHAR(50) NOT NULL ,
  daps_address VARCHAR(50) NOT NULL,
  UNIQUE (symbol, eth_address)
);
