(function() {
    'use strict';

    angular
        .module('laakariApp')
        .controller('LvilleDeleteController',LvilleDeleteController);

    LvilleDeleteController.$inject = ['$uibModalInstance', 'entity', 'Lville'];

    function LvilleDeleteController($uibModalInstance, entity, Lville) {
        var vm = this;

        vm.lville = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Lville.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
