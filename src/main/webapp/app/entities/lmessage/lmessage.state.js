(function() {
    'use strict';

    angular
        .module('laakariApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('lmessage', {
            parent: 'entity',
            url: '/lmessage?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'laakariApp.lmessage.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/lmessage/lmessages.html',
                    controller: 'LmessageController',
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
                    $translatePartialLoader.addPart('lmessage');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('lmessage-detail', {
            parent: 'entity',
            url: '/lmessage/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'laakariApp.lmessage.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/lmessage/lmessage-detail.html',
                    controller: 'LmessageDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('lmessage');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Lmessage', function($stateParams, Lmessage) {
                    return Lmessage.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'lmessage',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('lmessage-detail.edit', {
            parent: 'lmessage-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lmessage/lmessage-dialog.html',
                    controller: 'LmessageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Lmessage', function(Lmessage) {
                            return Lmessage.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('lmessage.new', {
            parent: 'lmessage',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lmessage/lmessage-dialog.html',
                    controller: 'LmessageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                sentdate: null,
                                msgcontent: null,
                                senthours: null,
                                sender: null,
                                reciever: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('lmessage', null, { reload: 'lmessage' });
                }, function() {
                    $state.go('lmessage');
                });
            }]
        })
        .state('lmessage.edit', {
            parent: 'lmessage',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lmessage/lmessage-dialog.html',
                    controller: 'LmessageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Lmessage', function(Lmessage) {
                            return Lmessage.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('lmessage', null, { reload: 'lmessage' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('lmessage.delete', {
            parent: 'lmessage',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lmessage/lmessage-delete-dialog.html',
                    controller: 'LmessageDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Lmessage', function(Lmessage) {
                            return Lmessage.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('lmessage', null, { reload: 'lmessage' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
