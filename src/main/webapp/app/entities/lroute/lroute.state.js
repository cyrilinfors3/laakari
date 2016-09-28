(function() {
    'use strict';

    angular
        .module('laakariApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('lroute', {
            parent: 'entity',
            url: '/lroute?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'laakariApp.lroute.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/lroute/lroutes.html',
                    controller: 'LrouteController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('lroute');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('lroute-detail', {
            parent: 'entity',
            url: '/lroute/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'laakariApp.lroute.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/lroute/lroute-detail.html',
                    controller: 'LrouteDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('lroute');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Lroute', function($stateParams, Lroute) {
                    return Lroute.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'lroute',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('lroute-detail.edit', {
            parent: 'lroute-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lroute/lroute-dialog.html',
                    controller: 'LrouteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Lroute', function(Lroute) {
                            return Lroute.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('lroute.new', {
            parent: 'lroute',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lroute/lroute-dialog.html',
                    controller: 'LrouteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nom: null,
                                quatier: null,
                                ville: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('lroute', null, { reload: 'lroute' });
                }, function() {
                    $state.go('lroute');
                });
            }]
        })
        .state('lroute.edit', {
            parent: 'lroute',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lroute/lroute-dialog.html',
                    controller: 'LrouteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Lroute', function(Lroute) {
                            return Lroute.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('lroute', null, { reload: 'lroute' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('lroute.delete', {
            parent: 'lroute',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lroute/lroute-delete-dialog.html',
                    controller: 'LrouteDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Lroute', function(Lroute) {
                            return Lroute.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('lroute', null, { reload: 'lroute' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
