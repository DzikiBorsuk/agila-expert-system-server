mainModule.directive('numberOnly', function () {
    return {
        require: 'ngModel',
        restrict: 'A',
        scope: {
            numberOnly: '@',
        },
        link: function (scope, element, attr, ctrl) {
            function inputValue(val) {
                if (val) {
                    let regex;
                    let parseFunction = null;
                    if (attr.numberOnly=='float') {
                        regex = /^[+-]?([0]{1}|(([0][.]){1}[0-9]*)|([1-9]{1}[0-9]*[.]?[0-9]*))$/g;
                        parseFunction = parseFloat;
                    } else if(attr.numberOnly=='integer') {
                        regex = /^(0|[-+]?[1-9]\d*)$/g;
                        parseFunction = parseInt;
                    }

                    if (regex.test(val)) {
                        return parseFunction(val, 10);
                    } else if (val === '-') {
                        return val;
                    } else if (val.length > 0) {
                        val = val.substring(0, val.length - 1);
                        ctrl.$setViewValue(val);
                        ctrl.$render();
                        return parseFunction(val, 10);
                    }
                }
                return undefined;
            }

            ctrl.$parsers.push(inputValue);
        }
    };
});

mainModule.directive('disableSpaces', function() {
    return {
        restrict: 'A',

        link: function($scope, $element) {
            $element.bind('input', function() {
                $(this).val($(this).val().replace(/ /g, ''));
            });
        }
    };
});

mainModule.directive('fileModel', ['$parse', function ($parse) {
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            var model = $parse(attrs.fileModel);
            var modelSetter = model.assign;
            element.bind('change', function(){
                scope.$apply(function(){
                    modelSetter(scope, element[0].files[0]);
                });
            });
        }
    };
}]);

mainModule.service('fileUpload', ['$http', 'agilaFactory', function ($http, agilaFactory) {
    this.uploadFileToUrl = function(file, uploadUrl){
        var fd = new FormData();
        fd.append('file', file);
        $http.post(uploadUrl, fd, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        }).then(function(){
            agilaFactory.showSuccess('Ontology upload is complete!');
        });
    };
}]);

mainModule.directive('variableForm', function ($compile) {
    return {
        restrict: 'E',

        transclude: true,

        scope: true,

        template: '<div>' +
            '<div class="variables"></div>' +
            '<input type="button" class="btn btn-secondary" value="Add new variable" name="Add new variable" ng-click="getData()">' +
            '</div>',


        link: function ($scope, element, attrs) {

            $scope.getData = function () {
                $newDirective = angular.element('<variable-item ></variable-item>');
                $newDirective.attr('ng-model', 'variable');
                $newDirective.attr('options', attrs.options);
                element.children('div').children('.variables').append($newDirective);
                $compile($newDirective)($scope);
            }
        }
    };
});

mainModule.directive('variableItem', function ($compile) {
    return {
        scope: {
            variable: '=ngModel',
            dataProperties: '=options'
        },
        restrict: 'E',
        replace: true,

        templateUrl: '../../views/constraints/variables_template.html',

        link: function ($scope, element, attrs) {
            $scope.index = Math.round(Math.random() * 100000000);
            $scope.remove = function () {
                delete $scope.variable.dataProperty[$scope.index];
                delete $scope.variable.value[$scope.index];
                element.remove();
            }
        }
    }
});

mainModule.directive('autocomplete', function ($compile) {
    return {
        scope: {
            hintList: '=hintList',
        },
        restrict: 'E',
        replace: true,

        link: function ($scope, element, attrs) {

            let currentFocus = -1;


            let closeAllLists = function () {

                angular.element(".autocomplete-items").remove();
            };

            let removeActive = function (x) {
                /*a function to remove the "active" class from all autocomplete items:*/
                x.removeClass("autocomplete-active");
            };

            let addActive = function (x) {
                /*a function to classify an item as "active":*/
                if (!x) return false;
                if (x.length === 0) return false;
                /*start by removing the "active" class on all items:*/
                removeActive(x);
                if (currentFocus >= x.length) currentFocus = 0;
                if (currentFocus < 0) currentFocus = (x.length - 1);
                /*add class "autocomplete-active":*/
                x[currentFocus].classList.add("autocomplete-active");
            };


            element.on('focusin input click', 'input', function (event) {
                angular.element(this).addClass('autocomplete-input');
                currentFocus = -1;
                closeAllLists();
                let value = angular.element(this).val();
                let el = angular.element('<div></div>');
                el.attr("id", angular.element(this).attr('id') + "autocomplete-list");
                el.attr("class", "autocomplete-items");
                let x = angular.element(this).parent();
                x.append(el);
                x = x.children('input');
                $scope.hintList.forEach(function (str) {
                    if (str.substr(0, value.length).toUpperCase() === value.toUpperCase()) {
                        /*create a DIV element for each matching element:*/
                        let b = angular.element('<div></div>');
                        /*make the matching letters bold:*/
                        b.html("<strong>" + str.substr(0, value.length) + "</strong>");
                        b.html(b.html() + str.substr(value.length));
                        /*insert a input field that will hold the current array item's value:*/
                        b.html(b.html() + "<input type='hidden' value='" + str + "'>");
                        /*execute a function when someone clicks on the item value (DIV element):*/
                        b.addClass('autocomplete-dropdown');
                        b.on("click", function (event) {
                            /*insert the value for the autocomplete text field:*/
                            x.val(angular.element(this).children('input').val()); //check if ok
                            x.triggerHandler('change');
                            /*close the list of autocompleted values,
                            (or any other open lists of autocompleted values:*/
                            closeAllLists();
                        });
                        el.append(b);
                    }
                });
            });

            angular.element(document).on('click', function (event) {
                if (!angular.element(event.target).hasClass('autocomplete-dropdown') && !angular.element(event.target).hasClass('autocomplete-input')) {
                    closeAllLists();
                }
            });

            element.on('keydown', 'input', function (event) {
                let element = angular.element('#' + angular.element(this).attr('id') + 'autocomplete-list');
                if (element) element = element.children("div");
                if (event.which == 40) {
                    /*If the arrow DOWN key is pressed,
                    increase the currentFocus variable:*/
                    currentFocus++;
                    /*and and make the current item more visible:*/
                    addActive(element);
                } else if (event.which == 38) { //up
                    /*If the arrow UP key is pressed,
                    decrease the currentFocus variable:*/
                    currentFocus--;
                    /*and and make the current item more visible:*/
                    addActive(element);
                } else if (event.which == 13) {
                    /*If the ENTER key is pressed, prevent the form from being submitted,*/
                    event.preventDefault();
                    if (currentFocus > -1) {
                        /*and simulate a click on the "active" item:*/
                        if (element) element[currentFocus].click();
                    }
                }
            })
        }

    }
});