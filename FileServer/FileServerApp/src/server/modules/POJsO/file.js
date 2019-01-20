module.exports = class FileClass{

  constructor(fileName, filePath, fileType, fileSize, fileExtension){
    this.fileName=fileName;
    this.filePath=filePath;
    this.fileType = fileType;
    this.fileSize = fileSize;
    this.fileExtension = fileExtension;
    this.expandFileOptions = false;
  }

}
