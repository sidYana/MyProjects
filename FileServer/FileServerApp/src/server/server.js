var express = require('express');
var bodyParser = require('body-parser');
//var cookieParser = require('cookie-parser');
var reqHandler = require('./modules/requestHandler.js');
var fileHandler = require('./modules/fileHandler.js');
var logger = require('./modules/customLogger.js');
var app = express();

//To parse URL encoded data
app.use(bodyParser.urlencoded({ extended: false }))

//To parse json data
app.use(bodyParser.json())

//To handle cookies
//app.use(cookieParser());

app.use('/request', reqHandler);
app.use('/FileServer', fileHandler);

app.listen(3000);

logger.info("Server started at "+Date());
