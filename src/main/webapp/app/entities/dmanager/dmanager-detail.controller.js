(function() {
    'use strict';

    angular
        .module('laakariApp')
        .controller('DmanagerDetailController', DmanagerDetailController);

    DmanagerDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Dmanager', 'Lprofil', 'Luser'];

    function DmanagerDetailController($scope, $rootScope, $stateParams, previousState, entity, Dmanager, Lprofil, Luser) {
        var vm = this;

        vm.dmanager = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('laakariApp:dmanagerUpdate', function(event, result) {
            vm.dmanager = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
