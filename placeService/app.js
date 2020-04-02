const express = require('express');
const app = express();
const mongoose = require('mongoose');
const bodyParser = require('body-parser');
const cors=require('cors');
app.use((req, res, next) => { 
	res.header('Access-Control-Allow-Origin', '*');
	next();});
	app.use(cors());

const placesRoute = require('./routes/places');

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));

app.use('/places', placesRoute);

mongoose.connect('mongodb://3.88.249.85:27017/TourismCanada' ,
{ useNewUrlParser: true }, 
() => console.log("Mongo connected")
);

app.listen(3000);