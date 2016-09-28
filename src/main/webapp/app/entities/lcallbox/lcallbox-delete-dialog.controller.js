(function() {
    'use strict';

    angular
        .module('laakariApp')
        .controller('LcallboxDeleteController',LcallboxDeleteController);

    LcallboxDeleteController.$inject = ['$uibModalInstance', 'entity', 'Lcallbox'];

    function LcallboxDeleteController($uibModalInstance, entity, Lcallbox) {
        var vm = this;

        vm.lcallbox = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Lcallbox.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
