(function() {
    'use strict';

    angular
        .module('laakariApp')
        .controller('LcarteDetailController', LcarteDetailController);

    LcarteDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Lcarte', 'Lpoint', 'Lzone'];

    function LcarteDetailController($scope, $rootScope, $stateParams, previousState, entity, Lcarte, Lpoint, Lzone) {
        var vm = this;

        vm.lcarte = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('laakariApp:lcarteUpdate', function(event, result) {
            vm.lcarte = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
