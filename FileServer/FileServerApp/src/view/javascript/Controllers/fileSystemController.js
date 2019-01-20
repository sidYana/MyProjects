app.controller("fileSysCtrler", function ($scope, $rootScope, $http, commonService) {

    $scope.ctrlInit = function(){
      commonService.hideSpinner();
      commonService.setPageHeading("File System")
      $scope.folderPaths = [];
      $scope.selectedFile;
      $scope.requestDirTree();
    };

    $scope.openFile = function(file){

      if(file.fileType=="directory" || file.fileType=="back_btn"){
        zoomIn(file);
      }else if(supportedVideoFormats[file.fileExtension]){
        streamVideo(file, supportedVideoFormats[file.fileExtension]);
        commonService.showModal('video_player_modal');
      }else if(supportedAudioFormats[file.fileExtension]){
        streamAudio(file, supportedAudioFormats[file.fileExtension]);
        commonService.showModal('audio_player_modal');
      }else{
        console.log("operation not supported");
      }

    };

    function getSelectedFiles(){
      selectedRows = [];
      for(var count = 0;count < $scope.dirTree.length; count++){
        if($scope.dirTree[count].selected && $scope.dirTree[count].fileType!="back_btn"){
          if ($scope.dirTree[count].fileType == "file") {
            selectedRows.push($scope.dirTree[count]);
          }
        }
      }
      return selectedRows;
    };

    $scope.downloadFiles = function(){
      commonService.showSpinner();
      var filesList = getSelectedFiles();

      var data = {
        data : fileList
      }

      var request = {
        requestType : httpRequestTypes.POST,
        data : data,
        requestUrl : serverRequestPaths.downloadFilesRequest
      }

      commonService.commonHttpRequest(request, function(response){
        console.log(response);
        commonService.hideSpinner();
      });

    }

    $scope.uploadFiles = function(){
      console.log(getSelectedFiles());
    }

    $scope.clearVideoData = function(){
      $scope.videoNameShown = "";
      $scope.video_url = "";
      $scope.contentType = "";
      commonService.hideModal('video_player_modal');
    };

    $scope.clearAudioData = function(){
      $scope.audioNameShown = "";
      $scope.audio_url = "";
      $scope.contentType = "";
      commonService.hideModal('audio_player_modal');
    };

    $scope.clearPropertiesData = function(){
      commonService.hideModal('file_properties_modal');
    }

    $scope.requestDirTree = function(){
      commonService.showSpinner();
      var request = {
        requestType : httpRequestTypes.GET,
        requestUrl : serverRequestPaths.folderStructureRequest
      }

      commonService.commonHttpRequest(request, function(response){
        $scope.dirTree = response;

        for(var count = 0;count < $scope.dirTree.length; count++){
          $scope.dirTree[count].selected=false;
          console.log($scope.dirTree[count].selected);
        }

        if($scope.dirTree[0].fileType=="back_btn"){
          $scope.currentDir = $scope.dirTree[1].filePath;
        }else{
          $scope.currentDir = $scope.dirTree[0].filePath;
        }

        commonService.hideSpinner();
      });
    };

    function streamAudio(file, contentType) {
        commonService.showSpinner();
        $scope.audioNameShown = file.fileName;
        $scope.audio_url = "/request/streamAudio?filePath=" + file.filePath + "&fileContentType=" + contentType;
        $scope.contentType = contentType;
        console.log($scope.audio_url);
        commonService.hideSpinner();
    }

    $scope.showClicked = function(){
      for(var count = 0;count < $scope.dirTree.length; count++){
        console.log($scope.dirTree[count].selected);
      }
    }

    function streamVideo(file, contentType) {
        commonService.showSpinner();
        $scope.videoNameShown = file.fileName;
        $scope.video_url = "/request/streamVideo?filePath=" + file.filePath + "&fileContentType=" + contentType;
        $scope.contentType = contentType;
        console.log($scope.video_url);
        commonService.hideSpinner();
    }

    function zoomIn(file){
      if(file.fileType == "directory" || file.fileType == "back_btn"){
        commonService.showSpinner();
        var data = {
          folderName:file.filePath
        }

        var request = {
          requestType : httpRequestTypes.POST,
          data : data,
          requestUrl : serverRequestPaths.folderStructureRequest
        }

        commonService.commonHttpRequest(request, function(response){
          $scope.dirTree = response;

          for(var count = 0;count < $scope.dirTree.length; count++){
            $scope.dirTree[count].selected=false;
            console.log($scope.dirTree[count].selected);
          }

          if($scope.dirTree[0].fileTyep="back_btn"){
            $scope.currentDir = file.filePath;
          }else{
            $scope.currentDir = $scope.dirTree[0].filePath;
          }

          commonService.hideSpinner();
        });
      }else{
        console.log("the selected object is a file and not a directory");
      }
    }

    $scope.showFileMenu = function(file){
      console.log("Menu display");
    }

    $scope.setSelectedFile = function(file){
      $scope.selectedFile = file;
      commonService.showModal('file_properties_modal');
    }

    $scope.getImgForFileType = function(file){
      if(file.fileType == "directory"){
        return imgToExt["folder"];
      } else if(imgToExt[file.fileExtension] != null || file.fileType == "back_btn") {
        return imgToExt[file.fileExtension];
      } else {
        return imgToExt["default"];
      }
    }

});
