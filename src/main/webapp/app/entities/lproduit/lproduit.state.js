(function() {
    'use strict';

    angular
        .module('laakariApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('lproduit', {
            parent: 'entity',
            url: '/lproduit?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'laakariApp.lproduit.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/lproduit/lproduits.html',
                    controller: 'LproduitController',
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
                    $translatePartialLoader.addPart('lproduit');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('lproduit-detail', {
            parent: 'entity',
            url: '/lproduit/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'laakariApp.lproduit.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/lproduit/lproduit-detail.html',
                    controller: 'LproduitDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('lproduit');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Lproduit', function($stateParams, Lproduit) {
                    return Lproduit.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'lproduit',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('lproduit-detail.edit', {
            parent: 'lproduit-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lproduit/lproduit-dialog.html',
                    controller: 'LproduitDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Lproduit', function(Lproduit) {
                            return Lproduit.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('lproduit.new', {
            parent: 'lproduit',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lproduit/lproduit-dialog.html',
                    controller: 'LproduitDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                codep: null,
                                libelle: null,
                                prix: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('lproduit', null, { reload: 'lproduit' });
                }, function() {
                    $state.go('lproduit');
                });
            }]
        })
        .state('lproduit.edit', {
            parent: 'lproduit',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lproduit/lproduit-dialog.html',
                    controller: 'LproduitDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Lproduit', function(Lproduit) {
                            return Lproduit.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('lproduit', null, { reload: 'lproduit' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('lproduit.delete', {
            parent: 'lproduit',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lproduit/lproduit-delete-dialog.html',
                    controller: 'LproduitDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Lproduit', function(Lproduit) {
                            return Lproduit.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('lproduit', null, { reload: 'lproduit' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
