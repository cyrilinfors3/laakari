(function() {
    'use strict';

    angular
        .module('laakariApp')
        .controller('LuserDetailController', LuserDetailController);

    LuserDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Luser', 'Dmanager', 'Dealer', 'Sdealer', 'Delegue', 'Lzone'];

    function LuserDetailController($scope, $rootScope, $stateParams, previousState, entity, Luser, Dmanager, Dealer, Sdealer, Delegue, Lzone) {
        var vm = this;

        vm.luser = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('laakariApp:luserUpdate', function(event, result) {
            vm.luser = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
