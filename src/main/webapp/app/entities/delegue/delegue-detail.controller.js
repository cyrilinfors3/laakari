(function() {
    'use strict';

    angular
        .module('laakariApp')
        .controller('DelegueDetailController', DelegueDetailController);

    DelegueDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Delegue', 'Lprofil', 'Luser'];

    function DelegueDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Delegue, Lprofil, Luser) {
        var vm = this;

        vm.delegue = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('laakariApp:delegueUpdate', function(event, result) {
            vm.delegue = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
