(function() {
    'use strict';

    angular
        .module('laakariApp')
        .controller('LvilleDetailController', LvilleDetailController);

    LvilleDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Lville', 'Lzone'];

    function LvilleDetailController($scope, $rootScope, $stateParams, previousState, entity, Lville, Lzone) {
        var vm = this;

        vm.lville = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('laakariApp:lvilleUpdate', function(event, result) {
            vm.lville = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
