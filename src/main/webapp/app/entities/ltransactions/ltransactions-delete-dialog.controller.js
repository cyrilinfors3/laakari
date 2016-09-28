(function() {
    'use strict';

    angular
        .module('laakariApp')
        .controller('LtransactionsDeleteController',LtransactionsDeleteController);

    LtransactionsDeleteController.$inject = ['$uibModalInstance', 'entity', 'Ltransactions'];

    function LtransactionsDeleteController($uibModalInstance, entity, Ltransactions) {
        var vm = this;

        vm.ltransactions = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Ltransactions.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
