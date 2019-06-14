sharedModule.controller('addDataPropertyController', ['$scope', '$http', '$uibModalInstance', '$sce', 'referenceProperty', 'callback', function ($scope, $http, $uibModalInstance, $sce, referenceProperty, callback) {

    $scope.referenceProperty = referenceProperty;

    $scope.model = {
        parentName:  $scope.referenceProperty.name,
        option: "child",
        name: null,
        description: null,
        dataType: null
    };

    $http.get('api/data-types/').then(function (response) {
        if (response.data.length > 0) {
            $scope.dataTypes = response.data;
            $scope.model.dataType = $scope.dataTypes[0];
        }
    });

    $scope.addDataProperty = function () {
        if (callback) {
            if ($scope.addDataPropertyForm.$valid) {
                callback($uibModalInstance, $scope.model);
            }
        } else {
            $uibModalInstance.close(true);
        }
    };

    $scope.modalDismiss = function () {
        $uibModalInstance.close(true);
    }

}]);