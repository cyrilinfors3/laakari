(function() {
    'use strict';

    angular
        .module('laakariApp')
        .controller('LprofilDeleteController',LprofilDeleteController);

    LprofilDeleteController.$inject = ['$uibModalInstance', 'entity', 'Lprofil'];

    function LprofilDeleteController($uibModalInstance, entity, Lprofil) {
        var vm = this;

        vm.lprofil = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Lprofil.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
