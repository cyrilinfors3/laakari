(function() {
    'use strict';

    angular
        .module('laakariApp')
        .controller('LzoneDialogController', LzoneDialogController);

    LzoneDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Lzone', 'Lcarte', 'Lroute', 'Lregion', 'Larrondissement', 'Lville', 'Luser'];

    function LzoneDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Lzone, Lcarte, Lroute, Lregion, Larrondissement, Lville, Luser) {
        var vm = this;

        vm.lzone = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.lcartes = Lcarte.query({filter: 'lzone-is-null'});
        $q.all([vm.lzone.$promise, vm.lcartes.$promise]).then(function() {
            if (!vm.lzone.lcarte || !vm.lzone.lcarte.id) {
                return $q.reject();
            }
            return Lcarte.get({id : vm.lzone.lcarte.id}).$promise;
        }).then(function(lcarte) {
            vm.lcartes.push(lcarte);
        });
        vm.lroutes = Lroute.query();
        vm.lregions = Lregion.query({filter: 'lzone-is-null'});
        $q.all([vm.lzone.$promise, vm.lregions.$promise]).then(function() {
            if (!vm.lzone.lregion || !vm.lzone.lregion.id) {
                return $q.reject();
            }
            return Lregion.get({id : vm.lzone.lregion.id}).$promise;
        }).then(function(lregion) {
            vm.lregions.push(lregion);
        });
        vm.larrondissements = Larrondissement.query();
        vm.lvilles = Lville.query();
        vm.lusers = Luser.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.lzone.id !== null) {
                Lzone.update(vm.lzone, onSaveSuccess, onSaveError);
            } else {
                Lzone.save(vm.lzone, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('laakariApp:lzoneUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.datecreation = false;
        vm.datePickerOpenStatus.datemodif = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
