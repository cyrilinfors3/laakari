(function() {
    'use strict';

    angular
        .module('laakariApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('lprofil', {
            parent: 'entity',
            url: '/lprofil?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'laakariApp.lprofil.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/lprofil/lprofils.html',
                    controller: 'LprofilController',
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
                    $translatePartialLoader.addPart('lprofil');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('lprofil-detail', {
            parent: 'entity',
            url: '/lprofil/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'laakariApp.lprofil.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/lprofil/lprofil-detail.html',
                    controller: 'LprofilDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('lprofil');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Lprofil', function($stateParams, Lprofil) {
                    return Lprofil.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'lprofil',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('lprofil-detail.edit', {
            parent: 'lprofil-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lprofil/lprofil-dialog.html',
                    controller: 'LprofilDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Lprofil', function(Lprofil) {
                            return Lprofil.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('lprofil.new', {
            parent: 'lprofil',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lprofil/lprofil-dialog.html',
                    controller: 'LprofilDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                login: null,
                                pass: null,
                                tel: null,
                                pic: null,
                                picContentType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('lprofil', null, { reload: 'lprofil' });
                }, function() {
                    $state.go('lprofil');
                });
            }]
        })
        .state('lprofil.edit', {
            parent: 'lprofil',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lprofil/lprofil-dialog.html',
                    controller: 'LprofilDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Lprofil', function(Lprofil) {
                            return Lprofil.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('lprofil', null, { reload: 'lprofil' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('lprofil.delete', {
            parent: 'lprofil',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lprofil/lprofil-delete-dialog.html',
                    controller: 'LprofilDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Lprofil', function(Lprofil) {
                            return Lprofil.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('lprofil', null, { reload: 'lprofil' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
