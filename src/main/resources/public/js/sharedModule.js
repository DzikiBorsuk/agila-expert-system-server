var sharedModule = angular.module('shared', ['ui.bootstrap', 'blockUI', 'angular-bind-html-compile']);

sharedModule.config(['blockUIConfig', function (blockUIConfig) {
    blockUIConfig.autoBlock = false;
    blockUIConfig.template = '<div class="block-ui-message-container"><div class="block-ui-message"><div class="loader"></div></div></div>';
    blockUIConfig.cssClass = "block-ui loading-blockUI";
}]);

sharedModule.directive('tooltip', function(){
    return {
        restrict: 'A',
        link: function(scope, element){
            element.hover(function(){
                element.tooltip('show');
            }, function(){
                element.tooltip('hide');
            });
        }
    };
});

sharedModule.factory('agilaFactory', ['$http', '$uibModal', 'blockUI', '$window', function ($http, $uibModal, blockUI, $window) {

    let showAlert = function (title, message, buttonClassClose, buttonClassConfirmation, confirmButtonText, okCallback, animation) {
        if (animation !== false) {
            animation = true;
        }
        $uibModal.open({
            templateUrl: '../views/messageDialog.html',
            controller: 'messageDialogController',
            windowClass: 'center-custom-modal',
            backdropFade: true,
            animation: animation,
            resolve: {
                title: function () {
                    return title;
                },
                message: function () {
                    return message;
                },
                buttonClassClose: function () {
                    return buttonClassClose
                },
                buttonClassConfirmation: function () {
                    return buttonClassConfirmation
                },
                confirmButtonText: function () {
                    return confirmButtonText
                },
                okCallback: function () {
                    return okCallback;
                }
            }
        });
        setTimeout(function () {
            $('.center-custom-modal .modal-dialog').addClass('modal-dialog-centered')
        }, 20);
    };

    let showSuccess = function (message, customCallback) {
        $.notify({
            icon: "fa fa-info-circle",
            title: "<strong>Success!</strong>",
            message: message
        },{
            type: 'success',
            onClose: customCallback
        });
    };

    let showError = function (message, customCallback, animation) {
        if (customCallback) {

        }
        else {
            showAlert('<span>Error</span><span class="fa fa-exclamation-triangle fa-lg text-danger"></span>', message, 'd-none', 'btn-danger', 'Close', customCallback, animation);
        }
    };

    let handleRestError = function (response, animation) {
        blockUI.stop();
        if (response.status === 400) {
            $uibModal.open({
                templateUrl: '../views/badRequestDialog.html',
                controller: 'badRequestDialogController',
                resolve: {
                    model: function () {
                        return response.data;
                    }
                }
            });
        } else if (response.status === 401) {
            showAlert('Message', 'Your session has expired', 'd-none', 'btn-danger', 'Close', function ($uibModalInstance) {
                $window.location.reload();
                //logout
            }, animation);
        } else if (response.status === 404) {
            let message = "";
            if (response.data.message != null) {
                message = response.data.message;
            }
            showAlert('Not Found', message, 'btn-danger', 'd-none', null, null, animation);
        } else if (response.status === 422) {
            let message = "";
            if (response.data.message != null) {
                message = response.data.message;
            } else if (response.data.agilaapierror != null && response.data.agilaapierror.subErrors != null) {
                for (let i in response.data.agilaapierror.subErrors) {
                    message = message.concat(response.data.agilaapierror.subErrors[i].message);
                    if (response.data.agilaapierror.subErrors[i].rejectedValue) {
                        message = message.concat("<p><b>" + response.data.agilaapierror.subErrors[i].rejectedValue + "</b></p>");
                    }
                }
            }
            showAlert('A validation error has occurred', message, 'btn-danger', 'd-none', null, null, animation);
        } else if (response.data && response.data.message) {
            showAlert('An error has occurred', response.data.message, 'btn-danger', 'd-none', null, null, animation);
        } else {
            showAlert('An error has occurred', 'HTTP error ' + response.status, 'btn-danger', 'd-none', null, null, animation);
        }
    };

    return {
        showAlert: showAlert,
        handleRestError: handleRestError,
        showSuccess: showSuccess,
        showError: showError

    };

}]);