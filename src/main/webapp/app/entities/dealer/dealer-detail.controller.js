(function() {
    'use strict';

    angular
        .module('laakariApp')
        .controller('DealerDetailController', DealerDetailController);

    DealerDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Dealer', 'Lprofil', 'Luser'];

    function DealerDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Dealer, Lprofil, Luser) {
        var vm = this;

        vm.dealer = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('laakariApp:dealerUpdate', function(event, result) {
            vm.dealer = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
