(function() {
    'use strict';

    angular
        .module('laakariApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('lfacture', {
            parent: 'entity',
            url: '/lfacture?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'laakariApp.lfacture.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/lfacture/lfactures.html',
                    controller: 'LfactureController',
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
                    $translatePartialLoader.addPart('lfacture');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('lfacture-detail', {
            parent: 'entity',
            url: '/lfacture/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'laakariApp.lfacture.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/lfacture/lfacture-detail.html',
                    controller: 'LfactureDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('lfacture');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Lfacture', function($stateParams, Lfacture) {
                    return Lfacture.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'lfacture',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('lfacture-detail.edit', {
            parent: 'lfacture-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lfacture/lfacture-dialog.html',
                    controller: 'LfactureDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Lfacture', function(Lfacture) {
                            return Lfacture.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('lfacture.new', {
            parent: 'lfacture',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lfacture/lfacture-dialog.html',
                    controller: 'LfactureDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                codebill: null,
                                total: null,
                                fdate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('lfacture', null, { reload: 'lfacture' });
                }, function() {
                    $state.go('lfacture');
                });
            }]
        })
        .state('lfacture.edit', {
            parent: 'lfacture',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lfacture/lfacture-dialog.html',
                    controller: 'LfactureDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Lfacture', function(Lfacture) {
                            return Lfacture.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('lfacture', null, { reload: 'lfacture' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('lfacture.delete', {
            parent: 'lfacture',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lfacture/lfacture-delete-dialog.html',
                    controller: 'LfactureDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Lfacture', function(Lfacture) {
                            return Lfacture.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('lfacture', null, { reload: 'lfacture' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
