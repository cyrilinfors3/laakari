(function() {
    'use strict';

    angular
        .module('laakariApp')
        .controller('LvisitDeleteController',LvisitDeleteController);

    LvisitDeleteController.$inject = ['$uibModalInstance', 'entity', 'Lvisit'];

    function LvisitDeleteController($uibModalInstance, entity, Lvisit) {
        var vm = this;

        vm.lvisit = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Lvisit.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
