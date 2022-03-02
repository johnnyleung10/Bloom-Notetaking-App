CREATE TABLE IF NOT EXISTS note_table (
  id BIGINT NOT NULL,
   title VARCHAR(255) NULL,
   content VARCHAR(255) NULL,
   date_created VARCHAR(255) NULL,
   date_modified VARCHAR(255) NULL,
   date_deleted VARCHAR(255) NULL,
   CONSTRAINT pk_note_table PRIMARY KEY (id)
);