app.controller("settingsCtrler", function ($scope, $rootScope, $http, commonService) {
  $scope.ctrlInit = function(){
    commonService.setPageHeading($rootScope, "Settings")
    commonService.hideSpinner($rootScope);
  };
});
