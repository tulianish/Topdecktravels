const express = require('express');
const router  = express.Router();

router.post('/validate' ,async (req,res) => {
    try{

        var cardNumber=req.body.cardNumber;
        console.log(cardNumber);
        if(cardNumber == "1111-1111-1111-1111"){
            res.json({message:"success"})
        }
        else{
            res.json({message:"fail"})
        }
        res.send(places);
    }
    catch(error){
        res.json({message:error});

    }
});

module.exports = router;