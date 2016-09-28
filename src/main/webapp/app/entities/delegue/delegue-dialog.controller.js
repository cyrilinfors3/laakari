(function() {
    'use strict';

    angular
        .module('laakariApp')
        .controller('DelegueDialogController', DelegueDialogController);

    DelegueDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'DataUtils', 'entity', 'Delegue', 'Lprofil', 'Luser'];

    function DelegueDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, DataUtils, entity, Delegue, Lprofil, Luser) {
        var vm = this;

        vm.delegue = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.lprofils = Lprofil.query({filter: 'delegue-is-null'});
        $q.all([vm.delegue.$promise, vm.lprofils.$promise]).then(function() {
            if (!vm.delegue.lprofil || !vm.delegue.lprofil.id) {
                return $q.reject();
            }
            return Lprofil.get({id : vm.delegue.lprofil.id}).$promise;
        }).then(function(lprofil) {
            vm.lprofils.push(lprofil);
        });
        vm.lusers = Luser.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.delegue.id !== null) {
                Delegue.update(vm.delegue, onSaveSuccess, onSaveError);
            } else {
                Delegue.save(vm.delegue, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('laakariApp:delegueUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.issuedate = false;
        vm.datePickerOpenStatus.dateofbirth = false;

        vm.setPic = function ($file, delegue) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        delegue.pic = base64Data;
                        delegue.picContentType = $file.type;
                    });
                });
            }
        };

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
