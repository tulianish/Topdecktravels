var mongoose =  require("mongoose");

var SessionSchema = new mongoose.Schema({
	email: String,
	createdOn: { type: Date, expires: 3600, default: Date.now}
});

SessionSchema.set('toJSON', { virtuals: true });


module.exports = mongoose.model("session", SessionSchema);
