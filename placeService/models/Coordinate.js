const mongoose = require('mongoose');
const CoordinateSchema = mongoose.Schema({
    city : {
        type : String,
        required : true
    },
    province : {
        type : String,
        required : true
    },
    coordinate : {
        type : Number,
        required :true
    }
})

module.exports=mongoose.model('coordinates',CoordinateSchema,'coordinates');