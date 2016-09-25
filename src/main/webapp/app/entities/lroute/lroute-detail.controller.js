(function() {
    'use strict';

    angular
        .module('laakariApp')
        .controller('LrouteDetailController', LrouteDetailController);

    LrouteDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Lroute', 'Lcallbox', 'Lzone'];

    function LrouteDetailController($scope, $rootScope, $stateParams, previousState, entity, Lroute, Lcallbox, Lzone) {
        var vm = this;

        vm.lroute = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('laakariApp:lrouteUpdate', function(event, result) {
            vm.lroute = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
