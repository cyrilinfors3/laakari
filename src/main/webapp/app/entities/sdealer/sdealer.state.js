(function() {
    'use strict';

    angular
        .module('laakariApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('sdealer', {
            parent: 'entity',
            url: '/sdealer?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'laakariApp.sdealer.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/sdealer/sdealers.html',
                    controller: 'SdealerController',
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
                    $translatePartialLoader.addPart('sdealer');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('sdealer-detail', {
            parent: 'entity',
            url: '/sdealer/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'laakariApp.sdealer.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/sdealer/sdealer-detail.html',
                    controller: 'SdealerDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('sdealer');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Sdealer', function($stateParams, Sdealer) {
                    return Sdealer.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'sdealer',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('sdealer-detail.edit', {
            parent: 'sdealer-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sdealer/sdealer-dialog.html',
                    controller: 'SdealerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Sdealer', function(Sdealer) {
                            return Sdealer.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('sdealer.new', {
            parent: 'sdealer',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sdealer/sdealer-dialog.html',
                    controller: 'SdealerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                shopcode: null,
                                sigle: null,
                                mastersim: null,
                                logo: null,
                                logoContentType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('sdealer', null, { reload: 'sdealer' });
                }, function() {
                    $state.go('sdealer');
                });
            }]
        })
        .state('sdealer.edit', {
            parent: 'sdealer',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sdealer/sdealer-dialog.html',
                    controller: 'SdealerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Sdealer', function(Sdealer) {
                            return Sdealer.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('sdealer', null, { reload: 'sdealer' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('sdealer.delete', {
            parent: 'sdealer',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sdealer/sdealer-delete-dialog.html',
                    controller: 'SdealerDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Sdealer', function(Sdealer) {
                            return Sdealer.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('sdealer', null, { reload: 'sdealer' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
