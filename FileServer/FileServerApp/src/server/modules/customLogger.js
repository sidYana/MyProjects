var fs = require('fs');
const logFileName = "../../../data/logs/fileServer.log"

function log(msg){

  msg = Date() + " : " + msg + "\n";
  console.log(msg);
  fs.appendFile(logFileName, msg, function (err) {
    if (err){
        console.log("could not log in logger:"+err);
    }
  });

};

var logger = {
            info: function (info) {
                log('Info: '+info);
            },
            warning:function (warning) {
                log('Warning: '+warning);
            },
            error:function (error) {
                log('Error: '+error);
            },
    };

module.exports = logger;
