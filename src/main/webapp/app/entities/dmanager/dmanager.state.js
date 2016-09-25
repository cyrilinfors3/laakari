(function() {
    'use strict';

    angular
        .module('laakariApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('dmanager', {
            parent: 'entity',
            url: '/dmanager?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'laakariApp.dmanager.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/dmanager/dmanagers.html',
                    controller: 'DmanagerController',
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
                    $translatePartialLoader.addPart('dmanager');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('dmanager-detail', {
            parent: 'entity',
            url: '/dmanager/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'laakariApp.dmanager.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/dmanager/dmanager-detail.html',
                    controller: 'DmanagerDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('dmanager');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Dmanager', function($stateParams, Dmanager) {
                    return Dmanager.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'dmanager',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('dmanager-detail.edit', {
            parent: 'dmanager-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/dmanager/dmanager-dialog.html',
                    controller: 'DmanagerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Dmanager', function(Dmanager) {
                            return Dmanager.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('dmanager.new', {
            parent: 'dmanager',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/dmanager/dmanager-dialog.html',
                    controller: 'DmanagerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                tel: null,
                                agentcode: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('dmanager', null, { reload: 'dmanager' });
                }, function() {
                    $state.go('dmanager');
                });
            }]
        })
        .state('dmanager.edit', {
            parent: 'dmanager',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/dmanager/dmanager-dialog.html',
                    controller: 'DmanagerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Dmanager', function(Dmanager) {
                            return Dmanager.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('dmanager', null, { reload: 'dmanager' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('dmanager.delete', {
            parent: 'dmanager',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/dmanager/dmanager-delete-dialog.html',
                    controller: 'DmanagerDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Dmanager', function(Dmanager) {
                            return Dmanager.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('dmanager', null, { reload: 'dmanager' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
