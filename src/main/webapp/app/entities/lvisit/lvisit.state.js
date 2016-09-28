(function() {
    'use strict';

    angular
        .module('laakariApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('lvisit', {
            parent: 'entity',
            url: '/lvisit?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'laakariApp.lvisit.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/lvisit/lvisits.html',
                    controller: 'LvisitController',
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
                    $translatePartialLoader.addPart('lvisit');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('lvisit-detail', {
            parent: 'entity',
            url: '/lvisit/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'laakariApp.lvisit.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/lvisit/lvisit-detail.html',
                    controller: 'LvisitDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('lvisit');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Lvisit', function($stateParams, Lvisit) {
                    return Lvisit.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'lvisit',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('lvisit-detail.edit', {
            parent: 'lvisit-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lvisit/lvisit-dialog.html',
                    controller: 'LvisitDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Lvisit', function(Lvisit) {
                            return Lvisit.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('lvisit.new', {
            parent: 'lvisit',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lvisit/lvisit-dialog.html',
                    controller: 'LvisitDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                date: null,
                                contentv: null,
                                durrationv: null,
                                staff: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('lvisit', null, { reload: 'lvisit' });
                }, function() {
                    $state.go('lvisit');
                });
            }]
        })
        .state('lvisit.edit', {
            parent: 'lvisit',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lvisit/lvisit-dialog.html',
                    controller: 'LvisitDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Lvisit', function(Lvisit) {
                            return Lvisit.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('lvisit', null, { reload: 'lvisit' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('lvisit.delete', {
            parent: 'lvisit',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lvisit/lvisit-delete-dialog.html',
                    controller: 'LvisitDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Lvisit', function(Lvisit) {
                            return Lvisit.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('lvisit', null, { reload: 'lvisit' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
