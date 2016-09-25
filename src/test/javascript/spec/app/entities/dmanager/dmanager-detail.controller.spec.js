'use strict';

describe('Controller Tests', function() {

    describe('Dmanager Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockDmanager, MockLprofil, MockLuser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockDmanager = jasmine.createSpy('MockDmanager');
            MockLprofil = jasmine.createSpy('MockLprofil');
            MockLuser = jasmine.createSpy('MockLuser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Dmanager': MockDmanager,
                'Lprofil': MockLprofil,
                'Luser': MockLuser
            };
            createController = function() {
                $injector.get('$controller')("DmanagerDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'laakariApp:dmanagerUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
