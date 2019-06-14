var authenticationModule = angular.module('authentication', ['shared','ui.router']);

authenticationModule.config(['$stateProvider', '$urlRouterProvider', '$locationProvider', function ($stateProvider, $urlRouterProvider, $locationProvider) {


    $locationProvider.html5Mode(true);
    let states = [
        {
            name: 'login',
            url: '/login',
            templateUrl: '../views/login.html',
            controller: 'loginController'
        },
        {
            name: 'register',
            url: '/register',
            templateUrl: '../views/register.html',
            controller: 'registerController'
        },
        {
            name: 'sign-up',
            url: '/sign-up',
            templateUrl: '../views/register.html',
            controller: 'registerController'
        }

    ];

    $urlRouterProvider.otherwise('/login');

    states.forEach(function (state) {
        $stateProvider.state(state);
    });

}]);

authenticationModule.service('Session', function () {
    this.create = function (data) {
        this.id = data.id;
        this.login = data.login;
        this.firstName = data.firstName;
        this.lastName = data.familyName;
        this.email = data.email;
        this.userRoles = [];
        angular.forEach(data.authorities, function (value, key) {
            this.push(value.name);
        }, this.userRoles);
    };
    this.invalidate = function () {
        this.id = null;
        this.login = null;
        this.firstName = null;
        this.lastName = null;
        this.email = null;
        this.userRoles = null;
    };
    return this;
});
