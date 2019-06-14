sharedModule.controller('messageDialogController', ['$scope', '$uibModalInstance', '$sce', 'title', 'message', 'buttonClassClose', 'buttonClassConfirmation', 'confirmButtonText', 'okCallback', function ($scope, $uibModalInstance, $sce, title, message, buttonClassClose, buttonClassConfirmation, confirmButtonText, okCallback) {

	$scope.title = $sce.trustAsHtml(title);
	$scope.message = $sce.trustAsHtml(message);
	$scope.buttonClassClose = buttonClassClose;
	$scope.buttonClassConfirmation = buttonClassConfirmation;
	$scope.confirmButtonText = confirmButtonText;

	$scope.confirm = function () {
		if (okCallback) {
			okCallback($uibModalInstance, $scope);
		} else {
            $uibModalInstance.close(true);
		}
	};

	$scope.modalDismiss = function () {
        $uibModalInstance.close(true);
    }

}]);
