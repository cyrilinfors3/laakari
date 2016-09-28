(function() {
    'use strict';

    angular
        .module('laakariApp')
        .controller('LvisitDialogController', LvisitDialogController);

    LvisitDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Lvisit', 'Lcallbox'];

    function LvisitDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Lvisit, Lcallbox) {
        var vm = this;

        vm.lvisit = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.lcallboxes = Lcallbox.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.lvisit.id !== null) {
                Lvisit.update(vm.lvisit, onSaveSuccess, onSaveError);
            } else {
                Lvisit.save(vm.lvisit, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('laakariApp:lvisitUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.date = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
