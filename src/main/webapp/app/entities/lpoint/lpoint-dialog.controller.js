(function() {
    'use strict';

    angular
        .module('laakariApp')
        .controller('LpointDialogController', LpointDialogController);

    LpointDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Lpoint', 'Lcarte', 'Lcallbox'];

    function LpointDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Lpoint, Lcarte, Lcallbox) {
        var vm = this;

        vm.lpoint = entity;
        vm.clear = clear;
        vm.save = save;
        vm.lcartes = Lcarte.query();
        vm.lcallboxes = Lcallbox.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.lpoint.id !== null) {
                Lpoint.update(vm.lpoint, onSaveSuccess, onSaveError);
            } else {
                Lpoint.save(vm.lpoint, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('laakariApp:lpointUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
