const express=require('express');
 
const app=express();
const bodyParser=require('body-parser');
const mongoose=require('mongoose');
//Cannot find module 'dotenv/config'require('dotenv/config');
const cors=require('cors');



app.use(cors());
app.use(bodyParser.json());

const ticketRoute=require('./Routes/ticket');

app.use('/ticket',ticketRoute);

app.get('/',(req,res)=>{
    res.send('WE ARE ON THE TICKET PAGE');
});

mongoose.connect('mongodb://3.88.249.85:27017/TourismCanada', {useNewUrlParser: true},
()=>
    console.log('DB connected')
    
);

app.listen(7000);

