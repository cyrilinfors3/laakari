(function() {
    'use strict';

    angular
        .module('laakariApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('delegue', {
            parent: 'entity',
            url: '/delegue?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'laakariApp.delegue.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/delegue/delegues.html',
                    controller: 'DelegueController',
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
                    $translatePartialLoader.addPart('delegue');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('delegue-detail', {
            parent: 'entity',
            url: '/delegue/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'laakariApp.delegue.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/delegue/delegue-detail.html',
                    controller: 'DelegueDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('delegue');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Delegue', function($stateParams, Delegue) {
                    return Delegue.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'delegue',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('delegue-detail.edit', {
            parent: 'delegue-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/delegue/delegue-dialog.html',
                    controller: 'DelegueDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Delegue', function(Delegue) {
                            return Delegue.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('delegue.new', {
            parent: 'delegue',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/delegue/delegue-dialog.html',
                    controller: 'DelegueDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                code: null,
                                mastersim: null,
                                numid: null,
                                issuedate: null,
                                dateofbirth: null,
                                quatier: null,
                                sex: null,
                                pic: null,
                                picContentType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('delegue', null, { reload: 'delegue' });
                }, function() {
                    $state.go('delegue');
                });
            }]
        })
        .state('delegue.edit', {
            parent: 'delegue',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/delegue/delegue-dialog.html',
                    controller: 'DelegueDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Delegue', function(Delegue) {
                            return Delegue.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('delegue', null, { reload: 'delegue' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('delegue.delete', {
            parent: 'delegue',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/delegue/delegue-delete-dialog.html',
                    controller: 'DelegueDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Delegue', function(Delegue) {
                            return Delegue.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('delegue', null, { reload: 'delegue' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
