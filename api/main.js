import express from 'express';
import bodyParser from 'body-parser';
import mysql from 'mysql';
import e from 'express';

const app = express()
const port = 3002

const dbConfig = {
    host: 'eliascastel.ddns.net',
    user: 'java',
    password: '!pn!XrZLgt-pn2RP',
    database: '2java',
};

function verifyTokenAndPerms(token) {
    return new Promise((resolve, reject) => {
        const connection = mysql.createConnection(dbConfig);
        connection.query('SELECT * FROM tokens WHERE token = ?', [token], function (error, results, fields) {
            if (error) {
                reject('Internal server error');
            } else {
                if (results.length === 0) {
                    reject('Invalid token');
                } else {
                    resolve(results[0].user_id);
                }
            }
            connection.end();
        });
    });
};

app.use(bodyParser.json());

// Middleware pour gérer les CORS
app.use(function (req, res, next) {
    res.setHeader('Access-Control-Allow-Origin', '*');
    res.setHeader('Access-Control-Allow-Methods', 'GET, POST');
    res.setHeader('Access-Control-Allow-Headers', 'Content-Type');
    next();
});

app.get('/', (req, res) => {
    res.send('API 2JAVA IStore')
})

// Route pour l'enregistrement utilisateur
app.post('/register', (req, res) => {
    const { email, pseudo, password } = req.body;
    console.log("email : " + email + " pseudo : " + pseudo + " password : " + password)
    
    const connection = mysql.createConnection(dbConfig);

    connection.query('SELECT * FROM whitelist WHERE email = ?', [email], function (error, resultsWhitelist, fields) {
        if (error) {
            console.log(error);
            res.status(500).json({ message: 'Internal server error!' });
        } else {
            if (resultsWhitelist.length === 0) {
                res.status(401).json({ message: 'You are not allowed to register' });
            } else {
                connection.query('SELECT * FROM users WHERE email = ?', [email], function (error, results, fields) {
                    if (error) {
                        res.status(500).json({ message: 'Internal server error' });
                    } else {
                        if (results.length > 0) {
                            res.status(401).json({ message: 'Email already used' });
                        }
                        else {
                            connection.query('INSERT INTO users (email, pseudo, password, role, store) VALUES (?, ?, ?, ?, ?)', [email, pseudo, password, resultsWhitelist[0].role, resultsWhitelist[0].store], function (error, results, fields) {
                                if (error) {
                                    res.status(500).json({ message: 'Internal server error' });
                                } else {
                                    res.status(200).json({ message: 'User registered' });
                                }
                                connection.end();
                            });
                        }
                    }
                });
            }
        }
    });

});

// Route pour la connexion utilisateur
app.get('/login', (req, res) => {
    const { pseudo, password } = req.body;
    console.log("pseudo : " + pseudo + " password : " + password);
    res.status(200).json({ message: 'User connected' });
    const connection = mysql.createConnection(dbConfig);

    connection.query('SELECT * FROM users WHERE pseudo = ? AND password = ?', [pseudo, password], function (error, resultsUser, fields) {
        if (error) {
            res.status(500).json({ message: 'Internal server error' });
        } else {
            if (resultsUser.length === 0) {
                res.status(401).json({ message: 'Invalid pseudo or password' });
            } else {
                // Générer un token
                const token = Math.random().toString(36).substring(7);
                connection.query('SELECT * FROM tokens WHERE token = ?', [token], function (error, results, fields) {
                    if (error) {
                        res.status(500).json({ message: 'Internal server error' });
                    } else {
                        if (results.length === 0) {
                            connection.query('INSERT INTO tokens (id, token, role, store) VALUES (?, ?, ?, ?)', [resultsUser[0].id, token, resultsUser[0].role, resultsUser[0].store], function (error, results, fields) {
                                if (error) {
                                    res.status(500).json({ message: 'Internal server error' });
                                } else {
                                    res.status(200).json({ token });
                                }
                            });
                        } else {
                            res.status(500).json({ message: 'Internal server error' });
                        }
                    }
                });
                res.status(200).json(results[0]);
            }
        }
        connection.end();
    });
});

app.listen(port, () => {
    console.log(`The app are started on port ${port}`)
})