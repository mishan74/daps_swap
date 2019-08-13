CREATE TABLE eth_daps_swap_swap_entry (
  id SERIAL PRIMARY KEY ,
  amount BIGINT ,
  eth_tx_hash VARCHAR(66) ,
  daps_tx_hash VARCHAR(66) ,
  transfer_status VARCHAR(20),
  link_entry_id SERIAL REFERENCES eth_daps_swap_connect_entry (id)
);