import express from 'express';
import mysql from 'mysql';
const app = express()
const port = 3001

// const dbConfig = {
//     host: 'eliascastel.ddns.net',
//     user: 'proj',
//     password: 'ne76uWF#8#',
//     database: 'proj',
// };

app.use(function (req, res, next) {
    res.setHeader('Access-Control-Allow-Origin', '*');
    if (req.method === 'OPTIONS') {
        res.setHeader('Access-Control-Allow-Methods', 'GET');
        res.setHeader('Access-Control-Allow-Headers', 'Content-Type');
        return res.status(200).json({});
    }
    next();
});

app.get('/', (req, res) => {
    res.send('API 2JAVA IStore')
})

app.get('/login', (req, res) => {
    const {username, password} = req.body;
    const connection = mysql.createConnection(dbConfig);

    connection.connect();
    //connection.query(`SELECT * FROM users WHERE username = '${username}' AND password = '${password}'`, function (error, results, fields) {
        if (error) throw error;
        if (results.length === 0) {
            res.status(401).json({message: 'Invalid username or password'});
        } else {
            res.status(200).json(results[0]);
        }
    });
    connection.end();
});

app.post('/register', (req, res) => {
    const {username, password} = req.body;
    const connection = mysql.createConnection(dbConfig);

    connection.connect();
    //connection.query(`INSERT INTO users (username, password) VALUES ('${username}', '${password}')`, function (error, results, fields) {
        if (error) throw error;
        res.status(200).json({message: 'User created successfully'});
    });
    connection.end();
});

app.listen(port, () => {
    console.log(`The app are started on port ${port}`)
})