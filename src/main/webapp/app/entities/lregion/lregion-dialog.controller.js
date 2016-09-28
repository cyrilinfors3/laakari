(function() {
    'use strict';

    angular
        .module('laakariApp')
        .controller('LregionDialogController', LregionDialogController);

    LregionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Lregion', 'Lzone'];

    function LregionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Lregion, Lzone) {
        var vm = this;

        vm.lregion = entity;
        vm.clear = clear;
        vm.save = save;
        vm.lzones = Lzone.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.lregion.id !== null) {
                Lregion.update(vm.lregion, onSaveSuccess, onSaveError);
            } else {
                Lregion.save(vm.lregion, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('laakariApp:lregionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
