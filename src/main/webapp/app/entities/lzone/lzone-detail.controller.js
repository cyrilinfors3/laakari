(function() {
    'use strict';

    angular
        .module('laakariApp')
        .controller('LzoneDetailController', LzoneDetailController);

    LzoneDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Lzone', 'Lcarte', 'Lroute', 'Lregion', 'Larrondissement', 'Lville', 'Luser'];

    function LzoneDetailController($scope, $rootScope, $stateParams, previousState, entity, Lzone, Lcarte, Lroute, Lregion, Larrondissement, Lville, Luser) {
        var vm = this;

        vm.lzone = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('laakariApp:lzoneUpdate', function(event, result) {
            vm.lzone = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
