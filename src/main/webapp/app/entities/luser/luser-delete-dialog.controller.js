(function() {
    'use strict';

    angular
        .module('laakariApp')
        .controller('LuserDeleteController',LuserDeleteController);

    LuserDeleteController.$inject = ['$uibModalInstance', 'entity', 'Luser'];

    function LuserDeleteController($uibModalInstance, entity, Luser) {
        var vm = this;

        vm.luser = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Luser.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
