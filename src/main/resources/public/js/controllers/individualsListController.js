mainModule.controller("individualsListController", ['$rootScope', '$scope', '$http', '$location', '$window', 'agilaFactory', 'blockUI', function ($rootScope, $scope, $http, $location, $window, agilaFactory, blockUI) {

    $scope.individualsList = [];
    $scope.individualsToShow=[];
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
        $scope.numberOfElements=number;
    };


    $scope.loadIndividuals = function () {
        blockUI.start();
        $scope.individualsList = [];
        $http.get('/api/individual').then(function (response) {
            $scope.numberOfIndividuals = response.data.length;
            response.data.forEach(function (element) {
                let individual = element.name;
                $http.get('/api/individual/' + individual).then(function (response) {
                    $scope.individualsList.push(response.data);
                    if ($scope.individualsList.length === $scope.numberOfIndividuals) {
                        $scope.individualsListSort();
                        $scope.setToShow();
                        blockUI.stop();
                        loaded = true;
                    }
                });
            });
        });
    };

    $scope.individualsListSort = function () {
        switch ($scope.option.sortBy) {
            case 'Name ascending':
                $scope.individualsList.sort(function (a, b) {
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
                $scope.individualsList.sort(function (a, b) {
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

    $scope.deleteIndividualHoverClick = function (id) {
        $('#individual-accordion-delete-' + id).collapse('toggle');
    };


    $scope.deleteIndividual = function (name) {

        blockUI.start();
        $http.delete('api/individual/' + name).then(function (response) {
            blockUI.stop();
            agilaFactory.showSuccess('<span>Individual was deleted successfully!</span>')
            $scope.loadIndividuals();
        }, function (response) {
            blockUI.stop();
            agilaFactory.handleRestError(response);
        });

    };


    $scope.searchFilter = function (individual) {
        if ($scope.option.search.length > 0) {
            regex = new RegExp($scope.option.search.toLowerCase());
            return regex.test(individual.name.toLowerCase());
        }
        else {
            return /.*?/.test(individual.name);
        }
    };


    $scope.loadIndividuals();

}]);