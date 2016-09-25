(function() {
    'use strict';

    angular
        .module('laakariApp')
        .controller('LfactureDeleteController',LfactureDeleteController);

    LfactureDeleteController.$inject = ['$uibModalInstance', 'entity', 'Lfacture'];

    function LfactureDeleteController($uibModalInstance, entity, Lfacture) {
        var vm = this;

        vm.lfacture = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Lfacture.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
