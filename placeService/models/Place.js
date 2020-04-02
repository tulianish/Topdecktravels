const mongoose = require('mongoose');

const PlaceSchema = mongoose.Schema({
    name : {
        type : String,
        required : true
    },
    image_url : {
        type : String,
        required : true
    },
    formatted_address : {
        type : String,
        required : true
    },
    open_status : {
        type : Boolean,
        default : false
    },
    coordinate : {
        type : Number,
        required :true
    },
    city : {
        type : String,
        required : true
    },
    province : {
        type : String,
        required:true
    }
})

module.exports=mongoose.model('places',PlaceSchema,'places');