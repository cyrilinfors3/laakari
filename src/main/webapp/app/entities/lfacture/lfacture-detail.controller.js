(function() {
    'use strict';

    angular
        .module('laakariApp')
        .controller('LfactureDetailController', LfactureDetailController);

    LfactureDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Lfacture', 'Ltransactions'];

    function LfactureDetailController($scope, $rootScope, $stateParams, previousState, entity, Lfacture, Ltransactions) {
        var vm = this;

        vm.lfacture = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('laakariApp:lfactureUpdate', function(event, result) {
            vm.lfacture = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
