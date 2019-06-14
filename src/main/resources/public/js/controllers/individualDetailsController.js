mainModule.controller("individualDetailsController", ['$rootScope', '$scope', '$http', '$location', '$window', '$state', 'agilaFactory', 'blockUI', 'id', function ($rootScope, $scope, $http, $location, $window, $state, agilaFactory, blockUI, id) {

    $scope.model = null;

    blockUI.start();
    $http.get('api/individual/' + id).then(function (response) {
        $scope.model = response.data;
        blockUI.stop();
    });
}]);