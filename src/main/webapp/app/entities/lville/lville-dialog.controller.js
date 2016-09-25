(function() {
    'use strict';

    angular
        .module('laakariApp')
        .controller('LvilleDialogController', LvilleDialogController);

    LvilleDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Lville', 'Lzone'];

    function LvilleDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Lville, Lzone) {
        var vm = this;

        vm.lville = entity;
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
            if (vm.lville.id !== null) {
                Lville.update(vm.lville, onSaveSuccess, onSaveError);
            } else {
                Lville.save(vm.lville, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('laakariApp:lvilleUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
