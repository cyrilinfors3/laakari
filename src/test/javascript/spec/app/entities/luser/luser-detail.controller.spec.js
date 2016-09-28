'use strict';

describe('Controller Tests', function() {

    describe('Luser Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockLuser, MockDmanager, MockDealer, MockSdealer, MockDelegue, MockLzone;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockLuser = jasmine.createSpy('MockLuser');
            MockDmanager = jasmine.createSpy('MockDmanager');
            MockDealer = jasmine.createSpy('MockDealer');
            MockSdealer = jasmine.createSpy('MockSdealer');
            MockDelegue = jasmine.createSpy('MockDelegue');
            MockLzone = jasmine.createSpy('MockLzone');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Luser': MockLuser,
                'Dmanager': MockDmanager,
                'Dealer': MockDealer,
                'Sdealer': MockSdealer,
                'Delegue': MockDelegue,
                'Lzone': MockLzone
            };
            createController = function() {
                $injector.get('$controller')("LuserDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'laakariApp:luserUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
