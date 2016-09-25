(function() {
    'use strict';

    angular
        .module('laakariApp')
        .controller('DelegueDeleteController',DelegueDeleteController);

    DelegueDeleteController.$inject = ['$uibModalInstance', 'entity', 'Delegue'];

    function DelegueDeleteController($uibModalInstance, entity, Delegue) {
        var vm = this;

        vm.delegue = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Delegue.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
