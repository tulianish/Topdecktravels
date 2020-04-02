const express = require('express');
const app = express();
const bodyParser = require('body-parser');
const errorHandler = require('./_helpers/error-handler');

app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());

// api routes
app.use('/users', require('./users/users.controller'));

// global error handler
app.use(errorHandler);

// start server
app.listen(3001,function(){
	console.log('Server Started');
})
