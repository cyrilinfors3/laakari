(function() {
    'use strict';

    angular
        .module('laakariApp')
        .controller('LrouteDeleteController',LrouteDeleteController);

    LrouteDeleteController.$inject = ['$uibModalInstance', 'entity', 'Lroute'];

    function LrouteDeleteController($uibModalInstance, entity, Lroute) {
        var vm = this;

        vm.lroute = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Lroute.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
