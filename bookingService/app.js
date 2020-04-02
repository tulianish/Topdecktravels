const express=require('express');
const app=express();
const bodyParser=require('body-parser');
const mongoose=require('mongoose');
//npm require('dotenv/config');
const cors=require('cors');
app.use((req, res, next) => { 
	res.header('Access-Control-Allow-Origin', '*');
	next();});
	app.use(cors());


app.use(cors());
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());

const paymentRoute = require('./Routes/payment');

app.use('/payment',paymentRoute);

const ticketRoute=require('./Routes/booking');

app.use('/booking',ticketRoute);

app.get('/',(req,res)=>{
    res.send('WE ARE ON THE BOOKING PAGE');
});

mongoose.connect('mongodb://3.88.249.85:27017/TourismCanada', {useNewUrlParser: true},

()=>
    console.log('DB connected')
    
);

app.listen(3002);

