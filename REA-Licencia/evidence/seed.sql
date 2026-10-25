CREATE TABLE IF NOT EXISTS user(
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  name text,
  lastName1 text,
  lastName2 text,
  curp text,
  phone text,
  email text,
  password text,
  pin text
);

CREATE TABLE IF NOT EXISTS licenses(
  id INTEGER PRIMARY KEY AUTOINCREMENT, 
  transporte INTEGER,
  nombre text,
  appellido_paterno text,
  appellido_materno text,
  rfc text,
  nacionalidad text,
  num_licencia text,
  inicio_vigencia text,
  fin_vigencia text,
  num_expediente_med text,
  expedicion text,
  antiguedad text,
  lugar_tramite text,
  capacidades text,
  empresa text,
  text_validez text,
  validez text,
  tipo_licencia text,
  tipo_licencia_articulo text,
  nombre_titular text,
  nombre_director text,
  cargo_director text,
  user_id integer,
  curp text,
  leyenda text,
  leyenda2 text,
  FOREIGN KEY(user_id) REFERENCES user(id)
);

CREATE TABLE IF NOT EXISTS remarks(
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  name text,
  value text,
  license_id integer,
  FOREIGN KEY(license_id) REFERENCES licences(id)
);

CREATE TABLE IF NOT EXISTS media(
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  name text,
  mime text,
  value text,
  license_id integer,
  FOREIGN KEY(license_id) REFERENCES licences(id)
);
/*INSERT or IGNORE INTO screen(id, flag) VALUES (1, 0),*/
/*DELETE FROM user,*/
