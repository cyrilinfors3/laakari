(function() {
    'use strict';

    angular
        .module('laakariApp')
        .controller('DmanagerDeleteController',DmanagerDeleteController);

    DmanagerDeleteController.$inject = ['$uibModalInstance', 'entity', 'Dmanager'];

    function DmanagerDeleteController($uibModalInstance, entity, Dmanager) {
        var vm = this;

        vm.dmanager = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Dmanager.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
