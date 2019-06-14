mainModule.controller("constraintsListController", ['$rootScope', '$scope', '$http', '$location', '$window', '$state', 'blockUI', 'agilaFactory', function ($rootScope, $scope, $http, $location, $window, $state, blockUI, agilaFactory) {


    $scope.constraintsList = [];
    $scope.constraintsListToShow = [];
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
    $scope.$watch('option.search',function () {
        $scope.setToShow();
    });

    $scope.loadMathJax = function () {
        MathJax.Hub.Queue(["Typeset", MathJax.Hub]);
    };

    $scope.loadConstraints = function () {
        $scope.constraintsList = [];
        blockUI.start();
        $http.get('api/constraint').then(function (response) {
            $scope.constraintsList = response.data;
            $scope.constraintsListSort();
            loaded = true;
            $scope.setToShow();
            blockUI.stop();
        });
    };

    $scope.setToShow = function () {
        $scope.option.startIndex = ($scope.option.currentPage - 1) * $scope.option.maxElements;
        $scope.option.endIndex = $scope.option.startIndex + $scope.option.maxElements;

    };


    $scope.constraintsListSort = function () {
        switch ($scope.option.sortBy) {
            case 'Name ascending':
                $scope.constraintsList.sort(function (a, b) {
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
                $scope.constraintsList.sort(function (a, b) {
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
            case 'Author ascending':
                $scope.constraintsList.sort(function (a, b) {
                    let x = a.createdBy.toLowerCase();
                    let y = b.createdBy.toLowerCase();
                    if (x < y) {
                        return 1;
                    }
                    if (x > y) {
                        return -1;
                    }
                    return 0;
                });
                break;
            case 'Author descending':
                $scope.constraintsList.sort(function (a, b) {
                    let x = a.createdBy.toLowerCase();
                    let y = b.createdBy.toLowerCase();
                    if (x < y) {
                        return -1;
                    }
                    if (x > y) {
                        return 1;
                    }
                    return 0;
                });
                break;
        }
    };

    $scope.deleteConstraintHoverClick = function (id) {
        $('#constraint-accordion-delete-' + id).collapse('toggle');
    };


    $scope.deleteConstraint = function (name) {

        blockUI.start();
        $http.delete('api/constraint/' + name).then(function (response) {
            blockUI.stop();
            agilaFactory.showSuccess('<span>Constraint was deleted successfully!</span>')
            $scope.loadConstraints();
        }, function (response) {
            blockUI.stop();
            agilaFactory.handleRestError(response);
        });

    };

    $scope.searchFilter = function (constraint) {
        if ($scope.option.search.length > 0) {
            regex = new RegExp($scope.option.search.toLowerCase());
            return regex.test(constraint.name.toLowerCase());
        }
        else {
            return /.*?/.test(constraint.name);
        }
    };

    $scope.loadConstraints();

}]);