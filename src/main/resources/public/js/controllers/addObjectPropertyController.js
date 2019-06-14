sharedModule.controller('addObjectPropertyController', ['$scope', '$http', '$uibModalInstance', '$sce', 'referenceProperty', 'callback', function ($scope, $http, $uibModalInstance, $sce, referenceProperty, callback) {

    $scope.referenceProperty = referenceProperty;

    $scope.model = {
        parentName:  $scope.referenceProperty.name,
        option: "child",
        name: null,
        description: null,
        range: null
    };

    $http.get('api/class-hierarchy/' + $scope.referenceProperty.range).then(function (response) {
        if (response.data.length > 0) {
            $scope.classes = response.data[0].nodes;
            $scope.selectedRange = $scope.classes[0];
        }
    });

    $scope.addObjectProperty = function () {
        if (callback) {
            if ($scope.addObjectPropertyForm.$valid) {
                $scope.model.range = $scope.selectedRange.text;
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