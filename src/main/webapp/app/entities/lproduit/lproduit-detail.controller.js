(function() {
    'use strict';

    angular
        .module('laakariApp')
        .controller('LproduitDetailController', LproduitDetailController);

    LproduitDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Lproduit', 'Ltransactions'];

    function LproduitDetailController($scope, $rootScope, $stateParams, previousState, entity, Lproduit, Ltransactions) {
        var vm = this;

        vm.lproduit = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('laakariApp:lproduitUpdate', function(event, result) {
            vm.lproduit = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
