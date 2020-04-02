const express = require('express');
const router  = express.Router();
const Place = require('../models/Place');
const Coordinate = require('../models/Coordinate'); 

router.get('/coordinate/:placeName', async (req,res) => {
    try{
        console.log("Here");
        if (req.params.placeName) {
            const query = { city : req.params.placeName}
        console.log(query);
        const coordinate = await Coordinate.find(query);
        console.log(coordinate);
        res.send(coordinate);
    }
    }
    catch(error){
        res.json({message:error});

    }
});

router.get('/coordinate/', async (req,res) => {
    try{
        const coordinate = await Coordinate.find({});
        console.log(coordinate);
        res.send(coordinate);
        }
    catch(error){
        res.json({message:error});

    }
});


router.get('/byId/:Id' ,async (req,res) => {
    try{

        console.log(req.params.Id);
        const place = await Place.findById(req.params.Id)
        console.log(place);
        res.send(place);
    }
    catch(error){
        res.json({message:error});

    }
});

router.get('/byName/:placeName' ,async (req,res) => {
    try{
        console.log(req.params.placeName);
        const place = await Place.find({name: req.params.placeName});
        console.log(place);
        res.send(place);
    }
    catch(error){
        res.json({message:error});

    }
});

router.get('/' ,async (req,res) => {
    try{
        console.log("placeName");
        const places = await Place.find();
        res.send(places);
    }
    catch(error){
        res.json({message:error});

    }
});


module.exports = router;