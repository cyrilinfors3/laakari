(function() {
    'use strict';

    angular
        .module('laakariApp')
        .controller('SdealerDeleteController',SdealerDeleteController);

    SdealerDeleteController.$inject = ['$uibModalInstance', 'entity', 'Sdealer'];

    function SdealerDeleteController($uibModalInstance, entity, Sdealer) {
        var vm = this;

        vm.sdealer = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Sdealer.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
