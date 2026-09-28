CREATE TABLE diagnosis_run (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  case_id VARCHAR(80) NOT NULL,
  circuit_id VARCHAR(120) NOT NULL,
  status VARCHAR(24) NOT NULL,
  created_by VARCHAR(120) NOT NULL,
  created_at DATETIME(3) NOT NULL,
  updated_at DATETIME(3) NOT NULL,
  INDEX idx_run_circuit_created (circuit_id, created_at)
);

CREATE TABLE diagnosis_step (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  diagnosis_id BIGINT NOT NULL,
  case_id VARCHAR(80) NOT NULL,
  circuit_id VARCHAR(120) NOT NULL,
  branch VARCHAR(16) NOT NULL,
  system_name VARCHAR(40) NOT NULL,
  step_name VARCHAR(80) NOT NULL,
  status VARCHAR(24) NOT NULL,
  started_at DATETIME(3) NULL,
  finished_at DATETIME(3) NULL,
  raw_output MEDIUMTEXT NULL,
  error_message TEXT NULL,
  CONSTRAINT fk_step_run FOREIGN KEY (diagnosis_id) REFERENCES diagnosis_run(id),
  INDEX idx_step_run (diagnosis_id, id),
  INDEX idx_step_circuit (circuit_id)
);
