(function() {
    'use strict';

    angular
        .module('laakariApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('ltransactions', {
            parent: 'entity',
            url: '/ltransactions?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'laakariApp.ltransactions.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/ltransactions/ltransactions.html',
                    controller: 'LtransactionsController',
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
                    $translatePartialLoader.addPart('ltransactions');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('ltransactions-detail', {
            parent: 'entity',
            url: '/ltransactions/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'laakariApp.ltransactions.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/ltransactions/ltransactions-detail.html',
                    controller: 'LtransactionsDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('ltransactions');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Ltransactions', function($stateParams, Ltransactions) {
                    return Ltransactions.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'ltransactions',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('ltransactions-detail.edit', {
            parent: 'ltransactions-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ltransactions/ltransactions-dialog.html',
                    controller: 'LtransactionsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Ltransactions', function(Ltransactions) {
                            return Ltransactions.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('ltransactions.new', {
            parent: 'ltransactions',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ltransactions/ltransactions-dialog.html',
                    controller: 'LtransactionsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                qte: null,
                                datet: null,
                                vendeur: null,
                                acheteur: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('ltransactions', null, { reload: 'ltransactions' });
                }, function() {
                    $state.go('ltransactions');
                });
            }]
        })
        .state('ltransactions.edit', {
            parent: 'ltransactions',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ltransactions/ltransactions-dialog.html',
                    controller: 'LtransactionsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Ltransactions', function(Ltransactions) {
                            return Ltransactions.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('ltransactions', null, { reload: 'ltransactions' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('ltransactions.delete', {
            parent: 'ltransactions',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ltransactions/ltransactions-delete-dialog.html',
                    controller: 'LtransactionsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Ltransactions', function(Ltransactions) {
                            return Ltransactions.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('ltransactions', null, { reload: 'ltransactions' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
