(function() {
    'use strict';

    angular
        .module('laakariApp')
        .controller('LmessageDialogController', LmessageDialogController);

    LmessageDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Lmessage', 'Luser'];

    function LmessageDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Lmessage, Luser) {
        var vm = this;

        vm.lmessage = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.lusers = Luser.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.lmessage.id !== null) {
                Lmessage.update(vm.lmessage, onSaveSuccess, onSaveError);
            } else {
                Lmessage.save(vm.lmessage, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('laakariApp:lmessageUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.sentdate = false;
        vm.datePickerOpenStatus.senthours = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
