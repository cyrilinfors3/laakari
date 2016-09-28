(function() {
    'use strict';

    angular
        .module('laakariApp')
        .controller('LpointDeleteController',LpointDeleteController);

    LpointDeleteController.$inject = ['$uibModalInstance', 'entity', 'Lpoint'];

    function LpointDeleteController($uibModalInstance, entity, Lpoint) {
        var vm = this;

        vm.lpoint = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Lpoint.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
