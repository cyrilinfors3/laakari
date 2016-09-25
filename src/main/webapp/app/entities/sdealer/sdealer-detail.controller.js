(function() {
    'use strict';

    angular
        .module('laakariApp')
        .controller('SdealerDetailController', SdealerDetailController);

    SdealerDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Sdealer', 'Lprofil', 'Luser'];

    function SdealerDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Sdealer, Lprofil, Luser) {
        var vm = this;

        vm.sdealer = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('laakariApp:sdealerUpdate', function(event, result) {
            vm.sdealer = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
