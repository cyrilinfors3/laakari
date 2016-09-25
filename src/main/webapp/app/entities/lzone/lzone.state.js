(function() {
    'use strict';

    angular
        .module('laakariApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('lzone', {
            parent: 'entity',
            url: '/lzone?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'laakariApp.lzone.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/lzone/lzones.html',
                    controller: 'LzoneController',
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
                    $translatePartialLoader.addPart('lzone');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('lzone-detail', {
            parent: 'entity',
            url: '/lzone/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'laakariApp.lzone.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/lzone/lzone-detail.html',
                    controller: 'LzoneDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('lzone');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Lzone', function($stateParams, Lzone) {
                    return Lzone.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'lzone',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('lzone-detail.edit', {
            parent: 'lzone-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lzone/lzone-dialog.html',
                    controller: 'LzoneDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Lzone', function(Lzone) {
                            return Lzone.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('lzone.new', {
            parent: 'lzone',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lzone/lzone-dialog.html',
                    controller: 'LzoneDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                datecreation: null,
                                datemodif: null,
                                type: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('lzone', null, { reload: 'lzone' });
                }, function() {
                    $state.go('lzone');
                });
            }]
        })
        .state('lzone.edit', {
            parent: 'lzone',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lzone/lzone-dialog.html',
                    controller: 'LzoneDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Lzone', function(Lzone) {
                            return Lzone.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('lzone', null, { reload: 'lzone' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('lzone.delete', {
            parent: 'lzone',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lzone/lzone-delete-dialog.html',
                    controller: 'LzoneDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Lzone', function(Lzone) {
                            return Lzone.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('lzone', null, { reload: 'lzone' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
