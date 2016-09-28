(function() {
    'use strict';

    angular
        .module('laakariApp')
        .controller('LregionDetailController', LregionDetailController);

    LregionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Lregion', 'Lzone'];

    function LregionDetailController($scope, $rootScope, $stateParams, previousState, entity, Lregion, Lzone) {
        var vm = this;

        vm.lregion = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('laakariApp:lregionUpdate', function(event, result) {
            vm.lregion = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
