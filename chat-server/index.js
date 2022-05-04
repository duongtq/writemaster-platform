const express = require('express');
const cors = require('cors');
const socket = require('socket.io');
const dotenv = require('dotenv');
const crypto = require('crypto');
const mongoDb = require('./db/MongoDBConnection');
const authorService = require('./services/AuthorService');
const Conversation = require('./db/Conversation');
const bodyParser = require('body-parser');
const Helper = require('./utils/Helper');
const { Server } = require("socket.io");

const http = require('http');

dotenv.config();
const port = process.env.PORT;
const origin = process.env.ORIGIN;

let messengers = authorService.getAllAuthors();

const app = express();

app.use(cors({
  origin: '*'
}));

app.use(bodyParser.json());


app.post('/messenger-list', (req, res) => {
  let messengers = authorService.getAllAuthors();
  res.json(messengers);
});

app.post('/conversations', (req, res) => {
  const messengers = req.body;

  const cursor = mongoDb.collection('conversations')
    .find({
      messengers: messengers
    })
    .toArray(function (err, aum) {

      if (err || aum.length === 0) {
        res.json([]);
        return;
      }

      res.json(aum[0].messages);
    });
});

app.listen(port, () => {
  console.log(`Chat server up and running at port: ${port}`);
});

const server = http.createServer(app);

const io = new Server(server);

io.on('connection', (socket) => {

  socket.emit('2', 'xyz');
});


