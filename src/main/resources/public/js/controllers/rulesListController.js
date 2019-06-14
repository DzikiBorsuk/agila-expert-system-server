mainModule.controller("rulesListController", ['$rootScope', '$scope', '$http', '$location', '$window', 'agilaFactory', 'blockUI', function ($rootScope, $scope, $http, $location, $window, agilaFactory, blockUI) {

    $scope.rulesList = [];
    $scope.rulesToShow=[];
    $scope.numberOfElements = 0;
    $scope.option = {currentPage: 1};
    $scope.option.search = '';
    $scope.option.maxSize = 5; //max numbers of page buttons
    $scope.option.maxElements = 5;

    let loaded = false;

    $scope.$watch('option.maxOnPage', function () {
            if (loaded) {
                $scope.option.maxElements = parseInt($scope.option.maxOnPage);
                $scope.setToShow();
            }
        }
    );

    $scope.$watch('search',function () {
        $scope.setToShow();
    });

    $scope.numberOf = function (number) {
        $scope.numberOfElements = number;
    };

    $scope.loadRules= function () {
        blockUI.start();
        $scope.rulesList = [];
        $http.get('/api/swrl-rule').then(function (response) {
            $scope.numberOfRules = response.data.length;
            $scope.rulesList = response.data;
            $scope.rulesListSort();
            loaded = true;
            $scope.setToShow();
            blockUI.stop();
        });
    };

    $scope.rulesListSort = function () {
        switch ($scope.option.sortBy) {
            case 'Name ascending':
                $scope.rulesList.sort(function (a, b) {
                    let x = a.name.toLowerCase();
                    let y = b.name.toLowerCase();
                    if (x < y) {
                        return -1;
                    }
                    if (x > y) {
                        return 1;
                    }
                    return 0;
                });
                break;
            case 'Name descending':
                $scope.rulesList.sort(function (a, b) {
                    let x = a.name.toLowerCase();
                    let y = b.name.toLowerCase();
                    if (x < y) {
                        return 1;
                    }
                    if (x > y) {
                        return -1;
                    }
                    return 0;
                });
                break;

        }
    };


    $scope.setToShow = function () {
        $scope.option.startIndex = ($scope.option.currentPage - 1) * $scope.option.maxElements;
        $scope.option.endIndex = $scope.option.startIndex + $scope.option.maxElements;
    };

    $scope.deleteRuleHoverClick = function (id) {
        $('#rule-accordion-delete-' + id).collapse('toggle');
    };


    $scope.deleteRule = function (name) {

        blockUI.start();
        $http.delete('api/swrl-rule/' + name).then(function (response) {
            blockUI.stop();
            agilaFactory.showSuccess('<span>Rule was deleted successfully!</span>')
            $scope.loadRules();
        }, function (response) {
            blockUI.stop();
            agilaFactory.handleRestError(response);
        });

    };


    $scope.searchFilter = function (rule) {
        if ($scope.option.search.length > 0) {
            let regex = new RegExp($scope.option.search.toLowerCase());
            return regex.test(rule.name.toLowerCase());
        }
        else {
            return /.*?/.test(rule.name);
        }
    };


    $scope.loadRules();

}]);