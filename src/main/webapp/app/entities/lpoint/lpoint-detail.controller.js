(function() {
    'use strict';

    angular
        .module('laakariApp')
        .controller('LpointDetailController', LpointDetailController);

    LpointDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Lpoint', 'Lcarte', 'Lcallbox'];

    function LpointDetailController($scope, $rootScope, $stateParams, previousState, entity, Lpoint, Lcarte, Lcallbox) {
        var vm = this;

        vm.lpoint = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('laakariApp:lpointUpdate', function(event, result) {
            vm.lpoint = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
