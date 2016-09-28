(function() {
    'use strict';

    angular
        .module('laakariApp')
        .controller('DmanagerDialogController', DmanagerDialogController);

    DmanagerDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Dmanager', 'Lprofil', 'Luser'];

    function DmanagerDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Dmanager, Lprofil, Luser) {
        var vm = this;

        vm.dmanager = entity;
        vm.clear = clear;
        vm.save = save;
        vm.lprofils = Lprofil.query();
        vm.lusers = Luser.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.dmanager.id !== null) {
                Dmanager.update(vm.dmanager, onSaveSuccess, onSaveError);
            } else {
                Dmanager.save(vm.dmanager, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('laakariApp:dmanagerUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
