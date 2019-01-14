const httpRequestTypes = {
  GET : "GET",
  POST : "POST"
}

const serverRequestPaths = {
  folderStructureRequest: "/request/folderStructure",
  downloadFilesRequest: "/request/downloadFiles"
}

const supportedVideoFormats = {
  ".mp4"   : "video/mp4",
  ".m4v"   : "video/mp4",
  ".webm"  : "video/webm",
  ".ogv"   : "video/ogg"
};

const supportedAudioFormats = {
  ".aac"   : "audio/aac",
  ".m4a"   : "audio/mp4",
  ".mp1"   : "audio/mpeg",
  ".mp2"   : "audio/mpeg",
  ".mp3"   : "audio/mpeg",
  ".mpg"   : "audio/mpeg",
  ".mpeg"  : "audio/mpeg",
  ".wav"   : "audio/wav"
};

const imgToExt = {
  'folder':'/FileServer/assets/folder_icon.png',
  '.jpg':'/FileServer/assets/image_icon.png',
  'default':'/FileServer/assets/file_icon.png',
  'back_btn':'/FileServer/assets/back_icon.png'
};
