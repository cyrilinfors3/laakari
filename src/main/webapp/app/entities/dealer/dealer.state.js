(function() {
    'use strict';

    angular
        .module('laakariApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('dealer', {
            parent: 'entity',
            url: '/dealer?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'laakariApp.dealer.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/dealer/dealers.html',
                    controller: 'DealerController',
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
                    $translatePartialLoader.addPart('dealer');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('dealer-detail', {
            parent: 'entity',
            url: '/dealer/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'laakariApp.dealer.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/dealer/dealer-detail.html',
                    controller: 'DealerDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('dealer');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Dealer', function($stateParams, Dealer) {
                    return Dealer.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'dealer',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('dealer-detail.edit', {
            parent: 'dealer-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/dealer/dealer-dialog.html',
                    controller: 'DealerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Dealer', function(Dealer) {
                            return Dealer.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('dealer.new', {
            parent: 'dealer',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/dealer/dealer-dialog.html',
                    controller: 'DealerDialogController',
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
                    $state.go('dealer', null, { reload: 'dealer' });
                }, function() {
                    $state.go('dealer');
                });
            }]
        })
        .state('dealer.edit', {
            parent: 'dealer',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/dealer/dealer-dialog.html',
                    controller: 'DealerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Dealer', function(Dealer) {
                            return Dealer.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('dealer', null, { reload: 'dealer' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('dealer.delete', {
            parent: 'dealer',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/dealer/dealer-delete-dialog.html',
                    controller: 'DealerDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Dealer', function(Dealer) {
                            return Dealer.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('dealer', null, { reload: 'dealer' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
