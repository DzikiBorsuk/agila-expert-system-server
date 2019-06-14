mainModule.controller("rulesAddController", ['$rootScope', '$scope', '$http', '$location', '$window', '$state', 'agilaFactory', 'blockUI', function ($rootScope, $scope, $http, $location, $window, $state, agilaFactory, blockUI) {

    $scope.rule = {
        name: null,
        description: null,
        rule: null
    };

    $scope.addRule = function () {
        let validation=$scope.ruleForm.$valid;

        if (validation) {
            blockUI.start();
            $http.post('/api/swrl-rule', {
                name: $scope.rule.name,
                comment: $scope.rule.description,
                rule: $scope.rule.rule
            }).then(function (response) {
                $state.transitionTo('rulesList');
                agilaFactory.showSuccess('Rule <strong>' + $scope.rule.name + '</strong> was added successfully!');
                blockUI.stop();
            }, agilaFactory.handleRestError);
        }
    };

}]);