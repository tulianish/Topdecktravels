const express = require('express');
const app = express();
var cors = require('cors');
app.use((req, res, next) => { 
	res.header('Access-Control-Allow-Origin', '*');
	next();});
	app.use(cors());

// creating a route for the home page
app.get("/", function(req, res){
	res.render("home.ejs")
});

// creating a route for the book page
app.get("/book", function(req, res){
	res.render("book.ejs")
});

// creating a route for the login page
app.get("/login", function(req, res){
	res.render("login.ejs")
});

// creating a route for the places page
app.get("/place", function(req, res){
	res.render("place.ejs")
});

// creating a route for the registration page
app.get("/registration", function(req, res){
	res.render("registration.ejs")
});

// creating a route for the ticket page
app.get("/ticket", function(req, res){
	res.render("ticket.ejs")
});

app.listen(3000,function(){
	console.log('Server Started');
})