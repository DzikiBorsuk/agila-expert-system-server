sharedModule.controller('addClassController', ['$scope', '$uibModalInstance', '$sce', 'referenceClass', 'callback', function ($scope, $uibModalInstance, $sce, referenceClass, callback) {

    $scope.referenceClass = referenceClass;
    $scope.isThing = $scope.referenceClass === 'Thing';

    $scope.model = {
        option: "child",
        name: null,
        description: null
    };

    $scope.addNewClass = function () {
        if (callback) {
            if ($scope.addClassForm.$valid) {
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