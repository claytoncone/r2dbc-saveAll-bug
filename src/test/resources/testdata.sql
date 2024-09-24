

INSERT INTO client (given_name, surname) VALUES ('John', 'Doe');

INSERT INTO contact (CLIENT_ID, EMAIL, PHONE, MOBILE, FAX) VALUES
( (SELECT ID FROM client  WHERE GIVEN_NAME = 'John' AND SURNAME = 'Doe'),
                'data@datar.us', '3037987766', '3037987766', '7207986677' );

INSERT INTO client (given_name, middle_initial, surname, title) VALUES (
                  'Richard', 'M','Nixon', 'Not A Crook');

INSERT INTO contact (CLIENT_ID, EMAIL, PHONE, MOBILE, FAX) VALUES
    ( (SELECT ID FROM client  WHERE GIVEN_NAME = 'Richard' AND SURNAME = 'Nixon'),
      'tricky@dick.us', '5057987766', '5057987766', '5057986677' );

INSERT INTO client (given_name, middle_initial, surname, company) VALUES
                ('Gerald', 'R', 'Ford', 'Vale Resort');

INSERT INTO contact (CLIENT_ID, EMAIL, PHONE, MOBILE, FAX) VALUES
    ( (SELECT ID FROM client  WHERE GIVEN_NAME = 'Gerald' AND SURNAME = 'Ford'),
      'ford@fpotus.us', '5057987766', '5057987766', '5057986677' );
