const mongoose=require('mongoose');

const PostSchema=mongoose.Schema({

    name:
    {  
        type:String,
        required:true

    },

    email:
    {  
        type:String,
        required:true

    },

    travel_date:
    {  
        type:String,
        required:true

    },
    booking_date:
    {  
        type:String,
        required:true

    },
    travel_time:
    {  
        type:String,
        required:true

    },
    from_place:{
        type:String,
        required:true
    },

    to_place:{
        type:String,
        required:true
    },

    fare:{
        type:String,
        required:true
    },

    tax:{
        type:String,
        required:true
    },

    amount:{
        type:String,
        required:true
    }
   
});



module.exports=mongoose.model('Bookings',PostSchema);
