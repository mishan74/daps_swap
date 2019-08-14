CREATE TABLE eth_to_daps_transition (
  id SERIAL PRIMARY KEY ,
  amount BIGINT ,
  eth_tx_hash VARCHAR(66) ,
  daps_tx_hash VARCHAR(66) ,
  transfer_status VARCHAR(20),
  connect_entry_id SERIAL REFERENCES eth_to_daps_connect (id)
);