'use strict';

describe('Controller Tests', function() {

    describe('Sdealer Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockSdealer, MockLprofil, MockLuser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockSdealer = jasmine.createSpy('MockSdealer');
            MockLprofil = jasmine.createSpy('MockLprofil');
            MockLuser = jasmine.createSpy('MockLuser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Sdealer': MockSdealer,
                'Lprofil': MockLprofil,
                'Luser': MockLuser
            };
            createController = function() {
                $injector.get('$controller')("SdealerDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'laakariApp:sdealerUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
