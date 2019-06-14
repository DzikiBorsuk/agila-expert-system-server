'use strict';
sharedModule.controller('badRequestDialogController', ['$scope', '$uibModalInstance', 'model', function ($scope, $uibModalInstance, model) {

    $scope.model = model;
    $scope.hasModelState = (model.modelState != null);

    $scope.close = function () {
        $uibModalInstance.close(true);
    };

}]);
