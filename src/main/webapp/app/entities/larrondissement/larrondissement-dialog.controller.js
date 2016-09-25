(function() {
    'use strict';

    angular
        .module('laakariApp')
        .controller('LarrondissementDialogController', LarrondissementDialogController);

    LarrondissementDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Larrondissement', 'Lzone'];

    function LarrondissementDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Larrondissement, Lzone) {
        var vm = this;

        vm.larrondissement = entity;
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
            if (vm.larrondissement.id !== null) {
                Larrondissement.update(vm.larrondissement, onSaveSuccess, onSaveError);
            } else {
                Larrondissement.save(vm.larrondissement, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('laakariApp:larrondissementUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
