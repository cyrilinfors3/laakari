(function() {
    'use strict';

    angular
        .module('laakariApp')
        .controller('LzoneDeleteController',LzoneDeleteController);

    LzoneDeleteController.$inject = ['$uibModalInstance', 'entity', 'Lzone'];

    function LzoneDeleteController($uibModalInstance, entity, Lzone) {
        var vm = this;

        vm.lzone = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Lzone.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
