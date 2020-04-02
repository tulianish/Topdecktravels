const express = require('express');
const app = express();
const mongoose = require('mongoose');
const bodyParser = require('body-parser');

const placesRoute = require('./routes/places');

app.use(bodyParser.json());

app.use('/places', placesRoute);

mongoose.connect('mongodb://3.88.249.85:27017/TourismCanada' ,
{ useNewUrlParser: true }, 
() => console.log("Mongo connected")
);

app.listen(3000);