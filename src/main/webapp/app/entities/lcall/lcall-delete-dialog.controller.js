(function() {
    'use strict';

    angular
        .module('laakariApp')
        .controller('LcallDeleteController',LcallDeleteController);

    LcallDeleteController.$inject = ['$uibModalInstance', 'entity', 'Lcall'];

    function LcallDeleteController($uibModalInstance, entity, Lcall) {
        var vm = this;

        vm.lcall = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Lcall.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
