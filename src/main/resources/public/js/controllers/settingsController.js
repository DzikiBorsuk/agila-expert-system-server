mainModule.controller("settingsController", ['$rootScope', '$scope', '$http', '$location', '$window', '$state', 'agilaFactory', 'blockUI', 'fileUpload', function ($rootScope, $scope, $http, $location, $window, $state, agilaFactory, blockUI, fileUpload) {

    $scope.ontologyFile = null;

    $http.get('api/user').then(function (response) {
        $scope.user = response.data;
    });


    $scope.download = function() {
        $window.location.href = "/api/ontology";
    };

    $scope.upload = function() {
        var file = $scope.ontologyFile;
        var uploadUrl = "/api/ontology";
        fileUpload.uploadFileToUrl(file, uploadUrl);
    };

    $(function () {
        $('#settings-tab a').click(function (e) {
            e.preventDefault();
            $(this).tab('show');
        })
    });

}]);