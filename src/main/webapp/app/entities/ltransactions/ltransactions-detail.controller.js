(function() {
    'use strict';

    angular
        .module('laakariApp')
        .controller('LtransactionsDetailController', LtransactionsDetailController);

    LtransactionsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Ltransactions', 'Luser', 'Lproduit', 'Lfacture'];

    function LtransactionsDetailController($scope, $rootScope, $stateParams, previousState, entity, Ltransactions, Luser, Lproduit, Lfacture) {
        var vm = this;

        vm.ltransactions = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('laakariApp:ltransactionsUpdate', function(event, result) {
            vm.ltransactions = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
