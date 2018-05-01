module.exports = (connection) => {
    return {
        setUserSession: (name, sessionToken, email) => {
            let promise = new Promise((resolve, reject) => {
                name = mysql_real_escape_string(name);
                var columnNames = 'name, session, email';
                var columnValues = `'${name}', '${sessionToken}', '${email}'`;
                var checkExistsQueryString = `SELECT id FROM users WHERE email = '${email}'`;
                var updateSessionQueryString = `UPDATE users SET session = '${sessionToken}' WHERE email = '${email}'`;
                var createUserQueryString = `INSERT INTO users (${columnNames}) VALUES (${columnValues})`;


                connection.query(checkExistsQueryString, (err, rows) => {

                    if(err) {

                        console.log("Error in initial check of user's existence");
                        return reject();

                    } else if(rows.length === 0) { //new user

                        connection.query(createUserQueryString, (err, rows) => {
                            if (!err) {
                                //user created successfully, now return the userId for this user:
                                connection.query(checkExistsQueryString, (err, rows) => {

                                    if(!err) {
                                        return resolve(rows[0].id);
                                    } else {
                                        console.log("Error in fetching created user's id")
                                        return reject();
                                    }

                                });
                            } else {
                                console.log("Error in creating the user!");
                                return reject();
                            }
                        });

                    } else { //existing user
                        return resolve(rows[0].id);
                    }

                });
            });

            return promise;
        }

    }
}

function mysql_real_escape_string (str) {
    return str.replace(/[\0\x08\x09\x1a\n\r"'\\\%]/g, function (char) {
        switch (char) {
            case "\0":
                return "\\0";
            case "\x08":
                return "\\b";
            case "\x09":
                return "\\t";
            case "\x1a":
                return "\\z";
            case "\n":
                return "\\n";
            case "\r":
                return "\\r";
            case "\"":
            case "'":
            case "\\":
            case "%":
                return "\\"+char; // prepends a backslash to backslash, percent,
                                  // and double/single quotes
        }
    });
}
