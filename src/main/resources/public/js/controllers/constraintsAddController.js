mainModule.controller("constraintsAddController", ['$rootScope', '$scope', '$http', '$location', '$window', '$state', 'agilaFactory', 'blockUI', function ($rootScope, $scope, $http, $location, $window, $state, agilaFactory, blockUI) {

    $scope.dataTypes = [];
    $scope.dataPropertiesArray = [];
    $scope.variablesList = new Map();
    $scope.equation = {};
    let variable_valid = true;
    $scope.variable_problems_list = [];
    let tokens = [];
    $scope.unmapped_variables = [];

    $scope.constraint = {
        formula: '',
    };

    $scope.variable = {
        value: {},
        dataProperty: {},
    };


    $http.get('api/data-types').then(function (response) {
        $scope.dataTypes = response.data;
    });

    $http.get('api/data-property').then(function (response) {
        $scope.dataProperties = response.data;
        response.data.forEach(function (element) {
            $scope.dataPropertiesArray.push(element.name);
        });
    });

    function get_unmapped_variable() {
        $scope.unmapped_variables = [];
        let tokens_list = [];
        if (tokens.length > 0) {
            tokens_list = tokens[1];
        }
        for (let i = 0; i < tokens_list.length; ++i) {
            if (tokens_list[i][0] === 'variable') {
                if (!$scope.variablesList.has(tokens_list[i][1])) {
                    if (!$scope.unmapped_variables.includes(tokens_list[i][1])) {
                        $scope.unmapped_variables.push(tokens_list[i][1]);
                    }
                }
            }
        }
    }

    $scope.$watch('variable', function () {


        $scope.variablesList = new Map();

        variable_valid = true;
        for (let id in $scope.variable.value) {
            if ($scope.variable.value[id] !== '') {
                if ($scope.variable.dataProperty.hasOwnProperty(id)) {
                    let variable = $scope.variable.value[id];
                    let data_property = $scope.variable.dataProperty[id].name;
                    if ($scope.variablesList.has(variable)) {
                        variable_valid = false;
                        if (!$scope.variable_problems_list.includes(variable)) {
                            $scope.variable_problems_list.push(variable);
                        }
                    }
                    $scope.variablesList.set(variable, data_property)
                }
            }
        }
        get_unmapped_variable();
    }, true);

    $scope.$watch('constraint.formula', function () {
        let math = document.getElementById("visualization");

        if ($scope.constraintForm.formula.$valid) {
            tokens = getTokens($scope.constraint.formula);
            get_unmapped_variable();
            math.innerHTML = '$$' + $scope.constraint.formula + '$$';
        }
        else {
            $scope.unmapped_variables = [];
            tokens = [];
            math.innerHTML = '';
        }


        MathJax.Hub.Queue(["Typeset", MathJax.Hub, math]);
    });

    $scope.mathBtnClick = function (event) {
        let str = '';
        let id = event.target.id;
        switch (id) {
            case 'addition-btn':
                str = '() + ()';
                break;
            case 'subtraction-btn':
                str = '() - ()';
                break;
            case 'multiplication-btn':
                str = '() \\times ()';
                break;
            case 'division-btn':
                str = '\\frac{}{}';
                break;
            case 'root-btn':
                str = '\\sqrt[]{}';
                break;
            case 'exponentiation-btn':
                str = '{}^{}';
                break;
            case 'less-than-btn':
                str = '{}<{}';
                break;
            case 'greater-than-btn':
                str = '{}>{}';
                break;
            case 'less-or-equal-than-btn':
                str = '{}\\leqslant{}';
                break;
            case 'greater-or-equal-than-btn':
                str = '{}\\geqslant{}';
                break;
        }
        let formula = $scope.constraint.formula;
        if (formula.length > 0 && formula.charAt(formula.length - 1) !== ' ') {
            formula += ' ';
        }
        $scope.constraint.formula = formula + str;
    };

    $scope.constraintSubmit = function () {
        if (variable_valid) {
            let valid_formula = false;


            if ($scope.constraintForm.$valid) {
                let error_description = '';
                if (tokens[0]) {
                    let syntax = checkSyntax(tokens[1]);
                    if (syntax[0]) {
                        let map_tokens = mapVariables(tokens[1], $scope.variablesList, $scope.dataPropertiesArray);
                        if (map_tokens[0]) {
                            valid_formula = true;
                            let norm_tokens = normalize(map_tokens[1]);
                            let rpn = reversePolishNotation(norm_tokens);
                            $scope.equation = convertoToJson(rpn);
                        }
                        else {
                            valid_formula = false;
                            let problems_list = map_tokens[1];
                            if (problems_list.length === 1) {
                                error_description = 'Variable ' + '<b>' + problems_list[0] + '</b>' + ' is not mapped!';
                            }
                            else {
                                error_description = 'Variables:<br>';
                                problems_list.forEach(function (element) {
                                    error_description += '<b>' + element + '</b>, ';
                                });
                                error_description += '<br>are not mapped!'
                            }
                            agilaFactory.showError(error_description);
                        }
                    }
                    else {
                        valid_formula = false;
                        error_description = syntax[1];
                        agilaFactory.showError('Syntax error:<br>' + error_description);
                    }
                }
                else {
                    valid_formula = false;
                    error_description = '<b>' + tokens[1] + '</b>' + ' is not recognized or not supported!';
                    agilaFactory.showError(error_description);
                }
            }

            if (valid_formula) {

                blockUI.start();

                let json = {};
                json['name'] = $scope.constraint.name;
                json['output'] = $scope.constraint.output;
                json['description'] = $scope.constraint.description;
                json['formula'] = $scope.constraint.formula;
                json['equation'] = $scope.equation;
                json['dataType'] = $scope.constraint.dataType;

                $http.post('/api/constraint', json).then(
                    function () {
                        blockUI.stop();
                        agilaFactory.showSuccess('<span>You have successfully added constraint!</span>', function () {

                        });
                        $state.transitionTo('constraintsList');
                    }, agilaFactory.handleRestError);
            }
        }
        else {
            let error_description = '';
            if ($scope.variable_problems_list.length === 1) {
                error_description = 'Variable ' + '<b>' + $scope.variable_problems_list[0] + '</b>' + ' is declared more than once!';
            }
            else {
                error_description = 'Variables:<br>';
                $scope.variable_problems_list.forEach(function (element) {
                    error_description += '<b>' + element + '</b>, ';
                });
                error_description += '<br>are declared more than once!'
            }
            $('#error-description').html(error_description);
            agilaFactory.showError(error_description);
        }
    };

}
])
;