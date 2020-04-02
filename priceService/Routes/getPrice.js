const express=require('express');
const router=express.Router();
const Place=require('../Models/Place');
const Coordinate = require('../Models/Coordinate')
const bodyParser=require('body-parser');

router.post('/getPrices',async(req,res)=>{
    try{
    source = req.body.from_place;
    destination = req.body.to_place;

  

    var source_data = await Coordinate.find({city : source});
    var dest_data = await Place.find({name : destination});



    var source_coordinate = source_data[0].coordinate;
    var destination_coordinate = dest_data[0].coordinate;
    

    var distance=Math.abs(destination_coordinate-source_coordinate);
    const pricePerCoordinate=10;

    var cost =distance*pricePerCoordinate;
    res.json({price : cost})

    }
    catch(err){
        res.json({message:err});
    }

});

module.exports=router;
