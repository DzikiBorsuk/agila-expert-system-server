mainModule.controller("dashboardController", ['$rootScope', '$scope', '$http', '$location', '$window', 'blockUI', function ($rootScope, $scope, $http, $location, $window, blockUI) {
    $scope.individuals = 0;
    $scope.classes = 0;
    $scope.objectProperties = 0;
    $scope.dataProperties = 0;

    blockUI.start();

    var init = function() {
          $http.get('api/dashboard/').then(function (response) {
            $scope.individuals = response.data.individuals;
            $scope.classes = response.data.classes;
            $scope.objectProperties = response.data.objectProperties;
            $scope.dataProperties = response.data.dataProperties;
            blockUI.stop();
        });
    };

    init();

}]);