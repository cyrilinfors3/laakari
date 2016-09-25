(function() {
    'use strict';

    angular
        .module('laakariApp')
        .controller('LregionDeleteController',LregionDeleteController);

    LregionDeleteController.$inject = ['$uibModalInstance', 'entity', 'Lregion'];

    function LregionDeleteController($uibModalInstance, entity, Lregion) {
        var vm = this;

        vm.lregion = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Lregion.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
