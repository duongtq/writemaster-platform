const mongoose = require('mongoose');
// const mongodb = require('mongodb');
const dotenv = require('dotenv');

dotenv.config();

const MONGODB_URL = process.env.MONGODB_URL;

mongoose.connect(MONGODB_URL, { useNewUrlParser: true, useUnifiedTopology: true }, (err, db) => {
  if (err) {
    console.log('Unable to connect to the mongoDB server. Error:', err);
  } else {
    console.log('MongoDB connected.');
  }
});

const mongoConnection = mongoose.connection;

module.exports = mongoConnection;

