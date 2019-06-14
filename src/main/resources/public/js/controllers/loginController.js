authenticationModule.controller('loginController', ['$rootScope', '$scope', '$http', '$location', '$window', 'agilaFactory', function ($rootScope, $scope, $http, $location, $window, agilaFactory) {
    $(document).ready(function () {

        $(document).keydown(function (e) {
            if (e.key === 'Enter') {
                $("#next-email").click();
            }
        });

        $("#back").click(function () {
            $("#email").addClass("d-print-none").removeClass("d-none");
            $("#password").addClass("d-none");
            $('#next-password').prop('disabled', true);
        });

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

        $("#next-email").click(function () {
            const email = $("#login-email").val();

            if (!/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(email)) {
                $("#email-alert").removeClass("d-none");
                $("#email").removeClass("d-none");
                $("#password").addClass("d-none");
            } else {
                $('#next-password').prop('disabled', false);
                $("#email-alert").empty();
                $("#email-alert").addClass("d-none");
                $("#email").removeClass("d-print-none").addClass("d-none");
                $("#password").removeClass("d-none");
                const email = $("#login-email").val();
                $("#email-back").text(email);
            }
        });


        var authenticate = function (credentials, callback) {
            var config = {
                ignoreAuthModule: 'ignoreAuthModule',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            };

            $http.post('/authenticate', $.param({
                username: credentials.username,
                password: credentials.password,
                rememberMe: credentials.rememberMe
            }), config)
            .then(function (response) {
                $rootScope.authenticated = true;
                callback(response);
            }, function (response) {
                $rootScope.authenticated = false;
                callback(response);
            });
        };

        $scope.credentials = {};

        $scope.loginSubmit = function () {
            authenticate($scope.credentials, function (response) {
                if ($rootScope.authenticated) {
                    $window.location.href = '/';
                    //alert('ok');
                    $scope.error = false;
                } else {
                    $scope.error = true;
                    //alert('bad');
                    agilaFactory.handleRestError(response);
                }
            });
        };

    });
}]);
