(function() {
    'use strict';

    angular
        .module('laakariApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('lcallbox', {
            parent: 'entity',
            url: '/lcallbox?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'laakariApp.lcallbox.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/lcallbox/lcallboxes.html',
                    controller: 'LcallboxController',
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
                    $translatePartialLoader.addPart('lcallbox');
                    $translatePartialLoader.addPart('sex');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('lcallbox-detail', {
            parent: 'entity',
            url: '/lcallbox/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'laakariApp.lcallbox.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/lcallbox/lcallbox-detail.html',
                    controller: 'LcallboxDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('lcallbox');
                    $translatePartialLoader.addPart('sex');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Lcallbox', function($stateParams, Lcallbox) {
                    return Lcallbox.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'lcallbox',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('lcallbox-detail.edit', {
            parent: 'lcallbox-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lcallbox/lcallbox-dialog.html',
                    controller: 'LcallboxDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Lcallbox', function(Lcallbox) {
                            return Lcallbox.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('lcallbox.new', {
            parent: 'lcallbox',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lcallbox/lcallbox-dialog.html',
                    controller: 'LcallboxDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nom: null,
                                prenom: null,
                                dateofbirth: null,
                                quatier: null,
                                mastersim: null,
                                sex: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('lcallbox', null, { reload: 'lcallbox' });
                }, function() {
                    $state.go('lcallbox');
                });
            }]
        })
        .state('lcallbox.edit', {
            parent: 'lcallbox',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lcallbox/lcallbox-dialog.html',
                    controller: 'LcallboxDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Lcallbox', function(Lcallbox) {
                            return Lcallbox.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('lcallbox', null, { reload: 'lcallbox' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('lcallbox.delete', {
            parent: 'lcallbox',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lcallbox/lcallbox-delete-dialog.html',
                    controller: 'LcallboxDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Lcallbox', function(Lcallbox) {
                            return Lcallbox.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('lcallbox', null, { reload: 'lcallbox' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
