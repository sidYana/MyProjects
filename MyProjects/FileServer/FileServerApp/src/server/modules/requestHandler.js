var express = require('express');
var logger = require('./customLogger.js');
var serviceRequests = require('./serviceRequests.js');
var streamRequests = require('./streamService.js');
var router = express.Router();

var serverOut;
var receivedRequest;
router.get('/:requestName', function(req, res){

  serverOut = "get request:" + Date() +"\n"
  receivedRequest = req.params.requestName;

  switch (receivedRequest) {
    case "folderStructure":
      var data = serviceRequests.getFolderStructure(req, res);
      res.send(data);
      break;
    case "streamVideo":
      streamRequests.video(req, res);
      break;
    case "streamAudio":
      streamRequests.audio(req, res);
      break;
    default:
      logger.warning("default request");
      res.send(serverOut);
  }

});

router.post('/:requestName', function(req, res){
  serverOut = "get request:" + Date() +"\n";
  receivedRequest = req.params.requestName;

  switch (receivedRequest) {
    case "folderStructure":
      var data = serviceRequests.getParticularFolderStructure(req, res);
      res.send(data);
      break;
    case "downloadFiles":
      serviceRequests.downloadFiles(req, res);
      break;
    case "uploadFiles":
      serviceRequests.uploadFiles(req, res);
      break;
    default:
      logger.warning("default request");
      res.send(serverOut);
  }

});

module.exports = router;
