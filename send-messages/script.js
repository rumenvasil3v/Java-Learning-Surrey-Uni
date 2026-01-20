const login = require("facebook-chat-api");

login({email: "rumenstvass@gmail.com", password: "x9z_2R4dvWSkPlj"}, (err, api) => {
    if(err) return console.error(err);

    console.log("Success!")
});