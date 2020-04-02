const bcrypt = require('bcryptjs');
const db = require('../_helpers/db');
const User = db.User;
const Session = db.Session;

module.exports = {
    authenticate,
    logout,
    validateQuestion,
    getAll,
    getById,
    create,
    update,
    delete: _delete
};

async function authenticate({email, password}) {
    console.log(email,password)
    const user = await User.findOne({ email });
    if (!user) {
        console.log("email not found");
    }
    if(user){
        console.log("email found");
    }
    if (user && bcrypt.compareSync(password, user.password)) {
        return await user;
    }
}

async function logout(id) {
    console.log(id.email)
    await Session.findOneAndDelete({email : id.email});
}

async function validateQuestion({ email, securityQuestion, answer }) {
    const user = await User.findOne({ email });
    if(user && user.securityQuestion == securityQuestion && bcrypt.compareSync(answer, user.answer)){
        
        var newSession = new Session();
            newSession.email = email;
        await newSession.save();

        return await user
    }
}

async function getAll() {
    return await User.find().select('-hash');
}

async function getById(id) {
    const sessionCheck = await Session.findOne({email : id.email})
    if (!sessionCheck){
        throw 'User not logged in'
    }
    else {
    return await User.find({email : id.email}).select('-hash');
}
}

async function create(userParam) {

    // validate
    if(!(/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(userParam.email))) {
        throw 'Invalid Email Address';
    }

    if (userParam.password.length<6) {
        throw 'Password needs to be atleast 6 characters'
    }

    if (await User.findOne({ email: userParam.email })) {
        throw 'Email "' + userParam.email + '" is already taken';
    }

    else {
        console.log("reached");
        var newUser = new User();
                newUser.name = userParam.name;
                newUser.email = userParam.email;
                newUser.password = newUser.generateHash(userParam.password);
                newUser.securityQuestion = userParam.securityQuestion;
                newUser.answer = newUser.generateHash(userParam.answer);
                newUser.dateCreated = new Date();

        var newSession = new Session();
            newSession.email = userParam.email;

        //save session
        await newSession.save();

        // save user
        await newUser.save();
    }
}

async function update(id, userParam) {
    console.log(id, userParam.password)
    const user = await User.findOne({email : id});
    var test = bcrypt.compareSync(userParam.password, user.password)
    console.log(test)
    console.log(user)

    // validate
    if (user.length == 0) throw 'User not found';

    //validate if old password is same as new password
    else if(bcrypt.compareSync(userParam.password, user.password)) {
        throw 'Old and New Password entered cannot be the same'
    }

    //validate the new password for length
    else if (userParam.password.length<6) {
        throw 'New password needs to be atleast 6 characters'
    }

    // hash password if it was entered
    else if (userParam.password) {
        userParam.password = bcrypt.hashSync(userParam.password, 10);
    }

    // copy userParam properties to user
    Object.assign(user, userParam);

    await user.save();
}

async function _delete(id) {
    await User.findOneAndDelete({email : id});
}