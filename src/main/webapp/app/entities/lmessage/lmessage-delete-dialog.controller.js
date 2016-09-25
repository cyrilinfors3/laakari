(function() {
    'use strict';

    angular
        .module('laakariApp')
        .controller('LmessageDeleteController',LmessageDeleteController);

    LmessageDeleteController.$inject = ['$uibModalInstance', 'entity', 'Lmessage'];

    function LmessageDeleteController($uibModalInstance, entity, Lmessage) {
        var vm = this;

        vm.lmessage = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Lmessage.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
