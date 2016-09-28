(function() {
    'use strict';

    angular
        .module('laakariApp')
        .controller('LvisitDetailController', LvisitDetailController);

    LvisitDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Lvisit', 'Lcallbox'];

    function LvisitDetailController($scope, $rootScope, $stateParams, previousState, entity, Lvisit, Lcallbox) {
        var vm = this;

        vm.lvisit = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('laakariApp:lvisitUpdate', function(event, result) {
            vm.lvisit = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
