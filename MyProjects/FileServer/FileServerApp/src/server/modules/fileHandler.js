var express = require('express');
var logger = require('./customLogger.js');
var path = require('path');
var router = express.Router();

var defaultAppDir = __dirname + "/";
var defaultDirChange = '../../view';
var mainAppFile = "index.html";

router.get('/:innerPath/:fileName', function(req, res){
  var innerPath = req.params.innerPath;
  var fileName = req.params.fileName;
  var changePath = defaultDirChange + '/' + innerPath;

  logger.info("file requested " + path.join(defaultAppDir, changePath, fileName));
  res.sendFile(path.join(defaultAppDir, changePath, fileName));
});

router.get('/:innerPath1/:innerPath2/:fileName', function(req, res){
  var innerPath1 = req.params.innerPath1;
  var innerPath2 = req.params.innerPath2;
  var fileName = req.params.fileName;
  var changePath = defaultDirChange + '/' + innerPath1 + '/' + innerPath2;

  logger.info("file requested " + path.join(defaultAppDir, changePath, fileName));
  res.sendFile(path.join(defaultAppDir, changePath, fileName));
});

router.get('/', function(req, res){
  logger.info("app requested");
  res.sendFile(path.join(defaultAppDir, defaultDirChange, mainAppFile));
});

module.exports = router;
