(function() {
    'use strict';

    angular
        .module('laakariApp')
        .controller('LarrondissementDetailController', LarrondissementDetailController);

    LarrondissementDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Larrondissement', 'Lzone'];

    function LarrondissementDetailController($scope, $rootScope, $stateParams, previousState, entity, Larrondissement, Lzone) {
        var vm = this;

        vm.larrondissement = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('laakariApp:larrondissementUpdate', function(event, result) {
            vm.larrondissement = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
