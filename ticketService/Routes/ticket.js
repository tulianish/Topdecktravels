

const express=require('express');

const router=express.Router();


const nodemailer = require('nodemailer');
const smtpTransport = require('nodemailer-smtp-transport');

const PDFDocument = require('pdfkit');

const fs = require('fs');

const Tickets=require('../Models/ticket');

//put id name email



router.get('/:ticketId',async(req,res)=>{
    try{
        const search=await Tickets.findById(req.params.ticketId);
        res.json(search);
        const doc = new PDFDocument;
        doc.pipe(fs.createWriteStream('Ticket'+search._id+'.pdf'));
        doc.image('ticket.png', 260, 15, {width: 100})

        doc.fontSize(20)
        .text("\nTicket Details",{align:'center'})
        .font('Times-Bold')


    
        doc.fontSize(15)
        .font('Times-Roman')
        .text(' \nEmail:' + search.email+'         Ticket ID         : ' + search._id+ '   \nName :' + search.name+'                    Booking Date  : '+search.booking_date+'\n\n\n\n')

        doc.fontSize(13)
        .font('Times-Bold')
        .text('\nTravel Details                                                                      Fare        Tax         Total')

        doc.fontSize(13)
        .font('Times-Roman')
        .text('\nSource City :             '+search.from_place+'                                            '+
        search.fare+'          '+search.tax +'        '+search.amount)
        .text('Destination Place :    '+search.to_place)
        .text('Travel Date :             '+search.travel_date)
        .text('Travel Time :             '+search.travel_time)

        .rect(15, 200, 570, 160).stroke();

        doc.fontSize(10)
    
        .font('Times-Bold')
        .text('\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\nTopdeck Travels Support\n1315 Dresden Row\nHalifax NS B3J 2K9\ntopdecktravels5409+support@gmail.com\nwww.topdecktravels.com/support',{align:'center'})
        doc.end()

  
    process.env.NODE_TLS_REJECT_UNAUTHORIZED = "0";
    var transporter = nodemailer.createTransport(smtpTransport({
        service: 'gmail',
        host: 'smtp.gmail.com',
        port:587,
        secure: false,
        auth: {
          user: 'topdecktravels5409@gmail.com',
          pass: 'cloudgroup17'
        }
      }))
      
      var mailOptions = {
        from: 'topdecktravels5409@gmail.com',
        to: search.email,
        subject: 'Topdeck Travels - Booking Ref. '+search._id,
        html: 'Dear Customer,<br>Thank you for booking with Topdeck Travels.<br><br>Please find attached a copy of your ticket.<br>Please quote this booking reference for any further support.<br><br>Happy Travelling!!<br><br><br>Topdeck Travels Support<br>1315 Dresden Row<br>Halifax NS B3J 2K9<br>topdecktravels5409+support@gmail.com<br>www.topdecktravels.com/support',
        attachments:[{
            filename: 'Ticket'+search._id+'.pdf',  
            path: 'Ticket'+search._id+'.pdf',                                       
            contentType: 'application/pdf'
        }]
        };
      
      
      transporter.sendMail(mailOptions, function(error, info){
        if (error) {
          console.log(error);
        } else {
          console.log('Email sent: ' + info.response);
        }
        transporter.close();
      });
    }
    catch(err){
        res.json({message:err});
    }
});


module.exports=router;
