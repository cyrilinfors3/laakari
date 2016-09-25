(function() {
    'use strict';

    angular
        .module('laakariApp')
        .controller('LcallDetailController', LcallDetailController);

    LcallDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Lcall', 'Luser'];

    function LcallDetailController($scope, $rootScope, $stateParams, previousState, entity, Lcall, Luser) {
        var vm = this;

        vm.lcall = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('laakariApp:lcallUpdate', function(event, result) {
            vm.lcall = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
