var app = angular.module("myApp", ["ngRoute"]);
app.config(function($routeProvider) {
    $routeProvider
    .when("/FileServer", {
        controller : "fileSysCtrler",
        templateUrl : "/FileServer/html/views/FileSystemView.html"
    }).when("/FileSystem", {
        controller : "fileSysCtrler",
        templateUrl : "/FileServer/html/views/FileSystemView.html"
    }).when("/Settings", {
        controller : "settingsCtrler",
        templateUrl : "/FileServer/html/views/SettingsView.html"
    }).otherwise({
        controller : "fileSysCtrler",
        templateUrl : "/FileServer/html/views/FileSystemView.html"
    });
});

app.factory('commonService', function($http, $rootScope){

  return {
    commonHttpRequest : function(request, callback){
        var httpRequestParams;

        switch (request.requestType) {
          case httpRequestTypes.GET:
            httpRequestParams = {
              method : request.requestType,
              url : request.requestUrl
            }
            makeHttpRequest();
            break;
          case httpRequestTypes.POST:
            httpRequestParams = {
              method : request.requestType,
              url : request.requestUrl,
              data : request.data
            }
            makeHttpRequest();
            break;
          default:
            return;
        }

        function makeHttpRequest(){
            $http(httpRequestParams)
            .then(function mySuccess(response){
              callback(response.data);
            }, function myError(response){
              console.log("some error occured "+response);
              callback(response);
            });
        };

      },
      showSpinner : function(){
        $rootScope.showLoading = true;
      },
      hideSpinner : function(){
        $rootScope.showLoading = false;
      },
      setPageHeading : function(title){
        $rootScope.navBarTitle = title;
      },
      showModal : function(modal){
        document.getElementById(modal).style.display='block'
      },
      hideModal : function(modal){
        document.getElementById(modal).style.display='none'
      }
  };

});

app.directive('ngRightClick', function($parse) {
    return function(scope, element, attrs) {
        var fn = $parse(attrs.ngRightClick);
        element.bind('contextmenu', function(event) {
            scope.$apply(function() {
                event.preventDefault();
                fn(scope, {$event:event});
            });
        });
    };
});
