(function() {
    'use strict';

    angular
        .module('laakariApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('lcall', {
            parent: 'entity',
            url: '/lcall?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'laakariApp.lcall.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/lcall/lcalls.html',
                    controller: 'LcallController',
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
                    $translatePartialLoader.addPart('lcall');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('lcall-detail', {
            parent: 'entity',
            url: '/lcall/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'laakariApp.lcall.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/lcall/lcall-detail.html',
                    controller: 'LcallDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('lcall');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Lcall', function($stateParams, Lcall) {
                    return Lcall.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'lcall',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('lcall-detail.edit', {
            parent: 'lcall-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lcall/lcall-dialog.html',
                    controller: 'LcallDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Lcall', function(Lcall) {
                            return Lcall.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('lcall.new', {
            parent: 'lcall',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lcall/lcall-dialog.html',
                    controller: 'LcallDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                emiteur: null,
                                recepteur: null,
                                calltime: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('lcall', null, { reload: 'lcall' });
                }, function() {
                    $state.go('lcall');
                });
            }]
        })
        .state('lcall.edit', {
            parent: 'lcall',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lcall/lcall-dialog.html',
                    controller: 'LcallDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Lcall', function(Lcall) {
                            return Lcall.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('lcall', null, { reload: 'lcall' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('lcall.delete', {
            parent: 'lcall',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lcall/lcall-delete-dialog.html',
                    controller: 'LcallDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Lcall', function(Lcall) {
                            return Lcall.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('lcall', null, { reload: 'lcall' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
