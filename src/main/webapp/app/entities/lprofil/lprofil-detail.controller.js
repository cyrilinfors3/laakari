(function() {
    'use strict';

    angular
        .module('laakariApp')
        .controller('LprofilDetailController', LprofilDetailController);

    LprofilDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Lprofil', 'Dmanager', 'Dealer', 'Sdealer', 'Delegue'];

    function LprofilDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Lprofil, Dmanager, Dealer, Sdealer, Delegue) {
        var vm = this;

        vm.lprofil = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('laakariApp:lprofilUpdate', function(event, result) {
            vm.lprofil = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
