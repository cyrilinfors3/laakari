(function() {
    'use strict';

    angular
        .module('laakariApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('home', {
            parent: 'app',
            url: '/',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/home/home.html',
                    controller: 'HomeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('home');
                    return $translate.refresh();
                }]
            }
        }).state('login', {
            parent: 'app',
            url: '/login',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/components/login/login.html',
                    controller: 'LoginController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('login');
                    return $translate.refresh();
                }]
            }
        }).state('accm', {
            parent: 'app',
            url: '/accm',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/modulee/accm/accm.html',
                    controller: 'AccmController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('accm');
                    return $translate.refresh();
                }]
            }
        }).state('newuserdm', {
            parent: 'app',
            url: '/newuserdm',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/modulee/newuserdm/newuserdm.html',
                    controller: 'NewuserdmController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('newuserdm');
                    return $translate.refresh();
                }]
            }
        }).state('newuserdmsc', {
            parent: 'app',
            url: '/newuserdmsc',
            params: {
                codep: '44'
            },
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/modulee/newuserdmsc/newuserdmsc.html',
                    controller: 'NewuserdmscController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('newuserdmsc');
                    return $translate.refresh();
                }]
            }
        }).state('newuserd', {
            parent: 'app',
            url: '/newuserd',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/modulee/newuserd/newuserd.html',
                    controller: 'NewuserdController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('newuserd');
                    return $translate.refresh();
                }]
            }
        });
    }
})();