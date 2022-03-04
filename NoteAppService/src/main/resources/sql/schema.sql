CREATE TABLE IF NOT EXISTS folder_table (
  id BIGINT NOT NULL PRIMARY KEY,
   title VARCHAR(255) NULL,
   date_created VARCHAR(255) NULL,
   date_modified VARCHAR(255) NULL,
   date_deleted VARCHAR(255) NULL
);

CREATE TABLE IF NOT EXISTS note_table (
  id BIGINT NOT NULL PRIMARY KEY,
   title VARCHAR(255) NULL,
   content_rich VARCHAR(255) NULL,
   content_plain VARCHAR(255) NULL,
   date_created VARCHAR(255) NULL,
   date_modified VARCHAR(255) NULL,
   date_deleted VARCHAR(255) NULL,
   folder_id BIGINT NOT NULL,
   FOREIGN KEY (folder_id) REFERENCES folder_table(id)
);