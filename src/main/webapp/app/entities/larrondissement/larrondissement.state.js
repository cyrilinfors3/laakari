(function() {
    'use strict';

    angular
        .module('laakariApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('larrondissement', {
            parent: 'entity',
            url: '/larrondissement?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'laakariApp.larrondissement.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/larrondissement/larrondissements.html',
                    controller: 'LarrondissementController',
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
                    $translatePartialLoader.addPart('larrondissement');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('larrondissement-detail', {
            parent: 'entity',
            url: '/larrondissement/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'laakariApp.larrondissement.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/larrondissement/larrondissement-detail.html',
                    controller: 'LarrondissementDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('larrondissement');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Larrondissement', function($stateParams, Larrondissement) {
                    return Larrondissement.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'larrondissement',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('larrondissement-detail.edit', {
            parent: 'larrondissement-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/larrondissement/larrondissement-dialog.html',
                    controller: 'LarrondissementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Larrondissement', function(Larrondissement) {
                            return Larrondissement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('larrondissement.new', {
            parent: 'larrondissement',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/larrondissement/larrondissement-dialog.html',
                    controller: 'LarrondissementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nom: null,
                                ville: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('larrondissement', null, { reload: 'larrondissement' });
                }, function() {
                    $state.go('larrondissement');
                });
            }]
        })
        .state('larrondissement.edit', {
            parent: 'larrondissement',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/larrondissement/larrondissement-dialog.html',
                    controller: 'LarrondissementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Larrondissement', function(Larrondissement) {
                            return Larrondissement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('larrondissement', null, { reload: 'larrondissement' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('larrondissement.delete', {
            parent: 'larrondissement',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/larrondissement/larrondissement-delete-dialog.html',
                    controller: 'LarrondissementDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Larrondissement', function(Larrondissement) {
                            return Larrondissement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('larrondissement', null, { reload: 'larrondissement' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
