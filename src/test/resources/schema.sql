
CREATE TABLE IF NOT EXISTS client (
                        id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                        given_name VARCHAR(255) NOT NULL,
                        middle_initial VARCHAR(1),
                        surname VARCHAR(255) NOT NULL,
                        title VARCHAR(255),
                        company VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS contact (
                         client_id BIGINT NOT NULL PRIMARY KEY,
                         email VARCHAR(255) UNIQUE NOT NULL,
                         phone VARCHAR(10),
                         mobile VARCHAR(10),
                         fax VARCHAR(10),
                         FOREIGN KEY (client_id) REFERENCES client (id)
);