(function() {
    'use strict';

    angular
        .module('laakariApp')
        .controller('LcallboxDetailController', LcallboxDetailController);

    LcallboxDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Lcallbox', 'Lvisit', 'Lpoint', 'Lroute'];

    function LcallboxDetailController($scope, $rootScope, $stateParams, previousState, entity, Lcallbox, Lvisit, Lpoint, Lroute) {
        var vm = this;

        vm.lcallbox = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('laakariApp:lcallboxUpdate', function(event, result) {
            vm.lcallbox = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
