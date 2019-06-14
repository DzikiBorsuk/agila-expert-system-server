mainModule.controller("individualsAddController", ['$rootScope', '$scope', '$http', '$location', '$window', 'agilaFactory', 'blockUI', function ($rootScope, $scope, $http, $location, $window, agilaFactory, blockUI) {

    $scope.objectProperties = [];
    $scope.inputDataProperty = {};
    $scope.inputObjectProperty = {};
    $scope.ranges = {};

    $scope.dataProperties = [];

    $http.get('/api/data-property').then(function (response) {
        $scope.dataProperties = response.data;
    });

    $http.get('/api/object-property').then(function (response) {
        $scope.objectProperties = response.data;

        function getClassesInRanges(data, array) {
            if (!Array.isArray(array)) {
                array = [];
            }
            data.forEach(function (element) {
                array.push(element.text);
                if (element.nodes) {
                    array = getClassesInRanges(element.nodes, array);
                }
            });
            return array;
        }

        function getChildren(element, index, objectProperties) {
            $http.get('/api/class-hierarchy/' + element).then(function (response) {
                let classes = getClassesInRanges(response.data, []);
                objectProperties[index].classes = objectProperties[index].classes.concat(classes);
                if (objectProperties[index].classes.length > 0) {
                    $scope.inputObjectProperty[objectProperties[index].name] = {};
                    $scope.inputObjectProperty[objectProperties[index].name] = objectProperties[index].classes[0];
                }
            });
        }

        function setDefaultValues(objectProperties) {
            for (let property in objectProperties) {
                let value = objectProperties[property].range;
                objectProperties[property].classes = [];
                if (value !== null) {
                    getChildren(value, property, objectProperties);
                } else {
                    setDefaultValues(objectProperties[property].subProperties);
                }
            }
        }

        setDefaultValues($scope.objectProperties);
    });

    $scope.clearAll = function () {
        $scope.inputDataProperty = {};
        $scope.inputObjectProperty = {};
        $scope.inputNewIndividual = "";
        $scope.inputDataPropertyBool = false;
    };

    $scope.submit = function () {
        let individual = {};
        individual.name = $scope.inputNewIndividual;
        let dataProp = [];


        for (let k in $scope.inputDataProperty) {
            let obj = {};
            obj.property = {};
            obj.property.name = k;
            obj.value = $scope.inputDataProperty[k];
            dataProp.push(obj);
        }

        let objectProp = [];
        for (let k in $scope.inputObjectProperty) {
            let obj = {};
            obj.property = {};
            obj.property.name = k;
            obj.value = $scope.inputObjectProperty[k];
            objectProp.push(obj);
        }

        individual.dataProperties = dataProp;
        individual.objectProperties = objectProp;

        let validation=$scope.individualForm.$valid;

        if (validation) {
            $http.post('/api/individual', individual).then(function (response) {
                agilaFactory.showSuccess('Individual <strong>' + individual.name + '</strong> was added successfully!');
            }, agilaFactory.handleRestError);
        }

    };


  

}]);