(function() {
    'use strict';

    angular
        .module('laakariApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('lpoint', {
            parent: 'entity',
            url: '/lpoint?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'laakariApp.lpoint.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/lpoint/lpoints.html',
                    controller: 'LpointController',
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
                    $translatePartialLoader.addPart('lpoint');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('lpoint-detail', {
            parent: 'entity',
            url: '/lpoint/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'laakariApp.lpoint.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/lpoint/lpoint-detail.html',
                    controller: 'LpointDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('lpoint');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Lpoint', function($stateParams, Lpoint) {
                    return Lpoint.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'lpoint',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('lpoint-detail.edit', {
            parent: 'lpoint-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lpoint/lpoint-dialog.html',
                    controller: 'LpointDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Lpoint', function(Lpoint) {
                            return Lpoint.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('lpoint.new', {
            parent: 'lpoint',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lpoint/lpoint-dialog.html',
                    controller: 'LpointDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                lati: null,
                                longi: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('lpoint', null, { reload: 'lpoint' });
                }, function() {
                    $state.go('lpoint');
                });
            }]
        })
        .state('lpoint.edit', {
            parent: 'lpoint',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lpoint/lpoint-dialog.html',
                    controller: 'LpointDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Lpoint', function(Lpoint) {
                            return Lpoint.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('lpoint', null, { reload: 'lpoint' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('lpoint.delete', {
            parent: 'lpoint',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lpoint/lpoint-delete-dialog.html',
                    controller: 'LpointDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Lpoint', function(Lpoint) {
                            return Lpoint.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('lpoint', null, { reload: 'lpoint' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
