(function() {
    'use strict';

    angular
        .module('laakariApp')
        .controller('LmessageDetailController', LmessageDetailController);

    LmessageDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Lmessage', 'Luser'];

    function LmessageDetailController($scope, $rootScope, $stateParams, previousState, entity, Lmessage, Luser) {
        var vm = this;

        vm.lmessage = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('laakariApp:lmessageUpdate', function(event, result) {
            vm.lmessage = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
