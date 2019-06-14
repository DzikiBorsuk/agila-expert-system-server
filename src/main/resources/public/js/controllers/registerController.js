authenticationModule.controller('registerController', ['$rootScope', '$scope', '$http', '$location', '$window', 'agilaFactory', function ($rootScope, $scope, $http, $location, $window, agilaFactory) {
    $(document).ready(function () {


        $('.movable').on('focus', function () {
            const element = "#" + $(this).attr('id') + "-placeholder";
            $(element).addClass('placeholder-custom-active');
        });


        $('.movable').on('focusout', function () {
            const element = "#" + $(this).attr('id') + "-placeholder";
            if (!$(this).val()) {
                $(element).removeClass('placeholder-custom-active');
            }

        });

        $('[data-toggle="tooltip"]').tooltip(); //tooltip init

        input_check_init();


        var authenticate = function (credentials, callback) {
            $http.post('/api/sign-up', {
                firstName: credentials.firstname,
                lastName: credentials.lastname,
                email: credentials.email,
                password: credentials.password
            })
            .then(function (response) {

                var config = {
                    ignoreAuthModule: 'ignoreAuthModule',
                    headers: {'Content-Type': 'application/x-www-form-urlencoded'}
                };

                $http.post('/authenticate', $.param({
                    username: credentials.email,
                    password: credentials.password
                }), config)
                .then(function (response) {
                    $rootScope.authenticated = true;
                    callback(response);
                })
                .catch(function (response) {
                    $rootScope.authenticated = false;
                    callback(response);
                });

            })
            .catch(function (response) {
                $rootScope.authenticated = false;
                callback(response);
            });
        };

        $scope.credentials = {};
        $scope.registerSubmit = function () {
            authenticate($scope.credentials, function (response) {
                if ($rootScope.authenticated) {
                    $window.location.href = '/';
                    $scope.error = false;
                } else {
                    agilaFactory.handleRestError(response);
                    $scope.error = true;
                }
            });
        };

    });
}]);