(function() {
    'use strict';

    angular
        .module('laakariApp')
        .controller('LarrondissementDeleteController',LarrondissementDeleteController);

    LarrondissementDeleteController.$inject = ['$uibModalInstance', 'entity', 'Larrondissement'];

    function LarrondissementDeleteController($uibModalInstance, entity, Larrondissement) {
        var vm = this;

        vm.larrondissement = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Larrondissement.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
