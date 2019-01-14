var logger = require('./customLogger.js');
const dirTree = require('directory-tree');
var FileClass = require('./POJsO/file.js');
var pathModule = require('path');
var formidable = require('formidable');
var dirPath = '<folder-path>';

function getBackReferenceFile(path){

  var tempList = [];
  if(dirPath != path){
    var normPath = pathModule.normalize(path).split('\\');
    console.log(normPath);
    var backRefPath = "";
    for(var index = 0; index < normPath.length-1; index++){
      backRefPath += normPath[index];
      if(index < normPath.length-2){
        backRefPath += "/";
      }
    }
    console.log(backRefPath);
    tempList.push(new FileClass("Back", backRefPath, "back_btn", 0, "back_btn"));
  }
  return tempList;
};

function getFolderStructureForFolder(folderPath){

  var directoryTree = dirTree(folderPath);
  var fileList = getBackReferenceFile(folderPath);

  if('children' in directoryTree){
    var children = directoryTree.children;

    for(var index = 0; index < children.length; index++){
      var file;
      if(children[index].type == 'directory'){
        file = new FileClass(children[index].name,
                             children[index].path,
                             children[index].type,
                             children[index].size,
                             "");
      }else{
        file = new FileClass(children[index].name,
                             children[index].path,
                             children[index].type,
                             children[index].size,
                             children[index].extension);
      }
      fileList.push(file);
    }
  }
  return fileList;
};

function getDefaultFolderStructure(req, res) {
  logger.info("request for default folder structure");
  return getFolderStructureForFolder(dirPath);

};

function getParticularFolderStructure(req, res){
  logger.info("request for particular folder structure:" + req.body);
  return getFolderStructureForFolder(req.body.folderName);
}

function downloadFiles(req, res) {
  logger.info("request for download:" + req.body.data);
  var fileList = req.body.data;

  if(fileList.length == 1){
    if(fileList[0].fileType == "directory"){

    }
  }

  console.log(fileList);
  res.send();
}

function uploadFiles(req, res){
  var form = new formidable.IncomingForm();

  form.parse(req);

  form.on('fileBegin', function (name, file){
      file.path = __dirname + '/uploads/' + file.name;
  });

  form.on('file', function (name, file){
      console.log('Uploaded ' + file.name);
  });

  res.sendFile(__dirname + '/index.html');
}

var serviceRequests = {

  getFolderStructure : function(req, res){
    return getDefaultFolderStructure(req, res);
  },
  getParticularFolderStructure : function(req, res){
    return getParticularFolderStructure(req, res);
  },
  downloadFiles : function(req, res){
    return downloadFiles(req, res);
  },
  uploadFiles : function(req, res){
    return uploadFiles(req, res);
  }

};

module.exports = serviceRequests;
