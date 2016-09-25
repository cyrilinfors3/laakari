(function() {
    'use strict';

    angular
        .module('laakariApp')
        .controller('LproduitDeleteController',LproduitDeleteController);

    LproduitDeleteController.$inject = ['$uibModalInstance', 'entity', 'Lproduit'];

    function LproduitDeleteController($uibModalInstance, entity, Lproduit) {
        var vm = this;

        vm.lproduit = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Lproduit.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
