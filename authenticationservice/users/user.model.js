var mongoose =  require("mongoose");
var bcrypt   = require('bcrypt-nodejs');

var UserSchema = new mongoose.Schema({
    name: String,
	email: String,
	password:String,
	securityQuestion: String,
	answer: String,
	dateCreated : Date
});

UserSchema.set('toJSON', { virtuals: true });

// methods ======================

// generating a hash
UserSchema.methods.generateHash = function(password) {
    return bcrypt.hashSync(password, bcrypt.genSaltSync(8), null);
};

// checking if password is valid
UserSchema.methods.validPassword = function(candidatePassword) {
    if(this.password != null) {
        return bcrypt.compareSync(candidatePassword, this.password);
    } else {
        return false;
    }
};


module.exports = mongoose.model("User", UserSchema);