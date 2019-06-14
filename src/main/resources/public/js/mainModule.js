var mainModule = angular.module('agila', ['shared','ui.router']);

mainModule.config(['$stateProvider', '$urlRouterProvider', function ($stateProvider, $urlRouterProvider) {

    //$locationProvider.html5Mode(true);

    let states = [
        {
            name: 'dashboard',
            url: '/',
            templateUrl: '../views/dashboard.html',
            controller: 'dashboardController'
        },
        {
            name: 'constraintsList',
            url: '/constraints',
            templateUrl: '../views/constraints/constraintsList.html',
            controller: 'constraintsListController'
        },
        {
            name: 'constraintsAdd',
            url: '/constraints/add',
            templateUrl: '../views/constraints/constraintsAdd.html',
            controller: 'constraintsAddController',
        },
        {
            name: 'rulesList',
            url: '/rules',
            templateUrl: '../views/rules/rulesList.html',
            controller: 'rulesListController'
        },
        {
            name: 'rulesAdd',
            url: '/rules/add',
            templateUrl: '../views/rules/rulesAdd.html',
            controller: 'rulesAddController'
        },
        {
            name:'individualsAdd',
            url:'/individuals/add',
            templateUrl:'../views/individuals/individualsAdd.html',
            controller:'individualsAddController'

        },
        {
            name:'individualsList',
            url:'/individuals/list',
            templateUrl:'../views/individuals/individualsList.html',
            controller:'individualsListController'

        },
        {
            name: 'individualDetails',
            url: '/individual/:id',
            templateUrl: '../views/individuals/details.html',
            controller: 'individualDetailsController',
            resolve:{
                id: ['$stateParams', function($stateParams){
                    return $stateParams.id;
                }]
            }
        },
        {
            name: 'settings',
            url: '/settings',
            templateUrl: '../views/settings/index.html',
            controller: 'settingsController'
        },
    ];

    $urlRouterProvider.otherwise('/');

    states.forEach(function (state) {
        $stateProvider.state(state);
    });

}]);
