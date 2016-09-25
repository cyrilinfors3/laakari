(function() {
    'use strict';

    angular
        .module('laakariApp')
        .controller('LcarteDeleteController',LcarteDeleteController);

    LcarteDeleteController.$inject = ['$uibModalInstance', 'entity', 'Lcarte'];

    function LcarteDeleteController($uibModalInstance, entity, Lcarte) {
        var vm = this;

        vm.lcarte = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Lcarte.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
