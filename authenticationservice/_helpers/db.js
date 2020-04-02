const mongoose = require('mongoose');
mongoose.connect('mongodb://3.88.249.85/TourismCanada')
mongoose.Promise = global.Promise;

module.exports = {
    User: require('../users/user.model'),
    Session: require('../users/session.model')
};