import express from 'express';
import bodyParser from 'body-parser';

const app = express();
const PORT = 3003;

app.use(bodyParser.json());

// Route pour récupérer les informations du body
app.post('/login', (req, res) => {
    const { email, password } = req.body;

    // Vérifier si les champs email et password existent dans le body
    if (!email || !password) {
        return res.status(400).json({ message: 'Email and password are required' });
    }

    // Faire quelque chose avec les données (par exemple, vérification d'authentification)
    // Ici, nous renvoyons simplement les données à titre d'exemple
    res.json({ email, password });
});

// Route pour récupérer les informations des paramètres de l'URL
app.get('/login', (req, res) => {
    const { email, password } = req.query;

    // Vérifier si les paramètres email et password existent dans l'URL
    if (!email || !password) {
        return res.status(400).json({ message: 'Email and password are required' });
    }

    // Faire quelque chose avec les données (par exemple, vérification d'authentification)
    // Ici, nous renvoyons simplement les données à titre d'exemple
    res.json({ email, password });
});

app.listen(PORT, () => {
    console.log(`Server is running on port ${PORT}`);
});
